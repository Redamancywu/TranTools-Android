package com.neil.trantools.data.chat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_message")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val role: String,
    val text: String,
    val sourcesJson: String = "[]",
    val createdAtEpochMs: Long = System.currentTimeMillis(),
)
