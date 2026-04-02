package com.neil.trantools.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neil.trantools.core.ui.R
import com.neil.trantools.data.chat.ChatSource
import com.neil.trantools.data.chat.ChatSourceType
import com.neil.trantools.ui.theme.TranToolsTheme

@Composable
fun ChatRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit = {},
    prefillQuestion: String? = null,
    onPrefillConsumed: () -> Unit = {},
    onOpenWikiArticle: (String) -> Unit = {},
    onOpenGemDetail: (String) -> Unit = {},
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(prefillQuestion) {
        val question = prefillQuestion ?: return@LaunchedEffect
        viewModel.applyPrefillQuestion(question)
        onPrefillConsumed()
    }
    ChatScreen(
        uiState = uiState,
        modifier = modifier,
        onBack = onBack,
        onOpenSettings = onOpenSettings,
        onInputChange = viewModel::setInput,
        onSend = viewModel::sendCurrentInput,
        onStopGenerating = viewModel::stopGenerating,
        onUseSuggestedQuestion = viewModel::sendSuggestedQuestion,
        onOpenWikiArticle = onOpenWikiArticle,
        onOpenGemDetail = onOpenGemDetail
    )
}

@Composable
fun ChatScreen(
    uiState: ChatUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onInputChange: (String) -> Unit = {},
    onSend: () -> Unit = {},
    onStopGenerating: () -> Unit = {},
    onUseSuggestedQuestion: (String) -> Unit = {},
    onOpenWikiArticle: (String) -> Unit = {},
    onOpenGemDetail: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = stringResource(R.string.action_back),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = stringResource(R.string.chat_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            IconButton(onClick = onOpenSettings) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = stringResource(R.string.action_settings),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        uiState.modelRuntimeHint?.takeIf { it.isNotBlank() }?.let { hint ->
            Text(
                text = hint,
                modifier = Modifier.padding(horizontal = 20.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.messages, key = { it.id }) { message ->
                ChatBubble(
                    message = message,
                    onUseSuggestedQuestion = onUseSuggestedQuestion,
                    onOpenWikiArticle = onOpenWikiArticle,
                    onOpenGemDetail = onOpenGemDetail
                )
            }
            if (uiState.streamingAnswer.isNotBlank()) {
                item {
                    ChatBubble(
                        message = ChatMessage(
                            id = -1,
                            role = ChatRole.Assistant,
                            text = uiState.streamingAnswer
                        ),
                        onUseSuggestedQuestion = onUseSuggestedQuestion,
                        onOpenWikiArticle = onOpenWikiArticle,
                        onOpenGemDetail = onOpenGemDetail
                    )
                }
            }
            if (uiState.isThinking) {
                item {
                    Text(
                        text = stringResource(R.string.chat_thinking),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = uiState.input,
                onValueChange = onInputChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text(stringResource(R.string.chat_placeholder)) },
                shape = RoundedCornerShape(20.dp)
            )
            IconButton(
                onClick = {
                    if (uiState.isThinking) {
                        onStopGenerating()
                    } else {
                        onSend()
                    }
                },
                modifier = Modifier
                    .size(52.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(
                    imageVector = if (uiState.isThinking) {
                        Icons.Outlined.Close
                    } else {
                        Icons.AutoMirrored.Outlined.Send
                    },
                    contentDescription = if (uiState.isThinking) {
                        stringResource(R.string.action_close)
                    } else {
                        stringResource(R.string.chat_send)
                    },
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
    onUseSuggestedQuestion: (String) -> Unit,
    onOpenWikiArticle: (String) -> Unit,
    onOpenGemDetail: (String) -> Unit,
) {
    val isUser = message.role == ChatRole.User
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(14.dp),
                color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
        if (message.sources.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.chat_sources),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge
            )
            message.sources.forEach { source ->
                SourceCard(
                    source = source,
                    onClick = {
                        when (source.type) {
                            ChatSourceType.Wiki -> onOpenWikiArticle(source.id)
                            ChatSourceType.Gem -> onOpenGemDetail(source.id)
                            ChatSourceType.Map -> onOpenGemDetail(source.id)
                            ChatSourceType.History -> Unit
                        }
                    }
                )
            }
        }
        if (!isUser && message.suggestedQuestions.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.chat_suggested_questions),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge
            )
            message.suggestedQuestions.forEach { suggested ->
                Card(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    onClick = { onUseSuggestedQuestion(suggested) },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Text(
                        text = suggested,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun SourceCard(
    source: ChatSource,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (source.type) {
                    ChatSourceType.Wiki -> Icons.Outlined.AutoStories
                    ChatSourceType.Gem -> Icons.Outlined.Explore
                    ChatSourceType.Map -> Icons.Outlined.Place
                    ChatSourceType.History -> Icons.AutoMirrored.Outlined.Send
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(source.title, fontWeight = FontWeight.Bold)
                Text(source.subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (source.detail.isNotBlank()) {
                    Text(
                        text = source.detail,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 850)
@Composable
private fun ChatScreenPreview() {
    TranToolsTheme {
        ChatScreen(
            uiState = ChatUiState(
                input = "",
                messages = listOf(
                    ChatMessage(1, ChatRole.Assistant, "Ask me about a landmark, food, or translation context."),
                    ChatMessage(2, ChatRole.User, "What should I know before visiting Kiyomizu-dera?")
                )
            )
        )
    }
}
