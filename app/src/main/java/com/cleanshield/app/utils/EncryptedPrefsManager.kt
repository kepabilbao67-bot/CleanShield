package com.cleanshield.app.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedPrefsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val prefs: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            Constants.Prefs.FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // ═══════════════════════════════════════════════════════════════
    // PASSWORD MANAGEMENT (SHA-256 hashing)
    // ═══════════════════════════════════════════════════════════════

    fun setPassword(password: String) {
        val hash = hashPassword(password)
        prefs.edit().putString(Constants.Prefs.KEY_PASSWORD_HASH, hash).apply()
    }

    fun verifyPassword(password: String): Boolean {
        val storedHash = prefs.getString(Constants.Prefs.KEY_PASSWORD_HASH, null)
            ?: return false
        return hashPassword(password) == storedHash
    }

    fun hasPassword(): Boolean {
        return prefs.getString(Constants.Prefs.KEY_PASSWORD_HASH, null) != null
    }

    private fun hashPassword(password: String): String {
        val salt = "CleanShield_Salt_2024_Secure"
        val input = "$salt$password$salt"
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))
        // Double hash for added security
        val doubleHash = digest.digest(hashBytes)
        return doubleHash.joinToString("") { "%02x".format(it) }
    }

    // ═══════════════════════════════════════════════════════════════
    // VPN STATE
    // ═══════════════════════════════════════════════════════════════

    var isVpnEnabled: Boolean
        get() = prefs.getBoolean(Constants.Prefs.KEY_VPN_ENABLED, true)
        set(value) = prefs.edit().putBoolean(Constants.Prefs.KEY_VPN_ENABLED, value).apply()

    // ═══════════════════════════════════════════════════════════════
    // APP BLOCKER STATE
    // ═══════════════════════════════════════════════════════════════

    var isAppBlockerEnabled: Boolean
        get() = prefs.getBoolean(Constants.Prefs.KEY_APP_BLOCKER_ENABLED, true)
        set(value) = prefs.edit().putBoolean(Constants.Prefs.KEY_APP_BLOCKER_ENABLED, value).apply()

    // ═══════════════════════════════════════════════════════════════
    // NIGHT MODE
    // ═══════════════════════════════════════════════════════════════

    var isNightModeEnabled: Boolean
        get() = prefs.getBoolean(Constants.Prefs.KEY_NIGHT_MODE, false)
        set(value) = prefs.edit().putBoolean(Constants.Prefs.KEY_NIGHT_MODE, value).apply()

    // ═══════════════════════════════════════════════════════════════
    // STREAK TRACKING
    // ═══════════════════════════════════════════════════════════════

    var streakStartDate: Long
        get() = prefs.getLong(Constants.Prefs.KEY_STREAK_START, System.currentTimeMillis())
        set(value) = prefs.edit().putLong(Constants.Prefs.KEY_STREAK_START, value).apply()

    var totalBlockedCount: Int
        get() = prefs.getInt(Constants.Prefs.KEY_TOTAL_BLOCKED, 0)
        set(value) = prefs.edit().putInt(Constants.Prefs.KEY_TOTAL_BLOCKED, value).apply()

    fun incrementBlockedCount() {
        totalBlockedCount = totalBlockedCount + 1
    }

    // ═══════════════════════════════════════════════════════════════
    // ONBOARDING
    // ═══════════════════════════════════════════════════════════════

    var isFirstLaunch: Boolean
        get() = prefs.getBoolean(Constants.Prefs.KEY_FIRST_LAUNCH, true)
        set(value) = prefs.edit().putBoolean(Constants.Prefs.KEY_FIRST_LAUNCH, value).apply()

    var isOnboardingComplete: Boolean
        get() = prefs.getBoolean(Constants.Prefs.KEY_ONBOARDING_COMPLETE, false)
        set(value) = prefs.edit().putBoolean(Constants.Prefs.KEY_ONBOARDING_COMPLETE, value).apply()

    // ═══════════════════════════════════════════════════════════════
    // EMERGENCY CONTACT
    // ═══════════════════════════════════════════════════════════════

    var emergencyContact: String?
        get() = prefs.getString(Constants.Prefs.KEY_EMERGENCY_CONTACT, null)
        set(value) = prefs.edit().putString(Constants.Prefs.KEY_EMERGENCY_CONTACT, value).apply()

    // ═══════════════════════════════════════════════════════════════
    // RELAPSE TRACKING
    // ═══════════════════════════════════════════════════════════════

    var lastRelapseDate: Long
        get() = prefs.getLong(Constants.Prefs.KEY_LAST_RELAPSE, 0L)
        set(value) = prefs.edit().putLong(Constants.Prefs.KEY_LAST_RELAPSE, value).apply()

    fun resetStreak() {
        lastRelapseDate = System.currentTimeMillis()
        streakStartDate = System.currentTimeMillis()
    }

    fun getStreakDays(): Int {
        val start = streakStartDate
        val now = System.currentTimeMillis()
        val diffMs = now - start
        return (diffMs / (1000 * 60 * 60 * 24)).toInt()
    }
}
