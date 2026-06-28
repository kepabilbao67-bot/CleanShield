package com.cleanshield.app.data.local.dao

import androidx.room.*
import com.cleanshield.app.data.local.entity.ChallengeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM daily_challenges WHERE date = :date LIMIT 1")
    suspend fun getChallengeForDate(date: String): ChallengeEntity?

    @Query("SELECT * FROM daily_challenges ORDER BY date DESC")
    fun getAllChallenges(): Flow<List<ChallengeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenge(challenge: ChallengeEntity)

    @Query("UPDATE daily_challenges SET isCompleted = 1, completedAt = :completedAt WHERE id = :id")
    suspend fun markCompleted(id: Long, completedAt: String)

    @Query("SELECT COUNT(*) FROM daily_challenges WHERE isCompleted = 1")
    suspend fun getCompletedCount(): Int
}
