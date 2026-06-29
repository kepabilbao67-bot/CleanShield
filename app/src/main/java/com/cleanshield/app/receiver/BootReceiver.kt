package com.cleanshield.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.cleanshield.app.blocker.AppBlockerService
import com.cleanshield.app.vpn.CleanShieldVpnService
import com.cleanshield.app.vpn.VpnController

/**
 * Receives BOOT_COMPLETED broadcast to automatically restart protection services.
 * Ensures protection is always active even after device restarts.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_LOCKED_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                Log.d(TAG, "Boot/package event received: ${intent.action}")
                startProtectionServices(context)
            }
        }
    }

    private fun startProtectionServices(context: Context) {
        try {
            // Start App Blocker Service
            val blockerIntent = Intent(context, AppBlockerService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(blockerIntent)
            } else {
                context.startService(blockerIntent)
            }
            Log.d(TAG, "App Blocker service started on boot")

            // Start VPN Service solo si el usuario tenia la proteccion activada
            if (!VpnController.isEnabled(context)) {
                Log.d(TAG, "Proteccion VPN desactivada por el usuario, no se reinicia")
                return
            }
            val vpnIntent = Intent(context, CleanShieldVpnService::class.java).apply {
                action = CleanShieldVpnService.ACTION_START
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(vpnIntent)
            } else {
                context.startService(vpnIntent)
            }
            Log.d(TAG, "VPN service started on boot")

        } catch (e: Exception) {
            Log.e(TAG, "Error starting services on boot", e)
        }
    }

    companion object {
        private const val TAG = "CSBootReceiver"
    }
}
