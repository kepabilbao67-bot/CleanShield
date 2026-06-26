package com.cleanshield.app.blocker

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.cleanshield.app.CleanShieldApp
import com.cleanshield.app.R
import com.cleanshield.app.ui.MainActivity
import com.cleanshield.app.ui.blocked.BlockedActivity
import com.cleanshield.app.utils.Constants
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Foreground service that monitors running apps using UsageStatsManager.
 * Detects blocked apps and launches BlockedActivity to prevent access.
 * Works alongside the Accessibility Service for redundant protection.
 */
class AppBlockerService : Service() {

    private val isRunning = AtomicBoolean(false)
    private var scheduler: ScheduledExecutorService? = null
    private var usageStatsManager: UsageStatsManager? = null

    private val blockedPackages: Set<String> by lazy {
        Constants.ALL_BLOCKED_PACKAGES.toHashSet()
    }

    private var lastBlockedPackage: String? = null
    private var lastBlockTime: Long = 0L
    private val blockCooldownMs = 2000L

    override fun onCreate() {
        super.onCreate()
        usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRunning.getAndSet(true)) {
            startForeground(CleanShieldApp.NOTIFICATION_ID_BLOCKER, createNotification())
            startMonitoring()
            Log.d(TAG, "App Blocker Service started")
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startMonitoring() {
        scheduler = Executors.newSingleThreadScheduledExecutor()
        scheduler?.scheduleAtFixedRate(
            { checkForegroundApp() },
            0L,
            POLL_INTERVAL_MS,
            TimeUnit.MILLISECONDS
        )
    }

    private fun checkForegroundApp() {
        try {
            val foregroundApp = getForegroundPackage()
            foregroundApp?.let { packageName ->
                if (shouldBlockApp(packageName)) {
                    blockApp(packageName)
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error checking foreground app", e)
        }
    }

    private fun getForegroundPackage(): String? {
        val usageStats = usageStatsManager ?: return null

        val currentTime = System.currentTimeMillis()
        val events = usageStats.queryEvents(currentTime - 5000, currentTime)
        val event = UsageEvents.Event()

        var lastPackage: String? = null
        var lastTimestamp = 0L

        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED ||
                event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                if (event.timeStamp > lastTimestamp) {
                    lastTimestamp = event.timeStamp
                    lastPackage = event.packageName
                }
            }
        }

        return lastPackage
    }

    private fun shouldBlockApp(packageName: String): Boolean {
        // Don't block ourselves or system UI
        if (packageName == "com.cleanshield.app" ||
            packageName == "com.android.systemui" ||
            packageName.startsWith("com.android.launcher")) {
            return false
        }

        return blockedPackages.contains(packageName)
    }

    private fun blockApp(packageName: String) {
        val currentTime = System.currentTimeMillis()

        // Cooldown to prevent spam
        if (packageName == lastBlockedPackage &&
            currentTime - lastBlockTime < blockCooldownMs) {
            return
        }

        lastBlockedPackage = packageName
        lastBlockTime = currentTime

        Log.d(TAG, "Blocking app: $packageName")

        val intent = Intent(this, BlockedActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(BlockedActivity.EXTRA_BLOCKED_PACKAGE, packageName)
        }
        startActivity(intent)
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CleanShieldApp.CHANNEL_BLOCKER)
            .setContentTitle("Bloqueador Activo")
            .setContentText("Monitoreando aplicaciones bloqueadas")
            .setSmallIcon(R.drawable.ic_shield)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    override fun onDestroy() {
        isRunning.set(false)
        scheduler?.shutdownNow()
        scheduler = null
        Log.d(TAG, "App Blocker Service stopped")
        super.onDestroy()
    }

    companion object {
        private const val TAG = "AppBlockerService"
        private const val POLL_INTERVAL_MS = 500L
    }
}
