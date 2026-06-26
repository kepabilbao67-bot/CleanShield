package com.cleanshield.app.vpn

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.core.app.NotificationCompat
import com.cleanshield.app.CleanShieldApp
import com.cleanshield.app.R
import com.cleanshield.app.ui.MainActivity
import com.cleanshield.app.utils.Constants

class CleanShieldVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null
    private var dnsProxyEngine: DnsProxyEngine? = null
    private var isRunning = false

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "VPN Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startVpn()
            ACTION_STOP -> stopVpn()
        }
        return START_STICKY
    }

    private fun startVpn() {
        if (isRunning) return

        startForeground(CleanShieldApp.NOTIFICATION_ID_VPN, createNotification())

        try {
            val builder = Builder()
                .setSession(Constants.Vpn.SESSION_NAME)
                .setMtu(Constants.Vpn.VPN_MTU)
                .addAddress(Constants.Vpn.VPN_ADDRESS, 32)
                .addRoute(Constants.Vpn.VPN_DNS_PRIMARY, 32)
                .addRoute(Constants.Vpn.VPN_DNS_SECONDARY, 32)
                .addDnsServer(Constants.Vpn.VPN_DNS_PRIMARY)
                .addDnsServer(Constants.Vpn.VPN_DNS_SECONDARY)

            // Exclude our own app from VPN
            builder.addDisallowedApplication(packageName)

            vpnInterface = builder.establish()

            vpnInterface?.let { pfd ->
                val dnsFilter = DnsFilter()
                dnsProxyEngine = DnsProxyEngine(pfd, dnsFilter, this)
                dnsProxyEngine?.start()
                isRunning = true
                Log.d(TAG, "VPN started successfully")
            } ?: run {
                Log.e(TAG, "Failed to establish VPN interface")
                stopSelf()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error starting VPN", e)
            stopSelf()
        }
    }

    private fun stopVpn() {
        isRunning = false
        dnsProxyEngine?.stop()
        dnsProxyEngine = null
        vpnInterface?.close()
        vpnInterface = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        Log.d(TAG, "VPN stopped")
    }

    override fun onDestroy() {
        stopVpn()
        super.onDestroy()
    }

    override fun onRevoke() {
        stopVpn()
        super.onRevoke()
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CleanShieldApp.CHANNEL_VPN)
            .setContentTitle("CleanShield VPN Activo")
            .setContentText("Tu navegación está protegida")
            .setSmallIcon(R.drawable.ic_shield)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    companion object {
        private const val TAG = "CleanShieldVPN"
        const val ACTION_START = "com.cleanshield.vpn.START"
        const val ACTION_STOP = "com.cleanshield.vpn.STOP"
    }
}
