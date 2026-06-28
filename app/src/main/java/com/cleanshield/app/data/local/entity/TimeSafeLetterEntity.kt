package com.cleanshield.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "time_safe_letters")
data class TimeSafeLetterEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val unlockAtDays: Int, // 30, 60, or 90
    val createdAt: String,
    val isUnlocked: Boolean = false,
    val unlockedAt: String? = null
)
