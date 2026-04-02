package com.neil.trantools.feature.voice

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neil.trantools.core.ui.R
import com.neil.trantools.data.history.TranslationHistoryEntity
import com.neil.trantools.feature.translate.TranslateLanguageOption
import com.neil.trantools.feature.translate.displayLabel
import com.neil.trantools.ui.components.VoyagerTopBar
import com.neil.trantools.ui.theme.TranToolsTheme
import java.util.Locale

@Composable
fun VoiceRoute(
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit = {},
    onOpenHistory: () -> Unit = {},
    prefillHistoryItem: TranslationHistoryEntity? = null,
    onPrefillConsumed: () -> Unit = {},
    viewModel: VoiceViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val mainExecutor = remember(context) { ContextCompat.getMainExecutor(context) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val latestUiState by rememberUpdatedState(uiState)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onMicPermissionChanged(granted)
    }

    val speechRecognizer = remember(context) {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            SpeechRecognizer.createSpeechRecognizer(context)
        } else {
            null
        }
    }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsReady by remember { mutableStateOf(false) }
    var spokenCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        viewModel.onMicPermissionChanged(granted)
        if (!granted) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    LaunchedEffect(prefillHistoryItem?.id) {
        val history = prefillHistoryItem ?: return@LaunchedEffect
        viewModel.applyHistoryItem(history)
        onPrefillConsumed()
    }

    DisposableEffect(context, speechRecognizer) {
        val textToSpeech = TextToSpeech(context) { status ->
            ttsReady = status == TextToSpeech.SUCCESS
        }
        textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) = Unit

            override fun onDone(utteranceId: String?) {
                val state = latestUiState
                if (!state.shouldRestartListening || !state.autoTurnTaking || !state.autoSpeak) return
                val recognizer = speechRecognizer ?: return
                mainExecutor.execute {
                    if (!state.hasMicPermission) return@execute
                    val intent = buildRecognizerIntent(context, state.sourceLanguage)
                    recognizer.startListening(intent)
                    viewModel.setListening(true)
                    viewModel.consumeRestartListening()
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) = Unit
        })
        tts = textToSpeech

        onDispose {
            tts?.stop()
            tts?.shutdown()
            speechRecognizer?.destroy()
        }
    }

    DisposableEffect(speechRecognizer) {
        val recognizer = speechRecognizer ?: return@DisposableEffect onDispose { }

        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) = Unit
            override fun onBeginningOfSpeech() = Unit
            override fun onRmsChanged(rmsdB: Float) = Unit
            override fun onBufferReceived(buffer: ByteArray?) = Unit
            override fun onEndOfSpeech() = Unit
            override fun onEvent(eventType: Int, params: Bundle?) = Unit

            override fun onError(error: Int) {
                viewModel.setListening(false)
                viewModel.onSpeechError(context.getString(R.string.voice_error_code, error))
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val partial = partialResults
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    .orEmpty()
                viewModel.onPartialSpeech(partial)
            }

            override fun onResults(results: Bundle?) {
                viewModel.setListening(false)
                val recognized = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    .orEmpty()
                viewModel.onSpeechRecognized(recognized)
            }
        })

        onDispose {
            recognizer.cancel()
        }
    }

    LaunchedEffect(uiState.messages.size, uiState.autoSpeak, ttsReady) {
        if (!uiState.autoSpeak || !ttsReady || tts == null) return@LaunchedEffect
        if (uiState.messages.isEmpty()) return@LaunchedEffect
        if (spokenCount >= uiState.messages.size) return@LaunchedEffect

        val latest = uiState.messages.last()
        val locale = localeFor(latest.targetLanguage)
        tts?.language = locale
        tts?.speak(latest.translatedText, TextToSpeech.QUEUE_FLUSH, null, "voice_translated_${uiState.messages.size}")
        spokenCount = uiState.messages.size
    }

    LaunchedEffect(uiState.shouldRestartListening, uiState.autoSpeak, ttsReady) {
        if (!uiState.shouldRestartListening) return@LaunchedEffect
        if (uiState.autoTurnTaking && uiState.autoSpeak && ttsReady && tts != null) return@LaunchedEffect
        val recognizer = speechRecognizer ?: return@LaunchedEffect
        if (!uiState.hasMicPermission) return@LaunchedEffect
        val intent = buildRecognizerIntent(context, uiState.sourceLanguage)
        recognizer.startListening(intent)
        viewModel.setListening(true)
        viewModel.consumeRestartListening()
    }

    VoiceScreen(
        uiState = uiState,
        modifier = modifier,
        onOpenSettings = onOpenSettings,
        onRequestMicPermission = { permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) },
        onCycleSourceLanguage = viewModel::cycleSourceLanguage,
        onCycleTargetLanguage = viewModel::cycleTargetLanguage,
        onSwapLanguages = viewModel::swapLanguages,
        onToggleAutoSpeak = viewModel::setAutoSpeak,
        onToggleAutoTurnTaking = viewModel::setAutoTurnTaking,
        onOpenHistory = onOpenHistory,
        onMicClick = {
            if (!uiState.hasMicPermission) {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                return@VoiceScreen
            }
            val recognizer = speechRecognizer
            if (recognizer == null) {
                viewModel.onSpeechError(context.getString(R.string.voice_error_unavailable))
                return@VoiceScreen
            }

            if (uiState.isListening) {
                recognizer.stopListening()
                viewModel.commitPendingSpeechIfAny()
            } else {
                viewModel.clearError()
                spokenCount = uiState.messages.size
                val intent = buildRecognizerIntent(context, uiState.sourceLanguage)
                recognizer.startListening(intent)
                viewModel.setListening(true)
            }
        }
    )
}

