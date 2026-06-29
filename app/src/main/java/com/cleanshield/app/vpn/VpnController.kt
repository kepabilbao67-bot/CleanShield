package com.cleanshield.app.vpn

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

/**
 * Punto unico para activar/desactivar la proteccion VPN y consultar su estado.
 *
 * El estado se guarda en SharedPreferences para que la interfaz pueda reflejar
 * si la proteccion esta realmente encendida (en vez de mostrarlo fijo).
 * El propio [CleanShieldVpnService] actualiza este flag al arrancar y al parar.
 */
object VpnController {

    private const val PREFS = "cleanshield_protection"
    private const val KEY_ENABLED = "vpn_enabled"

    fun isEnabled(context: Context): Boolean =
        context.applicationContext
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(KEY_ENABLED, false)

    fun setEnabled(context: Context, enabled: Boolean) {
        context.applicationContext
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_ENABLED, enabled)
            .apply()
    }

    /** Arranca el servicio VPN. Requiere que el permiso ya haya sido concedido. */
    fun startProtection(context: Context) {
        val intent = Intent(context, CleanShieldVpnService::class.java).apply {
            action = CleanShieldVpnService.ACTION_START
        }
        ContextCompat.startForegroundService(context, intent)
        setEnabled(context, true)
    }

    /** Detiene el servicio VPN. */
    fun stopProtection(context: Context) {
        val intent = Intent(context, CleanShieldVpnService::class.java).apply {
            action = CleanShieldVpnService.ACTION_STOP
        }
        ContextCompat.startForegroundService(context, intent)
        setEnabled(context, false)
    }
}
