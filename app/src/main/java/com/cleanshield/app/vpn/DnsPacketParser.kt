package com.cleanshield.app.vpn

import android.util.Log
import java.nio.ByteBuffer

/**
 * Parses DNS queries from raw UDP packets and creates blocked responses (0.0.0.0).
 */
object DnsPacketParser {

    private const val TAG = "DnsPacketParser"

    // DNS header constants
    private const val DNS_HEADER_SIZE = 12
    private const val DNS_FLAGS_RESPONSE: Short = -0x7B80 // 0x8480 (response + no error + recursion)
    private const val DNS_TYPE_A: Short = 1
    private const val DNS_CLASS_IN: Short = 1
    private const val DNS_TTL = 300 // 5 minutes

    /**
     * Extract the queried domain name from a DNS packet.
     * Returns null if the packet is not a valid DNS query.
     */
    fun extractDomain(packet: ByteArray, length: Int): String? {
        if (length < DNS_HEADER_SIZE + 5) return null // Minimum valid DNS query

        try {
            val buffer = ByteBuffer.wrap(packet, 0, length)

            // Skip Transaction ID (2 bytes)
            buffer.position(2)

            // Read flags
            val flags = buffer.short
            val isQuery = (flags.toInt() and 0x8000) == 0
            if (!isQuery) return null

            // Questions count
            buffer.position(4)
            val questions = buffer.short.toInt() and 0xFFFF
            if (questions < 1) return null

            // Skip to question section (after header)
            buffer.position(DNS_HEADER_SIZE)

            // Parse domain name
            val domain = parseDomainName(buffer)
            return domain
        } catch (e: Exception) {
            Log.w(TAG, "Error parsing DNS packet", e)
            return null
        }
    }

    /**
     * Parse a DNS domain name from the buffer position.
     * Domain names are encoded as sequences of labels (length + chars).
     */
    private fun parseDomainName(buffer: ByteBuffer): String {
        val parts = mutableListOf<String>()

        while (buffer.hasRemaining()) {
            val labelLength = buffer.get().toInt() and 0xFF

            if (labelLength == 0) break // End of name

            // Handle DNS name compression (pointer)
            if ((labelLength and 0xC0) == 0xC0) {
                // Compressed label - skip
                buffer.get() // second byte of pointer
                break
            }

            if (labelLength > 63) break // Invalid label

            val label = ByteArray(labelLength)
            buffer.get(label)
            parts.add(String(label, Charsets.US_ASCII))
        }

        return parts.joinToString(".")
    }

    /**
     * Create a DNS response that resolves to 0.0.0.0 (blocked).
     * This effectively blocks the domain by pointing it to a non-routable address.
     */
    fun createBlockedResponse(queryPacket: ByteArray, queryLength: Int): ByteArray? {
        if (queryLength < DNS_HEADER_SIZE + 5) return null

        try {
            val buffer = ByteBuffer.wrap(queryPacket, 0, queryLength)

            // Get transaction ID
            val transactionId = buffer.short

            // Skip to question section to get the full question
            buffer.position(DNS_HEADER_SIZE)

            // Calculate question section length
            val questionStart = DNS_HEADER_SIZE
            var pos = questionStart
            while (pos < queryLength) {
                val len = queryPacket[pos].toInt() and 0xFF
                if (len == 0) {
                    pos++ // null terminator
                    break
                }
                pos += len + 1
            }
            pos += 4 // QTYPE (2) + QCLASS (2)
            val questionLength = pos - questionStart

            // Build response packet
            val responseSize = DNS_HEADER_SIZE + questionLength + 16 // 16 = answer record for A
            val response = ByteBuffer.allocate(responseSize)

            // Header
            response.putShort(transactionId)                  // Transaction ID
            response.putShort(DNS_FLAGS_RESPONSE)             // Flags: response, no error
            response.putShort(1)                              // Questions: 1
            response.putShort(1)                              // Answers: 1
            response.putShort(0)                              // Authority: 0
            response.putShort(0)                              // Additional: 0

            // Copy question section
            response.put(queryPacket, questionStart, questionLength)

            // Answer section (pointing to 0.0.0.0)
            response.putShort(0xC00C.toShort())               // Name pointer to question
            response.putShort(DNS_TYPE_A)                      // Type A
            response.putShort(DNS_CLASS_IN)                    // Class IN
            response.putInt(DNS_TTL)                           // TTL
            response.putShort(4)                              // Data length: 4 bytes (IPv4)
            response.put(0)                                   // 0.0.0.0
            response.put(0)
            response.put(0)
            response.put(0)

            return response.array()
        } catch (e: Exception) {
            Log.e(TAG, "Error creating blocked response", e)
            return null
        }
    }

    /**
     * Check if the packet is a DNS query (as opposed to a response).
     */
    fun isDnsQuery(packet: ByteArray, length: Int): Boolean {
        if (length < DNS_HEADER_SIZE) return false
        val flags = ((packet[2].toInt() and 0xFF) shl 8) or (packet[3].toInt() and 0xFF)
        return (flags and 0x8000) == 0 // QR bit = 0 means query
    }

    /**
     * Get the DNS query type (A, AAAA, etc.)
     */
    fun getQueryType(packet: ByteArray, length: Int): Int? {
        if (length < DNS_HEADER_SIZE + 5) return null
        try {
            val buffer = ByteBuffer.wrap(packet, 0, length)
            buffer.position(DNS_HEADER_SIZE)

            // Skip domain name
            while (buffer.hasRemaining()) {
                val len = buffer.get().toInt() and 0xFF
                if (len == 0) break
                if ((len and 0xC0) == 0xC0) {
                    buffer.get()
                    break
                }
                buffer.position(buffer.position() + len)
            }

            return buffer.short.toInt() and 0xFFFF
        } catch (e: Exception) {
            return null
        }
    }
}
