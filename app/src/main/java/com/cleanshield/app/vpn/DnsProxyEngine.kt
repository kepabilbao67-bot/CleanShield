package com.cleanshield.app.vpn

import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import com.cleanshield.app.utils.Constants
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * Full DNS proxy engine that intercepts DNS queries, checks them against the filter,
 * and either blocks them (responds with 0.0.0.0) or forwards to upstream DNS.
 */
class DnsProxyEngine(
    private val vpnFileDescriptor: ParcelFileDescriptor,
    private val dnsFilter: DnsFilter,
    private val vpnService: VpnService
) {
    private val running = AtomicBoolean(false)
    private val blockedCount = AtomicInteger(0)
    private val totalQueries = AtomicInteger(0)

    private var readThread: Thread? = null
    private val executor: ExecutorService = Executors.newFixedThreadPool(4)

    private val inputStream: FileInputStream by lazy {
        FileInputStream(vpnFileDescriptor.fileDescriptor)
    }
    private val outputStream: FileOutputStream by lazy {
        FileOutputStream(vpnFileDescriptor.fileDescriptor)
    }

    fun start() {
        if (running.getAndSet(true)) return

        readThread = Thread({
            processPackets()
        }, "DnsProxy-Reader").apply {
            isDaemon = true
            start()
        }

        Log.d(TAG, "DNS Proxy Engine started")
    }

    fun stop() {
        running.set(false)
        executor.shutdownNow()
        readThread?.interrupt()
        readThread = null

        try {
            inputStream.close()
        } catch (_: Exception) {}
        try {
            outputStream.close()
        } catch (_: Exception) {}

        Log.d(TAG, "DNS Proxy Engine stopped. Blocked: ${blockedCount.get()}/${totalQueries.get()}")
    }

    private fun processPackets() {
        val buffer = ByteArray(Constants.Vpn.MAX_PACKET_SIZE)

        while (running.get()) {
            try {
                val length = inputStream.read(buffer)
                if (length <= 0) {
                    Thread.sleep(10)
                    continue
                }

                // Parse IP header to extract UDP payload
                val packet = buffer.copyOf(length)
                handlePacket(packet, length)

            } catch (e: InterruptedException) {
                break
            } catch (e: Exception) {
                if (running.get()) {
                    Log.w(TAG, "Error reading packet", e)
                }
            }
        }
    }

    private fun handlePacket(packet: ByteArray, length: Int) {
        // Verify it's an IPv4 packet
        if (length < 20) return
        val version = (packet[0].toInt() shr 4) and 0xF
        if (version != 4) return

        // Get IP header length
        val ihl = (packet[0].toInt() and 0xF) * 4
        if (length < ihl + 8) return

        // Check protocol (17 = UDP)
        val protocol = packet[9].toInt() and 0xFF
        if (protocol != 17) return

        // Get destination port from UDP header
        val destPort = ((packet[ihl + 2].toInt() and 0xFF) shl 8) or
                (packet[ihl + 3].toInt() and 0xFF)

        // Only handle DNS traffic (port 53)
        if (destPort != Constants.Vpn.DNS_PORT) return

        // Extract UDP payload (DNS packet)
        val udpHeaderSize = 8
        val dnsOffset = ihl + udpHeaderSize
        val dnsLength = length - dnsOffset
        if (dnsLength <= 0) return

        val dnsPacket = packet.copyOfRange(dnsOffset, dnsOffset + dnsLength)

        totalQueries.incrementAndGet()

        // Parse DNS query
        val domain = DnsPacketParser.extractDomain(dnsPacket, dnsLength) ?: return

        if (dnsFilter.shouldBlock(domain)) {
            // Create blocked response and send back
            blockedCount.incrementAndGet()
            val blockedResponse = DnsPacketParser.createBlockedResponse(dnsPacket, dnsLength)
            if (blockedResponse != null) {
                sendDnsResponse(packet, ihl, blockedResponse)
            }
        } else {
            // Forward to upstream DNS
            executor.submit {
                forwardDnsQuery(packet, ihl, dnsPacket, dnsLength)
            }
        }
    }

    private fun sendDnsResponse(originalPacket: ByteArray, ihl: Int, dnsResponse: ByteArray) {
        try {
            val udpHeaderSize = 8
            val totalLength = ihl + udpHeaderSize + dnsResponse.size
            val responsePacket = ByteArray(totalLength)

            // Copy and modify IP header
            System.arraycopy(originalPacket, 0, responsePacket, 0, ihl)

            // Swap source and destination IP
            System.arraycopy(originalPacket, 12, responsePacket, 16, 4) // src -> dst
            System.arraycopy(originalPacket, 16, responsePacket, 12, 4) // dst -> src

            // Update IP total length
            responsePacket[2] = ((totalLength shr 8) and 0xFF).toByte()
            responsePacket[3] = (totalLength and 0xFF).toByte()

            // Clear IP checksum (will be recalculated by kernel)
            responsePacket[10] = 0
            responsePacket[11] = 0

            // Calculate IP checksum
            val ipChecksum = calculateChecksum(responsePacket, 0, ihl)
            responsePacket[10] = ((ipChecksum shr 8) and 0xFF).toByte()
            responsePacket[11] = (ipChecksum and 0xFF).toByte()

            // UDP header - swap ports
            responsePacket[ihl] = originalPacket[ihl + 2]     // src port = original dst
            responsePacket[ihl + 1] = originalPacket[ihl + 3]
            responsePacket[ihl + 2] = originalPacket[ihl]     // dst port = original src
            responsePacket[ihl + 3] = originalPacket[ihl + 1]

            // UDP length
            val udpLength = udpHeaderSize + dnsResponse.size
            responsePacket[ihl + 4] = ((udpLength shr 8) and 0xFF).toByte()
            responsePacket[ihl + 5] = (udpLength and 0xFF).toByte()

            // UDP checksum (0 = disabled for IPv4)
            responsePacket[ihl + 6] = 0
            responsePacket[ihl + 7] = 0

            // DNS payload
            System.arraycopy(dnsResponse, 0, responsePacket, ihl + udpHeaderSize, dnsResponse.size)

            // Write response back through VPN
            synchronized(outputStream) {
                outputStream.write(responsePacket)
                outputStream.flush()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error sending blocked response", e)
        }
    }

    private fun forwardDnsQuery(originalPacket: ByteArray, ihl: Int, dnsPacket: ByteArray, dnsLength: Int) {
        try {
            val socket = DatagramSocket()
            vpnService.protect(socket)

            val dnsServer = InetAddress.getByName(Constants.Vpn.VPN_DNS_PRIMARY)
            val request = DatagramPacket(dnsPacket, dnsLength, dnsServer, Constants.Vpn.DNS_PORT)
            socket.soTimeout = Constants.Vpn.DNS_QUERY_TIMEOUT_MS.toInt()
            socket.send(request)

            val responseBuffer = ByteArray(1024)
            val response = DatagramPacket(responseBuffer, responseBuffer.size)
            socket.receive(response)
            socket.close()

            // Send DNS response back through VPN
            val dnsResponse = responseBuffer.copyOf(response.length)
            sendDnsResponse(originalPacket, ihl, dnsResponse)

        } catch (e: Exception) {
            Log.w(TAG, "Error forwarding DNS query", e)
        }
    }

    private fun calculateChecksum(data: ByteArray, offset: Int, length: Int): Int {
        var sum = 0L
        var i = offset
        val end = offset + length

        while (i < end - 1) {
            sum += ((data[i].toInt() and 0xFF) shl 8) or (data[i + 1].toInt() and 0xFF)
            i += 2
        }

        if (i < end) {
            sum += (data[i].toInt() and 0xFF) shl 8
        }

        while (sum shr 16 > 0) {
            sum = (sum and 0xFFFF) + (sum shr 16)
        }

        return (sum.inv() and 0xFFFF).toInt()
    }

    fun getBlockedCount(): Int = blockedCount.get()
    fun getTotalQueries(): Int = totalQueries.get()

    companion object {
        private const val TAG = "DnsProxyEngine"
    }
}
