package com.cleanshield.app.admin

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

/**
 * Device Admin Receiver that prevents uninstallation of CleanShield.
 * When activated, the user cannot uninstall the app without first
 * disabling the device admin (which requires the protection password).
 */
class CleanShieldDeviceAdmin : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Log.d(TAG, "Device Admin enabled - uninstall protection active")
        Toast.makeText(
            context,
            "Protección contra desinstalación activada",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        Log.d(TAG, "Device Admin disabled - uninstall protection removed")
        Toast.makeText(
            context,
            "Protección contra desinstalación desactivada",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDisableRequested(context: Context, intent: Intent): CharSequence {
        Log.w(TAG, "Device Admin disable requested")
        return "¿Estás seguro? Desactivar el administrador del dispositivo " +
                "permitirá desinstalar CleanShield y eliminar toda la protección. " +
                "Necesitarás tu contraseña de protección para continuar."
    }

    override fun onPasswordFailed(context: Context, intent: Intent, userHandle: android.os.UserHandle) {
        super.onPasswordFailed(context, intent, userHandle)
        Log.w(TAG, "Password failed attempt detected")
    }

    override fun onPasswordSucceeded(context: Context, intent: Intent, userHandle: android.os.UserHandle) {
        super.onPasswordSucceeded(context, intent, userHandle)
        Log.d(TAG, "Password succeeded")
    }

    companion object {
        private const val TAG = "CSDeviceAdmin"
    }
}
