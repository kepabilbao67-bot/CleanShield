package com.cleanshield.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val mood: String,
    val date: String,
    val createdAt: String
)
