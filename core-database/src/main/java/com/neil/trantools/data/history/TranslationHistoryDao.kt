package com.neil.trantools.data.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TranslationHistoryEntity)

    @Query("SELECT * FROM translation_history ORDER BY createdAtEpochMs DESC")
    fun observeAll(): Flow<List<TranslationHistoryEntity>>

    @Query("SELECT * FROM translation_history ORDER BY createdAtEpochMs DESC LIMIT :limit")
    suspend fun getRecent(limit: Int): List<TranslationHistoryEntity>

    @Query("DELETE FROM translation_history")
    suspend fun clearAll()
}
