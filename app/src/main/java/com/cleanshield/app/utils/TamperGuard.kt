package com.cleanshield.app.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.cleanshield.app.vpn.DnsLockManager

/**
 * Lógica central anti-manipulación.
 *
 * Comprueba si la protección de DNS sigue intacta. Si alguien la cambió:
 *  - Si la app puede escribir ajustes (permiso ADB) -> la REAPLICA al instante.
 *  - Avisa al tutor por SMS (con límite de frecuencia).
 *
 * Se llama periódicamente desde el servicio de accesibilidad y al detectar
 * que se abre la pantalla de "DNS privado".
 */
object TamperGuard {

    private const val TAG = "TamperGuard"

    /**
     * Comprueba el estado y reacciona si hay manipulación.
     * @return true si la protección está intacta (o se restauró), false si está comprometida.
     */
    fun enforce(context: Context): Boolean {
        val appContext = context.applicationContext
        val prefs = EncryptedPrefsManager(appContext)

        if (!prefs.isDnsLockEnabled) return true

        val host = prefs.dnsHost
        if (DnsLockManager.isCorrect(appContext, host)) return true

        // El DNS NO es el correcto -> intento de manipulación
        Log.w(TAG, "Manipulación detectada: el DNS privado no es $host")

        var restored = false
        if (DnsLockManager.canWrite(appContext)) {
            restored = DnsLockManager.apply(appContext, host)
        }

        alertGuardian(
            appContext,
            prefs,
            if (restored)
                "CleanShield: se intentó desactivar el filtro de contenido, pero se restauró automáticamente."
            else
                "CleanShield: ATENCIÓN, el filtro de contenido (DNS) fue desactivado en el móvil y no se pudo restaurar solo."
        )

        return restored
    }

    /** Envía un aviso al tutor, respetando un límite de frecuencia. */
    fun alertGuardian(context: Context, prefs: EncryptedPrefsManager, message: String) {
        val now = System.currentTimeMillis()
        if (now - prefs.lastTamperAlert < Constants.Dns.ALERT_THROTTLE_MS) return

        val phone = prefs.guardianPhone
        if (phone.isNullOrBlank()) return

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w(TAG, "Sin permiso SEND_SMS, no se puede avisar al tutor")
            return
        }

        prefs.lastTamperAlert = now
        try {
            val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                context.getSystemService(SmsManager::class.java)
            } else {
                @Suppress("DEPRECATION")
                SmsManager.getDefault()
            }
            smsManager?.sendTextMessage(phone, null, message, null, null)
            Log.d(TAG, "Aviso enviado al tutor")
        } catch (e: Exception) {
            Log.w(TAG, "No se pudo enviar el SMS al tutor", e)
        }
    }
}
