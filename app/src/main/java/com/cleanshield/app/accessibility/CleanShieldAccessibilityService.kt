package com.cleanshield.app.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.cleanshield.app.ui.blocked.BlockedActivity
import com.cleanshield.app.utils.Constants
import com.cleanshield.app.utils.EncryptedPrefsManager
import com.cleanshield.app.utils.TamperGuard

class CleanShieldAccessibilityService : AccessibilityService() {

    private val blockedPackages: Set<String> by lazy {
        Constants.ALL_BLOCKED_PACKAGES.toHashSet()
    }

    private var lastBlockedPackage: String? = null
    private var lastBlockTime: Long = 0L
    private val blockCooldownMs = 1000L // Prevent rapid re-triggering

    // Vigilancia periodica del DNS (anti-manipulacion)
    private val handler = Handler(Looper.getMainLooper())
    private val enforceRunnable = object : Runnable {
        override fun run() {
            try {
                TamperGuard.enforce(applicationContext)
            } catch (e: Exception) {
                Log.w(TAG, "Error en vigilancia de DNS", e)
            }
            handler.postDelayed(this, Constants.Dns.ENFORCE_INTERVAL_MS)
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                    AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or
                    AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
            notificationTimeout = 100
        }

        serviceInfo = info
        // Arranca la vigilancia periodica del DNS
        handler.removeCallbacks(enforceRunnable)
        handler.post(enforceRunnable)
        Log.d(TAG, "Accessibility Service connected - monitoring ${blockedPackages.size} packages")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return

        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                handleWindowStateChange(event)
            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                handleContentChange(event)
            }
        }
    }

    private fun handleWindowStateChange(event: AccessibilityEvent) {
        val packageName = event.packageName?.toString() ?: return

        // Skip our own app and system UI
        if (packageName == "com.cleanshield.app" ||
            packageName == "com.cleanshield.app.debug" ||
            packageName == "com.cleanshield.app.tutor" ||
            packageName == "com.android.systemui" ||
            packageName == "com.android.launcher" ||
            packageName.startsWith("com.android.launcher")) {
            return
        }

        // Anti-manipulacion: proteger la pantalla de "DNS privado" de Ajustes
        if (packageName == "com.android.settings" || packageName.contains("settings")) {
            guardDnsSettingsScreen()
            return
        }

        if (isBlockedPackage(packageName)) {
            blockApp(packageName)
        }
    }

    /**
     * Si el candado de DNS esta activo y el usuario abre la pantalla de
     * "DNS privado", lo sacamos de ahi, reaplicamos el DNS y avisamos al tutor.
     */
    private fun guardDnsSettingsScreen() {
        val prefs = EncryptedPrefsManager(applicationContext)
        if (!prefs.isDnsLockEnabled) return

        val root = rootInActiveWindow ?: return
        val isDnsScreen = nodeContainsAnyText(root, DNS_SCREEN_KEYWORDS, 0)
        if (!isDnsScreen) return

        Log.d(TAG, "Pantalla de DNS privado detectada con candado activo -> bloqueando")
        performGlobalAction(GLOBAL_ACTION_BACK)
        TamperGuard.enforce(applicationContext)
        TamperGuard.alertGuardian(
            applicationContext,
            prefs,
            "CleanShield: se intentó abrir los ajustes de DNS privado en el móvil."
        )
    }

    /** Busca (recursivamente, con límite de profundidad) si algún nodo contiene alguno de los textos. */
    private fun nodeContainsAnyText(
        node: AccessibilityNodeInfo?,
        keywords: List<String>,
        depth: Int
    ): Boolean {
        if (node == null || depth > 12) return false
        val text = (node.text?.toString() ?: "") + " " + (node.contentDescription?.toString() ?: "")
        val lower = text.lowercase()
        if (keywords.any { lower.contains(it) }) return true
        for (i in 0 until node.childCount) {
            if (nodeContainsAnyText(node.getChild(i), keywords, depth + 1)) return true
        }
        return false
    }

    private fun handleContentChange(event: AccessibilityEvent) {
        val packageName = event.packageName?.toString() ?: return

        // Check if content contains blocked keywords (for browsers that aren't blocked)
        if (!isBlockedPackage(packageName)) {
            val text = extractTextFromEvent(event)
            if (containsBlockedContent(text)) {
                blockApp(packageName)
            }
        }
    }

    private fun isBlockedPackage(packageName: String): Boolean {
        return blockedPackages.contains(packageName)
    }

    private fun extractTextFromEvent(event: AccessibilityEvent): String {
        val builder = StringBuilder()
        event.text?.forEach { text ->
            builder.append(text).append(" ")
        }
        return builder.toString().lowercase()
    }

    private fun containsBlockedContent(text: String): Boolean {
        if (text.isBlank()) return false
        return Constants.ALL_BLOCKED_KEYWORDS.any { keyword ->
            text.contains(keyword.lowercase())
        }
    }

    private fun blockApp(packageName: String) {
        val currentTime = System.currentTimeMillis()

        // Cooldown to prevent rapid re-triggering
        if (packageName == lastBlockedPackage &&
            currentTime - lastBlockTime < blockCooldownMs) {
            return
        }

        lastBlockedPackage = packageName
        lastBlockTime = currentTime

        Log.d(TAG, "Blocking app: $packageName")

        // Launch BlockedActivity
        val intent = Intent(this, BlockedActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra(BlockedActivity.EXTRA_BLOCKED_PACKAGE, packageName)
            putExtra(BlockedActivity.EXTRA_BLOCKED_APP_NAME, getAppName(packageName))
        }

        startActivity(intent)

        // Also try to go home as fallback
        performGlobalAction(GLOBAL_ACTION_HOME)
    }

    private fun getAppName(packageName: String): String {
        return try {
            val pm = packageManager
            val appInfo = pm.getApplicationInfo(packageName, 0)
            pm.getApplicationLabel(appInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            packageName
        }
    }

    override fun onInterrupt() {
        Log.w(TAG, "Accessibility Service interrupted")
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(enforceRunnable)
        Log.d(TAG, "Accessibility Service destroyed")
    }

    companion object {
        private const val TAG = "CSAccessibility"

        // Textos que identifican la pantalla de "DNS privado" en Ajustes (varios idiomas/marcas)
        private val DNS_SCREEN_KEYWORDS = listOf(
            "dns privado",
            "private dns",
            "proveedor de dns",
            "private dns provider",
            "nombre de host del proveedor",
            "dns-over-tls"
        )
    }
}
