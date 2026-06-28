package com.cleanshield.app.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.cleanshield.app.R
import com.google.android.gms.location.*

/**
 * Background service that monitors GPS location and alerts user
 * when approaching danger zones (casinos, betting shops, red light districts, etc.)
 */
class DangerZonesService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var isMonitoring = false

    companion object {
        const val CHANNEL_ID = "danger_zones_channel"
        const val NOTIFICATION_ID = 2001
        const val ALERT_NOTIFICATION_ID = 2002
        const val ALERT_RADIUS_METERS = 500.0

        // Known danger zone types for Places API query
        val DANGER_ZONE_TYPES = listOf(
            "casino",
            "night_club",
            "liquor_store"
        )

        val MOTIVATIONAL_ALERTS = listOf(
            "⚠️ Estás cerca de una zona de riesgo. Recuerda por qué empezaste este camino.",
            "🛡️ Zona peligrosa detectada. Tú eres más fuerte que este impulso.",
            "💪 Alerta: zona de riesgo cerca. Piensa en tu familia, en tu progreso.",
            "🔥 Estás cerca de tentación. Cada paso que das en otra dirección es una victoria.",
            "⚡ Zona de peligro cercana. Llama a tu contacto de confianza AHORA."
        )
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
        setupLocationCallback()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createForegroundNotification())
        startLocationMonitoring()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        stopLocationMonitoring()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Zonas de Peligro",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Monitoreo de ubicación para zonas de riesgo"
        }
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun createForegroundNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Protección GPS Activa")
            .setContentText("Monitoreando zonas de peligro")
            .setSmallIcon(R.drawable.ic_shield)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    checkDangerZones(location)
                }
            }
        }
    }

    private fun startLocationMonitoring() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            60000 // Check every 60 seconds
        ).apply {
            setMinUpdateDistanceMeters(100f) // Only update if moved 100m
        }.build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        isMonitoring = true
    }

    private fun stopLocationMonitoring() {
        if (isMonitoring) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            isMonitoring = false
        }
    }

    private fun checkDangerZones(location: Location) {
        // In a production app, this would query Google Places API
        // or use a local database of known danger zone coordinates
        // For now, we simulate the check with a notification system ready

        // Example: If near a known location, send alert
        // This would be replaced with actual Places API integration
    }

    private fun sendDangerAlert() {
        val message = MOTIVATIONAL_ALERTS.random()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("⚠️ Zona de Peligro Cerca")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_shield)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(ALERT_NOTIFICATION_ID, notification)
    }
}
