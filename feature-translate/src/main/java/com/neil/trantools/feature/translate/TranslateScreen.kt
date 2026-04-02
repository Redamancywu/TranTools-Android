package com.neil.trantools.feature.translate

import android.Manifest
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview as CameraPreviewUseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview as ComposePreview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neil.trantools.core.ui.R
import com.neil.trantools.data.history.TranslationHistoryEntity
import com.neil.trantools.data.translation.FavoritePhrase
import com.neil.trantools.data.translation.TranslationModelStore
import com.neil.trantools.data.translation.TranslationPackStatus
import com.neil.trantools.data.translation.matches
import com.neil.trantools.ui.components.VoyagerTopBar
import com.neil.trantools.ui.theme.TranToolsTheme
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun TranslateRoute(
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit = {},
    onOpenHistory: () -> Unit = {},
    prefillHistoryItem: TranslationHistoryEntity? = null,
    onPrefillConsumed: () -> Unit = {},
    viewModel: TranslateViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val mainExecutor = remember(context) { ContextCompat.getMainExecutor(context) }
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val translationPackStatuses by remember { TranslationModelStore.observeStatuses() }
        .collectAsStateWithLifecycle()
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onCameraPermissionChanged(granted)
    }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            viewModel.processImportedImage(uri)
        }
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        viewModel.onCameraPermissionChanged(granted)
        TranslationModelStore.refresh()
        if (!granted) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(prefillHistoryItem?.id) {
        val history = prefillHistoryItem ?: return@LaunchedEffect
        viewModel.applyHistoryItem(history)
        onPrefillConsumed()
    }

    DisposableEffect(context) {
        val textToSpeech = TextToSpeech(context) { status ->
            ttsReady = status == TextToSpeech.SUCCESS
        }
        tts = textToSpeech
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    val effectiveSourceLanguage = if (uiState.inputMode == TranslateInputMode.Text) {
        uiState.effectiveTextSourceLanguage()
    } else {
        uiState.sourceLanguage.takeUnless { it == TranslateLanguageOption.Auto }
    }
    val sourcePackStatus = effectiveSourceLanguage?.let { language ->
        translationPackStatuses.firstOrNull { it.matches(language) }
    }
    val targetPackStatus = translationPackStatuses.firstOrNull { it.matches(uiState.targetLanguage) }

    TranslateScreen(
        uiState = uiState,
        imageCapture = imageCapture,
        sourcePackStatus = sourcePackStatus,
        targetPackStatus = targetPackStatus,
        onCapture = {
            viewModel.captureAndRecognize(
                imageCapture = imageCapture,
                executor = mainExecutor
            )
        },
        onOpenSettings = onOpenSettings,
        onOpenHistory = onOpenHistory,
        onSwapLanguages = viewModel::swapLanguages,
        onSelectSourceLanguage = viewModel::setSourceLanguage,
        onSelectTargetLanguage = viewModel::setTargetLanguage,
        onSelectInputMode = viewModel::setInputMode,
        onTextInputChange = viewModel::setTextInput,
        onTranslateText = viewModel::translateText,
        onSpeakTextTranslation = {
            val translated = uiState.textTranslation.trim()
            if (translated.isBlank()) return@TranslateScreen
            val targetLocale = localeForLanguage(uiState.targetLanguage)
            tts?.language = targetLocale
            tts?.speak(translated, TextToSpeech.QUEUE_FLUSH, null, "translate_text_tts")
        },
        isTextSpeechReady = ttsReady,
        onRequestCameraPermission = { permissionLauncher.launch(Manifest.permission.CAMERA) },
        onClearResult = viewModel::clearRecognizedResult,
        onClearText = viewModel::clearTextResult,
        onToggleFavoritePhrase = viewModel::toggleCurrentFavorite,
        onApplyPhrase = viewModel::applyPhrase,
        onImportPhoto = { imagePickerLauncher.launch("image/*") },
        onShareResult = {
            val payload = buildString {
                if (uiState.recognizedLines.isNotEmpty()) {
                    append(uiState.recognizedLines.joinToString("\n"))
                    append("\n\n")
                }
                if (uiState.translatedLines.isNotEmpty()) {
                    append(uiState.translatedLines.joinToString("\n"))
                } else {
                    append(uiState.textTranslation)
                }
            }.trim()
            if (payload.isNotBlank()) {
                context.startActivity(
                    Intent.createChooser(
                        Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.translate_share_subject))
                            putExtra(Intent.EXTRA_TEXT, payload)
                        },
                        null
                    )
                )
            }
        },
        onDownloadMissingPacks = {
            scope.launch {
                listOf(uiState.sourceLanguage, uiState.targetLanguage)
                    .flatMap { language ->
                        when (language) {
                            TranslateLanguageOption.Auto -> listOfNotNull(uiState.effectiveTextSourceLanguage())
                            else -> listOf(language)
                        }
                    }
                    .distinct()
                    .forEach { language ->
                        val status = translationPackStatuses.firstOrNull { it.language == language }
                        if (status?.isDownloaded != true) {
                            TranslationModelStore.download(language)
                        }
                    }
                TranslationModelStore.refresh()
            }
        },
        modifier = modifier
    )
}

