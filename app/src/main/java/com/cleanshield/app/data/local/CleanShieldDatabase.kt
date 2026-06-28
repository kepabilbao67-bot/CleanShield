package com.cleanshield.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cleanshield.app.data.local.dao.ChallengeDao
import com.cleanshield.app.data.local.dao.JournalDao
import com.cleanshield.app.data.local.dao.TimeSafeDao
import com.cleanshield.app.data.local.entity.ChallengeEntity
import com.cleanshield.app.data.local.entity.JournalEntity
import com.cleanshield.app.data.local.entity.TimeSafeLetterEntity

@Database(
    entities = [
        JournalEntity::class,
        ChallengeEntity::class,
        TimeSafeLetterEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CleanShieldDatabase : RoomDatabase() {
    abstract fun journalDao(): JournalDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun timeSafeDao(): TimeSafeDao
}
