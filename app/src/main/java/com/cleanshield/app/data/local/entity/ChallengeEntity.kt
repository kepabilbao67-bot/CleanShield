package com.cleanshield.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_challenges")
data class ChallengeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val category: String,
    val date: String,
    val isCompleted: Boolean = false,
    val completedAt: String? = null
)