@Composable
fun TranslateScreen(
    uiState: TranslateUiState,
    imageCapture: ImageCapture,
    sourcePackStatus: TranslationPackStatus?,
    targetPackStatus: TranslationPackStatus?,
    onCapture: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenHistory: () -> Unit,
    onSwapLanguages: () -> Unit,
    onSelectSourceLanguage: (TranslateLanguageOption) -> Unit,
    onSelectTargetLanguage: (TranslateLanguageOption) -> Unit,
    onSelectInputMode: (TranslateInputMode) -> Unit,
    onTextInputChange: (String) -> Unit,
    onTranslateText: () -> Unit,
    onSpeakTextTranslation: () -> Unit,
    isTextSpeechReady: Boolean,
    onRequestCameraPermission: () -> Unit,
    onClearResult: () -> Unit,
    onClearText: () -> Unit,
    onToggleFavoritePhrase: () -> Unit,
    onApplyPhrase: (FavoritePhrase) -> Unit,
    onImportPhoto: () -> Unit,
    onShareResult: () -> Unit,
    onDownloadMissingPacks: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboard = LocalClipboard.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val cameraBackground = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.88f)
    val cameraFrame = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
    var showSourceDialog by remember { mutableStateOf(false) }
    var showTargetDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                if (uiState.inputMode == TranslateInputMode.Camera) {
                    cameraBackground
                } else {
                    MaterialTheme.colorScheme.background
                }
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            VoyagerTopBar(
                modifier = Modifier.background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                            Color.Transparent
                        )
                    )
                ),
                onSettingsClick = onOpenSettings
            )

            ModeSwitcher(
                selectedMode = uiState.inputMode,
                onSelectMode = onSelectInputMode,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            LanguageBar(
                uiState = uiState,
                onSwapLanguages = onSwapLanguages,
                onOpenSourceDialog = { showSourceDialog = true },
                onOpenTargetDialog = { showTargetDialog = true },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )

            when (uiState.inputMode) {
                TranslateInputMode.Text -> {
                    TextTranslatePane(
                        uiState = uiState,
                        sourcePackStatus = sourcePackStatus,
                        targetPackStatus = targetPackStatus,
                        onTextInputChange = onTextInputChange,
                        onTranslateText = onTranslateText,
                        onSpeakTextTranslation = onSpeakTextTranslation,
                        isTextSpeechReady = isTextSpeechReady,
                        onClearText = onClearText,
                        onToggleFavoritePhrase = onToggleFavoritePhrase,
                        onApplyPhrase = onApplyPhrase,
                        onCopyTranslation = {
                            if (uiState.textTranslation.isNotBlank()) {
                                scope.launch {
                                    clipboard.setClipEntry(
                                        ClipData
                                            .newPlainText("translation", uiState.textTranslation)
                                            .toClipEntry()
                                    )
                                }
                            }
                        },
                        onDownloadMissingPacks = onDownloadMissingPacks,
                        modifier = Modifier.weight(1f)
                    )
                }

                TranslateInputMode.Camera -> {
                    CameraTranslatePane(
                        uiState = uiState,
                        imageCapture = imageCapture,
                        onCapture = onCapture,
                        onImportPhoto = onImportPhoto,
                        onShareResult = onShareResult,
                        onRequestCameraPermission = onRequestCameraPermission,
                        onClearResult = onClearResult,
                        cameraFrame = cameraFrame,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            RoundButton(
                icon = Icons.Outlined.History,
                container = MaterialTheme.colorScheme.primary,
                tint = MaterialTheme.colorScheme.onPrimary,
                onClick = onOpenHistory
            )
            RoundButton(
                icon = Icons.Outlined.AutoAwesome,
                container = MaterialTheme.colorScheme.secondaryContainer,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }

    if (showSourceDialog) {
        LanguageSelectionDialog(
            title = stringResource(R.string.translate_select_source),
            selected = uiState.sourceLanguage,
            options = if (uiState.inputMode == TranslateInputMode.Camera) {
                TranslateLanguageOption.entries
            } else {
                TranslateLanguageOption.entries
            },
            onDismiss = { showSourceDialog = false },
            onSelect = { option ->
                onSelectSourceLanguage(option)
                showSourceDialog = false
            }
        )
    }

    if (showTargetDialog) {
        LanguageSelectionDialog(
            title = stringResource(R.string.translate_select_target),
            selected = uiState.targetLanguage,
            options = supportedTranslateLanguages,
            onDismiss = { showTargetDialog = false },
            onSelect = { option ->
                onSelectTargetLanguage(option)
                showTargetDialog = false
            }
        )
    }
}

@Composable
private fun ModeSwitcher(
    selectedMode: TranslateInputMode,
    onSelectMode: (TranslateInputMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FilterChip(
            selected = selectedMode == TranslateInputMode.Text,
            onClick = { onSelectMode(TranslateInputMode.Text) },
            label = { Text(stringResource(R.string.translate_mode_text)) }
        )
        FilterChip(
            selected = selectedMode == TranslateInputMode.Camera,
            onClick = { onSelectMode(TranslateInputMode.Camera) },
            label = { Text(stringResource(R.string.translate_mode_camera)) }
        )
    }
}

@Composable
private fun LanguageBar(
    uiState: TranslateUiState,
    onSwapLanguages: () -> Unit,
    onOpenSourceDialog: () -> Unit,
    onOpenTargetDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(999.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (uiState.inputMode == TranslateInputMode.Camera) {
                MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
            } else {
                MaterialTheme.colorScheme.surfaceContainerLow
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = uiState.sourceLanguage.displayLabel(context),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable { onOpenSourceDialog() }
            )
            Icon(
                imageVector = Icons.Outlined.SwapHoriz,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onSwapLanguages() }
            )
            Text(
                text = uiState.targetLanguage.displayLabel(context),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onOpenTargetDialog() }
            )
        }
    }
}

