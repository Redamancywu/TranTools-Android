package com.neil.trantools.data.chat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: ChatMessageEntity): Long

    @Query("SELECT * FROM chat_message ORDER BY createdAtEpochMs ASC")
    fun observeAll(): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_message ORDER BY createdAtEpochMs ASC")
    suspend fun getAll(): List<ChatMessageEntity>

    @Query("DELETE FROM chat_message")
    suspend fun clearAll()
}
