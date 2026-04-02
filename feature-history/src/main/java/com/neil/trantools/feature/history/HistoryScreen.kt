package com.neil.trantools.feature.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neil.trantools.core.ui.R
import com.neil.trantools.data.history.HistoryMode
import com.neil.trantools.data.history.TranslationHistoryEntity
import com.neil.trantools.feature.translate.displayLabel
import com.neil.trantools.feature.translate.translateLanguageFromStored
import com.neil.trantools.ui.theme.TranToolsTheme

@Composable
fun HistoryRoute(
    onBack: () -> Unit,
    onUseItem: (TranslationHistoryEntity) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HistoryScreen(
        uiState = uiState,
        onBack = onBack,
        onUseItem = onUseItem,
        onFilterSelect = viewModel::setFilter,
        onClearAll = viewModel::clearAll,
        modifier = modifier
    )
}

@Composable
fun HistoryScreen(
    uiState: HistoryUiState,
    onBack: () -> Unit,
    onUseItem: (TranslationHistoryEntity) -> Unit,
    onFilterSelect: (HistoryFilter) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val items = uiState.filteredItems()
    val grouped = items.groupBy { epochDayLabel(it.createdAtEpochMs) }
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
                    text = stringResource(R.string.history_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            if (uiState.items.isNotEmpty()) {
                TextButton(onClick = onClearAll) {
                    Text(stringResource(R.string.history_clear_all))
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = uiState.filter == HistoryFilter.All,
                onClick = { onFilterSelect(HistoryFilter.All) },
                label = { Text(stringResource(R.string.history_filter_all)) }
            )
            FilterChip(
                selected = uiState.filter == HistoryFilter.Text,
                onClick = { onFilterSelect(HistoryFilter.Text) },
                label = { Text(stringResource(R.string.history_filter_text)) }
            )
            FilterChip(
                selected = uiState.filter == HistoryFilter.Ocr,
                onClick = { onFilterSelect(HistoryFilter.Ocr) },
                label = { Text(stringResource(R.string.history_filter_ocr)) }
            )
            FilterChip(
                selected = uiState.filter == HistoryFilter.Voice,
                onClick = { onFilterSelect(HistoryFilter.Voice) },
                label = { Text(stringResource(R.string.history_filter_voice)) }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (items.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.history_empty),
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                grouped.forEach { (day, dayItems) ->
                    item(key = "day_$day") {
                        Text(
                            text = day,
                            modifier = Modifier.padding(horizontal = 20.dp),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(dayItems, key = { it.id }) { item ->
                        HistoryItemCard(
                            item = item,
                            context = context,
                            onClick = { onUseItem(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryItemCard(
    item: TranslationHistoryEntity,
    context: android.content.Context,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = when (item.mode) {
                    HistoryMode.TEXT -> context.getString(R.string.history_mode_text)
                    HistoryMode.OCR -> context.getString(R.string.history_mode_ocr)
                    HistoryMode.VOICE -> context.getString(R.string.history_mode_voice)
                },
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${translateLanguageFromStored(item.sourceLanguage).displayLabel(context)} -> ${translateLanguageFromStored(item.targetLanguage).displayLabel(context)}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium
            )
            Text(item.sourceText, style = MaterialTheme.typography.bodyMedium)
            Text(
                item.translatedText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryScreenPreview() {
    TranToolsTheme {
        HistoryScreen(
            uiState = HistoryUiState(
                items = listOf(
                    TranslationHistoryEntity(
                        id = 1,
                        mode = HistoryMode.OCR,
                        sourceLanguage = "Japanese",
                        targetLanguage = "English",
                        sourceText = "焼き鮭",
                        translatedText = "Grilled salmon"
                    ),
                    TranslationHistoryEntity(
                        id = 2,
                        mode = HistoryMode.VOICE,
                        sourceLanguage = "English",
                        targetLanguage = "Japanese",
                        sourceText = "Where is the nearest station?",
                        translatedText = "最寄り駅はどこですか？"
                    )
                )
            ),
            onBack = {},
            onUseItem = {},
            onFilterSelect = {},
            onClearAll = {}
        )
    }
}

private fun epochDayLabel(epochMs: Long): String {
    val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    return formatter.format(java.util.Date(epochMs))
}