@Composable
private fun TextTranslatePane(
    uiState: TranslateUiState,
    sourcePackStatus: TranslationPackStatus?,
    targetPackStatus: TranslationPackStatus?,
    onTextInputChange: (String) -> Unit,
    onTranslateText: () -> Unit,
    onSpeakTextTranslation: () -> Unit,
    isTextSpeechReady: Boolean,
    onClearText: () -> Unit,
    onToggleFavoritePhrase: () -> Unit,
    onApplyPhrase: (FavoritePhrase) -> Unit,
    onCopyTranslation: () -> Unit,
    onDownloadMissingPacks: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val missingPacks = listOfNotNull(sourcePackStatus, targetPackStatus)
        .filterNot { it.isDownloaded }
        .distinctBy { it.language }
    val isCurrentFavorite = uiState.favoritePhrases.any {
        it.sourceText == uiState.textInput.trim() && it.translatedText == uiState.textTranslation.trim()
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, top = 0.dp, end = 20.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = stringResource(R.string.translate_text_headline),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = stringResource(R.string.translate_text_subtitle),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (uiState.sourceLanguage == TranslateLanguageOption.Auto && uiState.detectedTextSourceLanguage != null) {
                    Text(
                        text = stringResource(
                            R.string.translate_detected_source,
                            uiState.detectedTextSourceLanguage.displayLabel(context)
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        item {
            CurrentPackStatusCard(
                sourcePackStatus = sourcePackStatus,
                targetPackStatus = targetPackStatus,
                onDownloadMissingPacks = onDownloadMissingPacks
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.textInput,
                        onValueChange = onTextInputChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 140.dp),
                        placeholder = { Text(stringResource(R.string.translate_text_placeholder)) },
                        shape = RoundedCornerShape(18.dp)
                    )
                    if (uiState.errorMessage != null && uiState.textInput.isNotBlank()) {
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = onTranslateText,
                            enabled = uiState.textInput.isNotBlank() && !uiState.isProcessing
                        ) {
                            Icon(Icons.Outlined.Translate, contentDescription = null)
                            Text(
                                text = if (uiState.isProcessing) {
                                    " ${stringResource(R.string.translate_translating)}"
                                } else {
                                    " ${stringResource(R.string.action_translate)}"
                                }
                            )
                        }
                        FilledTonalButton(
                            onClick = onClearText,
                            enabled = uiState.textInput.isNotBlank() || uiState.textTranslation.isNotBlank()
                        ) {
                            Text(stringResource(R.string.action_clear))
                        }
                        FilledTonalButton(
                            onClick = onToggleFavoritePhrase,
                            enabled = uiState.textTranslation.isNotBlank()
                        ) {
                            Icon(
                                imageVector = if (isCurrentFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = null
                            )
                            Text(
                                text = if (isCurrentFavorite) {
                                    " ${stringResource(R.string.translate_saved_phrase)}"
                                } else {
                                    " ${stringResource(R.string.translate_favorite_phrase)}"
                                }
                            )
                        }
                    }
                    if (uiState.isProcessing) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        item {
            if (uiState.commonPhrases.isNotEmpty()) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.translate_common_phrases_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        uiState.commonPhrases.take(4).forEach { phrase ->
                            PhraseCard(phrase = phrase, onClick = { onApplyPhrase(phrase) })
                        }
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLowest)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.translate_translation_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(
                            onClick = onCopyTranslation,
                            enabled = uiState.textTranslation.isNotBlank()
                        ) {
                            Icon(Icons.Outlined.ContentCopy, contentDescription = null)
                            Text(stringResource(R.string.action_copy), modifier = Modifier.padding(start = 4.dp))
                        }
                        TextButton(
                            onClick = onSpeakTextTranslation,
                            enabled = uiState.textTranslation.isNotBlank() && isTextSpeechReady
                        ) {
                            Icon(Icons.Outlined.GraphicEq, contentDescription = null)
                            Text(stringResource(R.string.action_speak), modifier = Modifier.padding(start = 4.dp))
                        }
                    }

                    if (uiState.textTranslation.isBlank()) {
                        Text(
                            text = if (missingPacks.isEmpty()) {
                                stringResource(R.string.translate_translation_empty)
                            } else {
                                stringResource(R.string.translate_translation_missing_pack)
                            },
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = uiState.textTranslation,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.translate_phrasebook_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (uiState.favoritePhrases.isEmpty()) {
                        Text(
                            text = stringResource(R.string.translate_phrasebook_empty),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        uiState.favoritePhrases.take(5).forEach { phrase ->
                            PhraseCard(phrase = phrase, onClick = { onApplyPhrase(phrase) })
                        }
                    }
                }
            }
        }
    }
}

private fun localeForLanguage(language: TranslateLanguageOption): Locale {
    return when (language) {
        TranslateLanguageOption.Auto -> Locale.getDefault()
        TranslateLanguageOption.English -> Locale.ENGLISH
        TranslateLanguageOption.Japanese -> Locale.JAPANESE
        TranslateLanguageOption.Chinese -> Locale.SIMPLIFIED_CHINESE
        TranslateLanguageOption.Korean -> Locale.KOREAN
        TranslateLanguageOption.Spanish -> Locale.forLanguageTag("es-ES")
    }
}

@Composable
private fun CurrentPackStatusCard(
    sourcePackStatus: TranslationPackStatus?,
    targetPackStatus: TranslationPackStatus?,
    onDownloadMissingPacks: () -> Unit,
) {
    val context = LocalContext.current
    val statuses = listOfNotNull(sourcePackStatus, targetPackStatus).distinctBy { it.language }
    val allReady = statuses.isNotEmpty() && statuses.all { it.isDownloaded }
    val busy = statuses.any { it.isBusy }
    val missingLabel = statuses.filterNot { it.isDownloaded }.joinToString { it.language.displayLabel(context) }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            if (allReady) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (allReady) stringResource(R.string.translate_pack_ready) else stringResource(R.string.translate_pack_status),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = when {
                    statuses.isEmpty() -> stringResource(R.string.translate_pack_choose_languages)
                    busy -> stringResource(R.string.translate_pack_downloading)
                    allReady -> statuses.joinToString { it.language.displayLabel(context) }
                    else -> stringResource(R.string.translate_pack_missing, missingLabel)
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (busy) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp
                )
            } else if (!allReady && statuses.isNotEmpty()) {
                FilledTonalButton(onClick = onDownloadMissingPacks) {
                    Icon(Icons.Outlined.Download, contentDescription = null)
                    Text(stringResource(R.string.translate_download_missing), modifier = Modifier.padding(start = 4.dp))
                }
            }
        }
    }
}

