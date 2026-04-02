package com.neil.trantools.feature.translate

import android.app.Application
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.neil.trantools.core.ui.R
import com.neil.trantools.data.history.HistoryMode
import com.neil.trantools.data.history.HistoryStore
import com.neil.trantools.data.history.TranslationHistoryEntity
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.nl.translate.Translation
import com.neil.trantools.data.translation.FavoritePhrase
import com.neil.trantools.data.translation.TranslationModelStore
import com.neil.trantools.data.translation.TranslatePhraseStore
import com.neil.trantools.domain.history.MapTranslateHistoryItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private var translator: Translator? = null
    private var translatorPair: Pair<String, String>? = null
    private val _uiState = MutableStateFlow(TranslateUiState())
    val uiState: StateFlow<TranslateUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            TranslatePhraseStore.observeFavorites(getApplication()).collect { phrases ->
                _uiState.value = _uiState.value.copy(
                    favoritePhrases = phrases,
                    commonPhrases = TranslatePhraseStore.defaultCommonPhrases()
                )
            }
        }
    }

    fun onCameraPermissionChanged(granted: Boolean) {
        _uiState.value = _uiState.value.copy(hasCameraPermission = granted)
    }

    fun setInputMode(mode: TranslateInputMode) {
        _uiState.value = _uiState.value.copy(
            inputMode = mode,
            detectedTextSourceLanguage = if (mode == TranslateInputMode.Text &&
                _uiState.value.sourceLanguage == TranslateLanguageOption.Auto
            ) {
                detectTranslateLanguage(_uiState.value.textInput)
            } else {
                _uiState.value.detectedTextSourceLanguage
            },
            ocrInsight = if (mode == TranslateInputMode.Text) null else _uiState.value.ocrInsight,
            errorMessage = null
        )
    }

    fun setTextInput(text: String) {
        val detected = if (_uiState.value.sourceLanguage == TranslateLanguageOption.Auto) {
            detectTranslateLanguage(text)
        } else {
            null
        }
        _uiState.value = _uiState.value.copy(
            textInput = text,
            detectedTextSourceLanguage = detected,
            textTranslation = if (text == _uiState.value.textInput) _uiState.value.textTranslation else "",
            errorMessage = null
        )
    }

    fun translateText() {
        val input = _uiState.value.textInput.trim()
        if (input.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = getApplication<Application>().getString(R.string.translate_error_enter_text))
            return
        }

        val effectiveSourceLanguage = when (_uiState.value.sourceLanguage) {
            TranslateLanguageOption.Auto -> detectTranslateLanguage(input)
            else -> _uiState.value.sourceLanguage
        }
        if (effectiveSourceLanguage == null) {
            _uiState.value = _uiState.value.copy(errorMessage = getApplication<Application>().getString(R.string.translate_error_detect_source))
            return
        }

        val sourceCode = effectiveSourceLanguage.toMlKitCode()
        val targetCode = _uiState.value.targetLanguage.toMlKitCode()
        if (sourceCode == null || targetCode == null) {
            _uiState.value = _uiState.value.copy(errorMessage = getApplication<Application>().getString(R.string.translate_error_select_languages))
            return
        }

        _uiState.value = _uiState.value.copy(
            isProcessing = true,
            errorMessage = null
        )

        viewModelScope.launch {
            val translated = runCatching {
                withContext(Dispatchers.IO) {
                    val translatorClient = ensureTranslator(
                        sourceLanguage = sourceCode,
                        targetLanguage = targetCode
                    )
                    Tasks.await(translatorClient.downloadModelIfNeeded())
                    Tasks.await(translatorClient.translate(input))
                }
            }.getOrElse { error ->
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    errorMessage = error.message ?: getApplication<Application>().getString(R.string.translate_error_translation_failed)
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                isProcessing = false,
                detectedTextSourceLanguage = effectiveSourceLanguage,
                textTranslation = translated,
                errorMessage = null
            )

            runCatching {
                HistoryStore.insert(
                    TranslationHistoryEntity(
                        mode = HistoryMode.TEXT,
                        sourceLanguage = effectiveSourceLanguage.code,
                        targetLanguage = _uiState.value.targetLanguage.code,
                        sourceText = input,
                        translatedText = translated
                    )
                )
            }

            runCatching { TranslationModelStore.refresh() }
        }
    }

    fun applyPhrase(phrase: FavoritePhrase) {
        _uiState.value = _uiState.value.copy(
            inputMode = TranslateInputMode.Text,
            sourceLanguage = translateLanguageFromStored(phrase.sourceLanguage),
            targetLanguage = translateLanguageFromStored(phrase.targetLanguage),
            detectedTextSourceLanguage = translateLanguageFromStored(phrase.sourceLanguage),
            textInput = phrase.sourceText,
            textTranslation = phrase.translatedText,
            errorMessage = null
        )
    }

    fun toggleCurrentFavorite() {
        val input = _uiState.value.textInput.trim()
        val translated = _uiState.value.textTranslation.trim()
        if (input.isBlank() || translated.isBlank()) return
        viewModelScope.launch {
            TranslatePhraseStore.toggleFavorite(
                context = getApplication(),
                phrase = FavoritePhrase(
                    sourceLanguage = _uiState.value.effectiveTextSourceLanguage()?.code ?: _uiState.value.sourceLanguage.code,
                    targetLanguage = _uiState.value.targetLanguage.code,
                    sourceText = input,
                    translatedText = translated
                )
            )
        }
    }

    fun captureAndRecognize(
        imageCapture: ImageCapture,
        executor: Executor,
    ) {
        _uiState.value = _uiState.value.copy(
            isProcessing = true,
            translatedLines = emptyList(),
            recognizedRegions = emptyList(),
            errorMessage = null
        )

        imageCapture.takePicture(
            executor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    processImage(image)
                }

                override fun onError(exception: ImageCaptureException) {
                    _uiState.value = _uiState.value.copy(
                        isProcessing = false,
                        errorMessage = exception.message ?: getApplication<Application>().getString(R.string.translate_error_capture_failed)
                    )
                }
            }
        )
    }

    fun processImportedImage(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                inputMode = TranslateInputMode.Camera,
                isProcessing = true,
                errorMessage = null,
                translatedLines = emptyList(),
                recognizedRegions = emptyList()
            )

            val result = runCatching {
                val inputImage = InputImage.fromFilePath(getApplication(), uri)
                Tasks.await(recognizer.process(inputImage))
            }.getOrElse { error ->
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    errorMessage = error.message ?: getApplication<Application>().getString(R.string.translate_error_ocr_failed)
                )
                return@launch
            }

            val recognizedLines = mutableListOf<String>()
            result.textBlocks.forEach { block ->
                block.lines.forEach { line ->
                    val text = line.text.trim()
                    if (text.isNotEmpty()) recognizedLines += text
                }
            }

            _uiState.value = _uiState.value.copy(
                recognizedLines = recognizedLines,
                recognizedRegions = emptyList(),
                ocrInsight = buildOcrInsight(recognizedLines),
                errorMessage = if (recognizedLines.isEmpty()) {
                    getApplication<Application>().getString(R.string.translate_error_no_text)
                } else null,
                translatedLines = emptyList()
            )
            if (recognizedLines.isEmpty()) {
                _uiState.value = _uiState.value.copy(isProcessing = false)
            } else {
                translateRecognizedLines(recognizedLines)
            }
        }
    }

    private fun processImage(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            _uiState.value = _uiState.value.copy(
                isProcessing = false,
                errorMessage = getApplication<Application>().getString(R.string.translate_error_no_image)
            )
            return
        }

        val inputImage = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        recognizer.process(inputImage)
            .addOnSuccessListener { text ->
                val imageWidth = imageProxy.width.toFloat().coerceAtLeast(1f)
                val imageHeight = imageProxy.height.toFloat().coerceAtLeast(1f)

                val recognizedLines = mutableListOf<String>()
                val recognizedRegions = mutableListOf<RecognizedRegion>()

                text.textBlocks.forEach { block ->
                    block.lines.forEach { line ->
                        val lineText = line.text.trim()
                        if (lineText.isEmpty()) return@forEach

                        recognizedLines += lineText

                        line.boundingBox?.let { box ->
                            recognizedRegions += RecognizedRegion(
                                left = (box.left / imageWidth).coerceIn(0f, 1f),
                                top = (box.top / imageHeight).coerceIn(0f, 1f),
                                right = (box.right / imageWidth).coerceIn(0f, 1f),
                                bottom = (box.bottom / imageHeight).coerceIn(0f, 1f)
                            )
                        }
                    }
                }

                _uiState.value = _uiState.value.copy(
                    recognizedLines = recognizedLines,
                    recognizedRegions = recognizedRegions,
                    ocrInsight = buildOcrInsight(recognizedLines),
                    errorMessage = if (recognizedLines.isEmpty()) {
                        getApplication<Application>().getString(R.string.translate_error_no_text)
                    } else {
                        null
                    },
                    translatedLines = emptyList()
                )
                if (recognizedLines.isEmpty()) {
                    _uiState.value = _uiState.value.copy(isProcessing = false)
                } else {
                    translateRecognizedLines(recognizedLines)
                }
            }
            .addOnFailureListener { error ->
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    errorMessage = error.message ?: getApplication<Application>().getString(R.string.translate_error_ocr_failed)
                )
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    fun clearRecognizedResult() {
        _uiState.value = _uiState.value.copy(
            recognizedLines = emptyList(),
            translatedLines = emptyList(),
            recognizedRegions = emptyList(),
            ocrInsight = null,
            errorMessage = null
        )
    }

    fun clearTextResult() {
        _uiState.value = _uiState.value.copy(
            detectedTextSourceLanguage = null,
            textInput = "",
            textTranslation = "",
            errorMessage = null
        )
    }

    fun applyHistoryItem(item: TranslationHistoryEntity) {
        val prefill = MapTranslateHistoryItemUseCase(item) ?: return
        _uiState.value = _uiState.value.copy(
            inputMode = if (prefill.isTextMode) TranslateInputMode.Text else TranslateInputMode.Camera,
            sourceLanguage = prefill.sourceLanguage,
            targetLanguage = prefill.targetLanguage,
            detectedTextSourceLanguage = prefill.detectedSourceLanguage,
            textInput = prefill.textInput,
            textTranslation = prefill.textTranslation,
            recognizedLines = prefill.recognizedLines,
            translatedLines = prefill.translatedLines,
            recognizedRegions = emptyList(),
            ocrInsight = if (prefill.recognizedLines.isNotEmpty()) buildOcrInsight(prefill.recognizedLines) else null,
            errorMessage = null,
            isProcessing = false
        )
    }

    fun cycleSourceLanguage() {
        val sequence = listOf(
            TranslateLanguageOption.Japanese,
            TranslateLanguageOption.English,
            TranslateLanguageOption.Chinese,
            TranslateLanguageOption.Korean,
            TranslateLanguageOption.Spanish
        )
        val current = _uiState.value.sourceLanguage
        val next = sequence[(sequence.indexOf(current).takeIf { it >= 0 } ?: 0).let { (it + 1) % sequence.size }]
        _uiState.value = _uiState.value.copy(sourceLanguage = next)
    }

    fun cycleTargetLanguage() {
        val sequence = listOf(
            TranslateLanguageOption.English,
            TranslateLanguageOption.Japanese,
            TranslateLanguageOption.Chinese,
            TranslateLanguageOption.Korean,
            TranslateLanguageOption.Spanish
        )
        val current = _uiState.value.targetLanguage
        val next = sequence[(sequence.indexOf(current).takeIf { it >= 0 } ?: 0).let { (it + 1) % sequence.size }]
        _uiState.value = _uiState.value.copy(targetLanguage = next)
    }

    fun setSourceLanguage(language: TranslateLanguageOption) {
        _uiState.value = _uiState.value.copy(
            sourceLanguage = language,
            detectedTextSourceLanguage = if (language == TranslateLanguageOption.Auto) {
                detectTranslateLanguage(_uiState.value.textInput)
            } else {
                null
            },
            errorMessage = null
        )
    }

    fun setTargetLanguage(language: TranslateLanguageOption) {
        _uiState.value = _uiState.value.copy(
            targetLanguage = language,
            errorMessage = null
        )
    }

    fun swapLanguages() {
        val source = _uiState.value.sourceLanguage
        val target = _uiState.value.targetLanguage
        if (source == TranslateLanguageOption.Auto || target == TranslateLanguageOption.Auto) {
            return
        }
        _uiState.value = _uiState.value.copy(
            sourceLanguage = target,
            targetLanguage = source,
            detectedTextSourceLanguage = null
        )
    }

    private fun translateRecognizedLines(lines: List<String>) {
        viewModelScope.launch {
            val sourceCode = _uiState.value.sourceLanguage.toMlKitCode()
            val targetCode = _uiState.value.targetLanguage.toMlKitCode()
            if (sourceCode == null || targetCode == null || sourceCode == targetCode) {
                _uiState.value = _uiState.value.copy(
                    translatedLines = lines,
                    isProcessing = false
                )
                return@launch
            }

            val translated = runCatching {
                withContext(Dispatchers.IO) {
                    val translatorClient = ensureTranslator(
                        sourceLanguage = sourceCode,
                        targetLanguage = targetCode
                    )

                    Tasks.await(translatorClient.downloadModelIfNeeded())
                    lines.map { source -> Tasks.await(translatorClient.translate(source)) }
                }
            }.getOrElse {
                lines
            }

            _uiState.value = _uiState.value.copy(
                translatedLines = translated,
                isProcessing = false
            )

            runCatching {
                HistoryStore.insert(
                    TranslationHistoryEntity(
                        mode = HistoryMode.OCR,
                        sourceLanguage = _uiState.value.sourceLanguage.code,
                        targetLanguage = _uiState.value.targetLanguage.code,
                        sourceText = lines.joinToString("\n"),
                        translatedText = translated.joinToString("\n")
                    )
                )
            }

            runCatching { TranslationModelStore.refresh() }
        }
    }

    private fun buildOcrInsight(lines: List<String>): String? {
        if (lines.isEmpty()) return null
        val joined = lines.joinToString(" ").lowercase()
        return when {
            joined.contains("¥") || joined.contains("$") || joined.contains("元") ->
                getApplication<Application>().getString(R.string.translate_ai_hint_price)
            joined.contains("exit") || joined.contains("station") || joined.contains("platform") ->
                getApplication<Application>().getString(R.string.translate_ai_hint_wayfinding)
            joined.contains("ramen") || joined.contains("sushi") || joined.contains("salmon") || joined.contains("pork") ->
                getApplication<Application>().getString(R.string.translate_ai_hint_menu)
            joined.contains("warning") || joined.contains("禁止") || joined.contains("danger") ->
                getApplication<Application>().getString(R.string.translate_ai_hint_warning)
            else ->
                getApplication<Application>().getString(R.string.translate_ai_hint_generic)
        }
    }

    private fun ensureTranslator(
        sourceLanguage: String,
        targetLanguage: String,
    ): Translator {
        val requestedPair = sourceLanguage to targetLanguage
        val existing = translator
        if (existing != null && translatorPair == requestedPair) return existing

        existing?.close()

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()

        return Translation.getClient(options).also {
            translator = it
            translatorPair = requestedPair
        }
    }

    override fun onCleared() {
        super.onCleared()
        recognizer.close()
        translator?.close()
    }
}