@Composable
fun VoiceScreen(
    uiState: VoiceUiState,
    onMicClick: () -> Unit,
    onRequestMicPermission: () -> Unit,
    onCycleSourceLanguage: () -> Unit,
    onCycleTargetLanguage: () -> Unit,
    onSwapLanguages: () -> Unit,
    onToggleAutoSpeak: (Boolean) -> Unit,
    onToggleAutoTurnTaking: (Boolean) -> Unit,
    onOpenHistory: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { VoyagerTopBar(onSettingsClick = onOpenSettings) }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(999.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
                    ) {
                        Row(
                            modifier = Modifier.padding(6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.voice_tab_realtime),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = stringResource(R.string.voice_tab_documents),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.clickable { onCycleSourceLanguage() }
                        ) {
                            Text(stringResource(R.string.voice_source), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(uiState.sourceLanguage.displayLabel(context), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Icon(
                            Icons.Outlined.SwapHoriz,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { onSwapLanguages() }
                        )
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.clickable { onCycleTargetLanguage() }
                        ) {
                            Text(stringResource(R.string.voice_target), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(uiState.targetLanguage.displayLabel(context), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.GraphicEq, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Text(stringResource(R.string.voice_auto_speak), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.History,
                                contentDescription = stringResource(R.string.voice_history),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable { onOpenHistory() }
                            )
                            Switch(checked = uiState.autoSpeak, onCheckedChange = onToggleAutoSpeak)
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Translate, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Column {
                                Text(stringResource(R.string.voice_auto_turn), color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    stringResource(
                                        R.string.voice_turn_direction,
                                        uiState.sourceLanguage.displayLabel(context),
                                        uiState.targetLanguage.displayLabel(context)
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Switch(checked = uiState.autoTurnTaking, onCheckedChange = onToggleAutoTurnTaking)
                    }
                }
            }

            if (!uiState.hasMicPermission) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(stringResource(R.string.voice_permission_required))
                            Text(
                                text = stringResource(R.string.action_grant_mic),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable { onRequestMicPermission() }
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }

            if (uiState.errorMessage != null) {
                item {
                    Text(
                        text = uiState.errorMessage,
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (uiState.messages.isEmpty()) {
                        Text(
                            stringResource(R.string.voice_start_hint),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        uiState.messages.forEach { message ->
                            MessageBubbleIn(
                                source = message.sourceText,
                                translated = message.translatedText
                            )
                        }
                    }

                    if (uiState.isListening) {
                        ListeningBubble(uiState.pendingTranscript)
                    }
                }
            }

            item { Box(modifier = Modifier.height(160.dp)) }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(999.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.9f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Outlined.GraphicEq, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    if (uiState.isListening) {
                        ListeningWaveform()
                    } else {
                        Text(stringResource(R.string.voice_tap_to_speak), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            IconButton(
                onClick = onMicClick,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(78.dp)
                    .background(
                        color = if (uiState.isListening) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Mic,
                    contentDescription = stringResource(R.string.voice_mic),
                    tint = if (uiState.isListening) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

private fun buildRecognizerIntent(
    context: Context,
    language: TranslateLanguageOption,
): Intent {
    val languageTag = localeFor(language).toLanguageTag()
    return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageTag)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languageTag)
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
    }
}

private fun localeFor(language: TranslateLanguageOption): Locale {
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
private fun MessageBubbleIn(source: String, translated: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            Text(source, modifier = Modifier.padding(16.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Outlined.Translate, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
            Text(translated, color = MaterialTheme.colorScheme.primary, fontStyle = FontStyle.Italic)
            Icon(Icons.Outlined.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun ListeningBubble(pendingTranscript: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            )
            Text(stringResource(R.string.voice_listening), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelLarge)
        }
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            Text(
                pendingTranscript.ifBlank { stringResource(R.string.voice_listening_hint) },
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
private fun ListeningWaveform() {
    val transition = rememberInfiniteTransition(label = "wave")
    val phase1 = transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 420, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "phase1"
    )
    val phase2 = transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 520, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "phase2"
    )
    val phase3 = transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 610, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "phase3"
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        WaveBar(heightScale = phase1.value)
        WaveBar(heightScale = phase2.value)
        WaveBar(heightScale = phase3.value)
        Text(stringResource(R.string.voice_listening), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun WaveBar(heightScale: Float) {
    Box(
        modifier = Modifier
            .size(width = 3.dp, height = (14f * heightScale).dp)
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(999.dp))
    )
}

@Preview(showBackground = true, heightDp = 850)
@Composable
private fun VoiceScreenPreview() {
    TranToolsTheme {
        VoiceScreen(
            uiState = VoiceUiState(
                hasMicPermission = true,
                messages = listOf(
                    VoiceMessage(
                        sourceLanguage = TranslateLanguageOption.English,
                        targetLanguage = TranslateLanguageOption.Japanese,
                        sourceText = "Where is the nearest subway station?",
                        translatedText = "Ginza line no moyori no chikatetsu eki wa doko desu ka?"
                    )
                )
            ),
            onMicClick = {},
            onRequestMicPermission = {},
            onCycleSourceLanguage = {},
            onCycleTargetLanguage = {},
            onSwapLanguages = {},
            onToggleAutoSpeak = {},
            onToggleAutoTurnTaking = {},
            onOpenHistory = {},
            onOpenSettings = {}
        )
    }
}
