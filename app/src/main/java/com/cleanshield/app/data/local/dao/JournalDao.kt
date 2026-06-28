package com.cleanshield.app.data.local.dao

import androidx.room.*
import com.cleanshield.app.data.local.entity.JournalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY createdAt DESC")
    fun getAllEntries(): Flow<List<JournalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntity)

    @Delete
    suspend fun deleteEntry(entry: JournalEntity)

    @Query("SELECT COUNT(*) FROM journal_entries")
    suspend fun getEntryCount(): Int
}
