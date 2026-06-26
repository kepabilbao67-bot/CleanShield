package com.cleanshield.app.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.cleanshield.app.ui.blocked.BlockedActivity
import com.cleanshield.app.utils.Constants

class CleanShieldAccessibilityService : AccessibilityService() {

    private val blockedPackages: Set<String> by lazy {
        Constants.ALL_BLOCKED_PACKAGES.toHashSet()
    }

    private var lastBlockedPackage: String? = null
    private var lastBlockTime: Long = 0L
    private val blockCooldownMs = 1000L // Prevent rapid re-triggering

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
            packageName == "com.android.systemui" ||
            packageName == "com.android.launcher" ||
            packageName.startsWith("com.android.launcher")) {
            return
        }

        if (isBlockedPackage(packageName)) {
            blockApp(packageName)
        }
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
        Log.d(TAG, "Accessibility Service destroyed")
    }

    companion object {
        private const val TAG = "CSAccessibility"
    }
}
