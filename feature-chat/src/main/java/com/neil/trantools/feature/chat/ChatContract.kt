package com.neil.trantools.feature.chat

import com.neil.trantools.data.chat.ChatSource

enum class ChatRole {
    User,
    Assistant,
}

data class ChatMessage(
    val id: Long,
    val role: ChatRole,
    val text: String,
    val sources: List<ChatSource> = emptyList(),
    val suggestedQuestions: List<String> = emptyList(),
)

data class ChatUiState(
    val input: String = "",
    val messages: List<ChatMessage> = emptyList(),
    val isThinking: Boolean = false,
    val streamingAnswer: String = "",
    val modelRuntimeHint: String? = null,
)