@Composable
private fun CameraTranslatePane(
    uiState: TranslateUiState,
    imageCapture: ImageCapture,
    onCapture: () -> Unit,
    onImportPhoto: () -> Unit,
    onShareResult: () -> Unit,
    onRequestCameraPermission: () -> Unit,
    onClearResult: () -> Unit,
    cameraFrame: Color,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(cameraFrame)
                .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(28.dp))
        ) {
            if (uiState.hasCameraPermission) {
                CameraPreview(
                    imageCapture = imageCapture,
                    modifier = Modifier.fillMaxSize()
                )

                OcrOverlay(
                    regions = uiState.recognizedRegions,
                    translatedLines = uiState.translatedLines,
                    fallbackLines = uiState.recognizedLines,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                PermissionContent(
                    onRequestCameraPermission = onRequestCameraPermission,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TranslateLabel(stringResource(R.string.translate_camera_tip))
                if (uiState.isProcessing) {
                    TranslateLabel(stringResource(R.string.translate_camera_processing))
                } else if (uiState.recognizedLines.isNotEmpty()) {
                    TranslateLabel(stringResource(R.string.translate_camera_lines_found, uiState.recognizedLines.size))
                }
            }

            if (uiState.isProcessing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(42.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

                Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 22.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                    RoundButton(
                        icon = Icons.Outlined.Image,
                        container = Color.White.copy(alpha = 0.75f),
                        tint = MaterialTheme.colorScheme.primary,
                        onClick = onImportPhoto
                    )
                CaptureButton(
                    enabled = uiState.hasCameraPermission && !uiState.isProcessing,
                    onCapture = onCapture
                )
                RoundButton(
                    icon = Icons.Outlined.FlashOn,
                    container = Color.White.copy(alpha = 0.75f),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        OcrResultPanel(
            uiState = uiState,
            onShareResult = onShareResult,
            onClearResult = onClearResult,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        )
    }
}

@Composable
private fun OcrOverlay(
    regions: List<RecognizedRegion>,
    translatedLines: List<String>,
    fallbackLines: List<String>,
    modifier: Modifier = Modifier,
) {
    if (regions.isEmpty()) return

    Canvas(modifier = modifier) {
        val strokeWidth = 3.dp.toPx()
        val boxColor = Color(0xFF73F1E7).copy(alpha = 0.85f)
        val bgColor = Color(0xFF006762).copy(alpha = 0.12f)
        val labelBgColor = Color(0xFF006762).copy(alpha = 0.65f)
        val textPaint = Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 12.dp.toPx()
            isAntiAlias = true
        }

        regions.forEachIndexed { index, region ->
            val left = region.left.coerceIn(0f, 1f) * size.width
            val top = region.top.coerceIn(0f, 1f) * size.height
            val right = region.right.coerceIn(0f, 1f) * size.width
            val bottom = region.bottom.coerceIn(0f, 1f) * size.height

            if (right <= left || bottom <= top) return@forEachIndexed

            drawRect(
                color = bgColor,
                topLeft = Offset(left, top),
                size = Size(right - left, bottom - top)
            )
            drawRect(
                color = boxColor,
                topLeft = Offset(left, top),
                size = Size(right - left, bottom - top),
                style = Stroke(width = strokeWidth)
            )

            val label = translatedLines.getOrNull(index)
                ?.takeIf { it.isNotBlank() }
                ?: fallbackLines.getOrNull(index).orEmpty()
            if (label.isNotBlank()) {
                val horizontalPadding = 6.dp.toPx()
                val verticalPadding = 4.dp.toPx()
                val baselineY = (top + 16.dp.toPx()).coerceAtMost(bottom - 4.dp.toPx())
                val maxLabelWidth = (right - left - horizontalPadding * 2f).coerceAtLeast(40.dp.toPx())
                val text = ellipsizeToWidth(label, textPaint, maxLabelWidth)
                val textWidth = textPaint.measureText(text)
                drawRect(
                    color = labelBgColor,
                    topLeft = Offset(left, top),
                    size = Size(
                        width = (textWidth + horizontalPadding * 2f).coerceAtMost(right - left),
                        height = textPaint.textSize + verticalPadding * 2f
                    )
                )
                drawContext.canvas.nativeCanvas.drawText(
                    text,
                    left + horizontalPadding,
                    baselineY,
                    textPaint
                )
            }
        }
    }
}

private fun ellipsizeToWidth(
    input: String,
    paint: Paint,
    maxWidth: Float,
): String {
    if (paint.measureText(input) <= maxWidth) return input
    val ellipsis = "…"
    val maxContentWidth = (maxWidth - paint.measureText(ellipsis)).coerceAtLeast(0f)
    var end = input.length
    while (end > 0 && paint.measureText(input, 0, end) > maxContentWidth) {
        end--
    }
    if (end <= 0) return ellipsis
    return input.substring(0, end) + ellipsis
}

@Composable
private fun CameraPreview(
    imageCapture: ImageCapture,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    DisposableEffect(lifecycleOwner, imageCapture) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val cameraExecutor = ContextCompat.getMainExecutor(context)

        val listener = Runnable {
            val cameraProvider = cameraProviderFuture.get()
            val preview = CameraPreviewUseCase.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )
        }

        cameraProviderFuture.addListener(listener, cameraExecutor)

        onDispose {
            runCatching {
                cameraProviderFuture.get().unbindAll()
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { previewView }
    )
}

@Composable
private fun CaptureButton(
    enabled: Boolean,
    onCapture: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(84.dp)
            .border(4.dp, Color.White.copy(alpha = 0.65f), CircleShape)
            .padding(6.dp)
            .background(
                color = if (enabled) Color.White else Color.White.copy(alpha = 0.45f),
                shape = CircleShape
            )
            .clickable(enabled = enabled) { onCapture() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.CameraAlt,
            contentDescription = stringResource(R.string.translate_capture),
            tint = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PermissionContent(
    onRequestCameraPermission: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(R.string.translate_camera_permission_required),
            color = Color.White
        )
        Button(onClick = onRequestCameraPermission) {
            Text(stringResource(R.string.action_grant_camera))
        }
    }
}

@Composable
private fun OcrResultPanel(
    uiState: TranslateUiState,
    onShareResult: () -> Unit,
    onClearResult: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.heightIn(min = 86.dp, max = 180.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.translate_ocr_result_title), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                if (uiState.recognizedLines.isNotEmpty()) {
                    Row {
                        TextButton(onClick = onShareResult) {
                            Text(stringResource(R.string.translate_share_result))
                        }
                        TextButton(onClick = onClearResult) {
                            Text(stringResource(R.string.action_clear))
                        }
                    }
                }
            }

            when {
                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                uiState.recognizedLines.isEmpty() -> {
                    Text(
                        text = stringResource(R.string.translate_ocr_empty),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (!uiState.ocrInsight.isNullOrBlank()) {
                            item {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = stringResource(R.string.translate_ai_hint_title),
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(uiState.ocrInsight, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                    }
                                }
                            }
                        }
                        items(uiState.recognizedLines.take(8).indices.toList()) { index ->
                            val source = uiState.recognizedLines[index]
                            val translated = uiState.translatedLines.getOrNull(index)
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = source,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (!translated.isNullOrBlank()) {
                                    Text(
                                        text = translated,
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PhraseCard(
    phrase: FavoritePhrase,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(phrase.sourceText, fontWeight = FontWeight.SemiBold)
            Text(phrase.translatedText, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun TranslateLabel(text: String) {
    Card(
        shape = RoundedCornerShape(999.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.75f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Restaurant,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(text = text, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun LanguageSelectionDialog(
    title: String,
    selected: TranslateLanguageOption,
    options: List<TranslateLanguageOption>,
    onDismiss: () -> Unit,
    onSelect: (TranslateLanguageOption) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                options.forEach { option ->
                    Text(
                        text = option.displayLabel(LocalContext.current) + if (option == selected) "  ✓" else "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onSelect(option) }
                            .padding(horizontal = 8.dp, vertical = 10.dp),
                        color = if (option == selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_close))
            }
        }
    )
}

@Composable
private fun RoundButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    container: Color,
    tint: Color,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .size(54.dp)
            .background(container, CircleShape)
            .let {
                if (onClick == null) it else it.clickable { onClick() }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint)
    }
}

@ComposePreview(showBackground = true, heightDp = 850)
@Composable
private fun TranslateScreenPreviewText() {
    TranToolsTheme {
        TranslateScreen(
            uiState = TranslateUiState(
                inputMode = TranslateInputMode.Text,
                textInput = "Where is the nearest train station?",
                textTranslation = "最近的火车站在哪里？"
            ),
            imageCapture = ImageCapture.Builder().build(),
            sourcePackStatus = TranslationPackStatus(TranslateLanguageOption.English, isDownloaded = true),
            targetPackStatus = TranslationPackStatus(TranslateLanguageOption.Chinese, isDownloaded = false),
            onCapture = {},
            onOpenSettings = {},
            onOpenHistory = {},
            onSwapLanguages = {},
            onSelectSourceLanguage = {},
            onSelectTargetLanguage = {},
            onSelectInputMode = {},
            onTextInputChange = {},
            onTranslateText = {},
            onSpeakTextTranslation = {},
            isTextSpeechReady = true,
            onRequestCameraPermission = {},
            onClearResult = {},
            onClearText = {},
            onToggleFavoritePhrase = {},
            onApplyPhrase = {},
            onImportPhoto = {},
            onShareResult = {},
            onDownloadMissingPacks = {}
        )
    }
}

@ComposePreview(showBackground = true, heightDp = 850)
@Composable
private fun TranslateScreenPreviewCamera() {
    TranToolsTheme {
        TranslateScreen(
            uiState = TranslateUiState(
                inputMode = TranslateInputMode.Camera,
                hasCameraPermission = true,
                recognizedLines = listOf("Grilled Salmon", "White Wine", "Garden Salad"),
                translatedLines = listOf("烤三文鱼", "白葡萄酒", "田园沙拉")
            ),
            imageCapture = ImageCapture.Builder().build(),
            sourcePackStatus = null,
            targetPackStatus = null,
            onCapture = {},
            onOpenSettings = {},
            onOpenHistory = {},
            onSwapLanguages = {},
            onSelectSourceLanguage = {},
            onSelectTargetLanguage = {},
            onSelectInputMode = {},
            onTextInputChange = {},
            onTranslateText = {},
            onSpeakTextTranslation = {},
            isTextSpeechReady = true,
            onRequestCameraPermission = {},
            onClearResult = {},
            onClearText = {},
            onToggleFavoritePhrase = {},
            onApplyPhrase = {},
            onImportPhoto = {},
            onShareResult = {},
            onDownloadMissingPacks = {}
        )
    }
}
