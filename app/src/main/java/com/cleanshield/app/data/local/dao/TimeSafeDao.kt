package com.cleanshield.app.data.local.dao

import androidx.room.*
import com.cleanshield.app.data.local.entity.TimeSafeLetterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeSafeDao {
    @Query("SELECT * FROM time_safe_letters ORDER BY createdAt DESC")
    fun getAllLetters(): Flow<List<TimeSafeLetterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLetter(letter: TimeSafeLetterEntity)

    @Query("UPDATE time_safe_letters SET isUnlocked = 1, unlockedAt = :unlockedAt WHERE id = :id")
    suspend fun unlockLetter(id: Long, unlockedAt: String)

    @Query("SELECT * FROM time_safe_letters WHERE isUnlocked = 0")
    fun getLockedLetters(): Flow<List<TimeSafeLetterEntity>>

    @Query("SELECT * FROM time_safe_letters WHERE isUnlocked = 1")
    fun getUnlockedLetters(): Flow<List<TimeSafeLetterEntity>>
}
