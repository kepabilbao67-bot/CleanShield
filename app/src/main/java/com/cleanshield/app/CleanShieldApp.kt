package com.cleanshield.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CleanShieldApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // VPN Protection Channel
        val vpnChannel = NotificationChannel(
            CHANNEL_VPN,
            "Protección VPN",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Notificaciones del servicio VPN de protección"
            setShowBadge(false)
        }

        // App Blocker Channel
        val blockerChannel = NotificationChannel(
            CHANNEL_BLOCKER,
            "Bloqueador de Apps",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Notificaciones del servicio de bloqueo de aplicaciones"
            setShowBadge(false)
        }

        // Streak Channel
        val streakChannel = NotificationChannel(
            CHANNEL_STREAK,
            "Racha de Días",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notificaciones de logros y racha de días limpios"
            setShowBadge(true)
            enableVibration(true)
        }

        // Motivation Channel
        val motivationChannel = NotificationChannel(
            CHANNEL_MOTIVATION,
            "Motivación",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Mensajes motivacionales diarios"
            setShowBadge(true)
        }

        // Danger Zones Channel
        val dangerZonesChannel = NotificationChannel(
            CHANNEL_DANGER_ZONES,
            "Zonas de Peligro",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Alertas de proximidad a zonas de riesgo"
            setShowBadge(true)
            enableVibration(true)
        }

        notificationManager.createNotificationChannels(
            listOf(vpnChannel, blockerChannel, streakChannel, motivationChannel, dangerZonesChannel)
        )
    }

    companion object {
        const val CHANNEL_VPN = "vpn_protection"
        const val CHANNEL_BLOCKER = "app_blocker"
        const val CHANNEL_STREAK = "streak_achievements"
        const val CHANNEL_MOTIVATION = "daily_motivation"

        const val NOTIFICATION_ID_VPN = 1001
        const val NOTIFICATION_ID_BLOCKER = 1002
        const val NOTIFICATION_ID_STREAK = 1003
        const val NOTIFICATION_ID_MOTIVATION = 1004
        const val CHANNEL_DANGER_ZONES = "danger_zones"
        const val NOTIFICATION_ID_DANGER_ZONES = 1005
    }
}
