package com.cleanshield.app.vpn

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import com.cleanshield.app.utils.Constants

/**
 * Gestiona el "DNS privado" de Android (DNS-over-TLS a nivel de sistema).
 *
 * Apuntando el DNS privado a un proveedor con filtro familiar
 * (p.ej. family.adguard-dns.com) se bloquea el porno en TODO el movil,
 * de forma cifrada y para todas las apps/navegadores.
 *
 * - LEER el estado del DNS no requiere permisos especiales.
 * - ESCRIBIR el DNS requiere el permiso WRITE_SECURE_SETTINGS, que se concede
 *   una sola vez por ADB:
 *       adb shell pm grant com.cleanshield.app.debug android.permission.WRITE_SECURE_SETTINGS
 *   Si esta concedido, la app puede REAPLICAR el DNS automaticamente cuando
 *   alguien intente cambiarlo (bloqueo real). Si no, la app solo puede
 *   detectar el cambio y avisar al tutor.
 */
object DnsLockManager {

    private const val TAG = "DnsLockManager"

    /** ¿El DNS privado esta puesto en el host familiar esperado? */
    fun isCorrect(context: Context, desiredHost: String): Boolean {
        val mode = currentMode(context)
        val specifier = currentSpecifier(context)
        return mode == Constants.Dns.MODE_HOSTNAME &&
                specifier.equals(desiredHost, ignoreCase = true)
    }

    fun currentMode(context: Context): String? = try {
        Settings.Global.getString(context.contentResolver, Constants.Dns.SETTING_MODE)
    } catch (e: Exception) {
        Log.w(TAG, "No se pudo leer private_dns_mode", e)
        null
    }

    fun currentSpecifier(context: Context): String? = try {
        Settings.Global.getString(context.contentResolver, Constants.Dns.SETTING_SPECIFIER)
    } catch (e: Exception) {
        Log.w(TAG, "No se pudo leer private_dns_specifier", e)
        null
    }

    /** ¿Tenemos permiso para escribir ajustes del sistema (concedido por ADB)? */
    fun canWrite(context: Context): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_SECURE_SETTINGS
        ) == PackageManager.PERMISSION_GRANTED

    /**
     * Fija el DNS privado al host familiar. Solo funciona si [canWrite] es true.
     * @return true si se aplico correctamente.
     */
    fun apply(context: Context, desiredHost: String): Boolean {
        if (!canWrite(context)) return false
        return try {
            Settings.Global.putString(
                context.contentResolver,
                Constants.Dns.SETTING_MODE,
                Constants.Dns.MODE_HOSTNAME
            )
            Settings.Global.putString(
                context.contentResolver,
                Constants.Dns.SETTING_SPECIFIER,
                desiredHost
            )
            Log.d(TAG, "DNS privado reaplicado a $desiredHost")
            true
        } catch (e: Exception) {
            Log.w(TAG, "No se pudo escribir el DNS privado", e)
            false
        }
    }
}
