package com.neil.trantools.feature.translate;

import android.app.Application;
import android.net.Uri;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.lifecycle.AndroidViewModel;
import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.neil.trantools.core.ui.R;
import com.neil.trantools.data.history.HistoryMode;
import com.neil.trantools.data.history.HistoryStore;
import com.neil.trantools.data.history.TranslationHistoryEntity;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.nl.translate.Translation;
import com.neil.trantools.data.translation.FavoritePhrase;
import com.neil.trantools.data.translation.TranslationModelStore;
import com.neil.trantools.data.translation.TranslatePhraseStore;
import com.neil.trantools.domain.history.MapTranslateHistoryItemUseCase;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.StateFlow;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u008e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u0019J\u0018\u0010\u001a\u001a\u0004\u0018\u00010\u000e2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u000e0\u001cH\u0002J\u0016\u0010\u001d\u001a\u00020\u00142\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!J\u0006\u0010\"\u001a\u00020\u0014J\u0006\u0010#\u001a\u00020\u0014J\u0006\u0010$\u001a\u00020\u0014J\u0006\u0010%\u001a\u00020\u0014J\u0018\u0010&\u001a\u00020\u000b2\u0006\u0010\'\u001a\u00020\u000e2\u0006\u0010(\u001a\u00020\u000eH\u0002J\u000e\u0010)\u001a\u00020\u00142\u0006\u0010*\u001a\u00020+J\b\u0010,\u001a\u00020\u0014H\u0014J\u0010\u0010-\u001a\u00020\u00142\u0006\u0010.\u001a\u00020/H\u0002J\u000e\u00100\u001a\u00020\u00142\u0006\u00101\u001a\u000202J\u000e\u00103\u001a\u00020\u00142\u0006\u00104\u001a\u000205J\u000e\u00106\u001a\u00020\u00142\u0006\u00107\u001a\u000208J\u000e\u00109\u001a\u00020\u00142\u0006\u00107\u001a\u000208J\u000e\u0010:\u001a\u00020\u00142\u0006\u0010;\u001a\u00020\u000eJ\u0006\u0010<\u001a\u00020\u0014J\u0006\u0010=\u001a\u00020\u0014J\u0016\u0010>\u001a\u00020\u00142\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u000e0\u001cH\u0002J\u0006\u0010?\u001a\u00020\u0014R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\f\u001a\u0010\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00070\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006@"}, d2 = {"Lcom/neil/trantools/feature/translate/TranslateViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/neil/trantools/feature/translate/TranslateUiState;", "recognizer", "Lcom/google/mlkit/vision/text/TextRecognizer;", "translator", "Lcom/google/mlkit/nl/translate/Translator;", "translatorPair", "Lkotlin/Pair;", "", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "applyHistoryItem", "", "item", "Lcom/neil/trantools/data/history/TranslationHistoryEntity;", "applyPhrase", "phrase", "Lcom/neil/trantools/data/translation/FavoritePhrase;", "buildOcrInsight", "lines", "", "captureAndRecognize", "imageCapture", "Landroidx/camera/core/ImageCapture;", "executor", "Ljava/util/concurrent/Executor;", "clearRecognizedResult", "clearTextResult", "cycleSourceLanguage", "cycleTargetLanguage", "ensureTranslator", "sourceLanguage", "targetLanguage", "onCameraPermissionChanged", "granted", "", "onCleared", "processImage", "imageProxy", "Landroidx/camera/core/ImageProxy;", "processImportedImage", "uri", "Landroid/net/Uri;", "setInputMode", "mode", "Lcom/neil/trantools/feature/translate/TranslateInputMode;", "setSourceLanguage", "language", "Lcom/neil/trantools/feature/translate/TranslateLanguageOption;", "setTargetLanguage", "setTextInput", "text", "swapLanguages", "toggleCurrentFavorite", "translateRecognizedLines", "translateText", "feature-translate_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class TranslateViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.google.mlkit.vision.text.TextRecognizer recognizer = null;
    @org.jetbrains.annotations.Nullable()
    private com.google.mlkit.nl.translate.Translator translator;
    @org.jetbrains.annotations.Nullable()
    private kotlin.Pair<java.lang.String, java.lang.String> translatorPair;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.neil.trantools.feature.translate.TranslateUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.neil.trantools.feature.translate.TranslateUiState> uiState = null;
    
    @javax.inject.Inject()
    public TranslateViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.neil.trantools.feature.translate.TranslateUiState> getUiState() {
        return null;
    }
    
    public final void onCameraPermissionChanged(boolean granted) {
    }
    
    public final void setInputMode(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateInputMode mode) {
    }
    
    public final void setTextInput(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    public final void translateText() {
    }
    
    public final void applyPhrase(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.data.translation.FavoritePhrase phrase) {
    }
    
    public final void toggleCurrentFavorite() {
    }
    
    public final void captureAndRecognize(@org.jetbrains.annotations.NotNull()
    androidx.camera.core.ImageCapture imageCapture, @org.jetbrains.annotations.NotNull()
    java.util.concurrent.Executor executor) {
    }
    
    public final void processImportedImage(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    private final void processImage(androidx.camera.core.ImageProxy imageProxy) {
    }
    
    public final void clearRecognizedResult() {
    }
    
    public final void clearTextResult() {
    }
    
    public final void applyHistoryItem(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.data.history.TranslationHistoryEntity item) {
    }
    
    public final void cycleSourceLanguage() {
    }
    
    public final void cycleTargetLanguage() {
    }
    
    public final void setSourceLanguage(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateLanguageOption language) {
    }
    
    public final void setTargetLanguage(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateLanguageOption language) {
    }
    
    public final void swapLanguages() {
    }
    
    private final void translateRecognizedLines(java.util.List<java.lang.String> lines) {
    }
    
    private final java.lang.String buildOcrInsight(java.util.List<java.lang.String> lines) {
        return null;
    }
    
    private final com.google.mlkit.nl.translate.Translator ensureTranslator(java.lang.String sourceLanguage, java.lang.String targetLanguage) {
        return null;
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}