package com.neil.trantools.feature.voice;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.neil.trantools.core.ui.R;
import com.neil.trantools.data.history.HistoryMode;
import com.neil.trantools.data.history.HistoryStore;
import com.neil.trantools.data.history.TranslationHistoryEntity;
import com.neil.trantools.data.translation.TranslationModelStore;
import com.neil.trantools.domain.history.MapVoiceHistoryItemUseCase;
import com.neil.trantools.feature.translate.TranslateLanguageOption;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u000e\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u0013J\u0006\u0010\u0017\u001a\u00020\u0013J\u0006\u0010\u0018\u001a\u00020\u0013J\u0006\u0010\u0019\u001a\u00020\u0013J\u0006\u0010\u001a\u001a\u00020\u0013J\u0018\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\t2\u0006\u0010\u001d\u001a\u00020\tH\u0002J\b\u0010\u001e\u001a\u00020\u0013H\u0014J\u000e\u0010\u001f\u001a\u00020\u00132\u0006\u0010 \u001a\u00020!J\u000e\u0010\"\u001a\u00020\u00132\u0006\u0010#\u001a\u00020\tJ\u000e\u0010$\u001a\u00020\u00132\u0006\u0010%\u001a\u00020\tJ\u000e\u0010&\u001a\u00020\u00132\u0006\u0010\'\u001a\u00020\tJ\u000e\u0010(\u001a\u00020\u00132\u0006\u0010)\u001a\u00020!J\u000e\u0010*\u001a\u00020\u00132\u0006\u0010)\u001a\u00020!J\u000e\u0010+\u001a\u00020\u00132\u0006\u0010,\u001a\u00020!J\u0006\u0010-\u001a\u00020\u0013J\u0010\u0010.\u001a\u00020\u00132\u0006\u0010\'\u001a\u00020\tH\u0002R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\f\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\t\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00070\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006/"}, d2 = {"Lcom/neil/trantools/feature/voice/VoiceViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/neil/trantools/feature/voice/VoiceUiState;", "recentlyCommittedFromManualStop", "", "translator", "Lcom/google/mlkit/nl/translate/Translator;", "translatorPair", "Lkotlin/Pair;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "applyHistoryItem", "", "item", "Lcom/neil/trantools/data/history/TranslationHistoryEntity;", "clearError", "commitPendingSpeechIfAny", "consumeRestartListening", "cycleSourceLanguage", "cycleTargetLanguage", "ensureTranslator", "sourceLanguage", "targetLanguage", "onCleared", "onMicPermissionChanged", "granted", "", "onPartialSpeech", "partialText", "onSpeechError", "message", "onSpeechRecognized", "sourceText", "setAutoSpeak", "enabled", "setAutoTurnTaking", "setListening", "listening", "swapLanguages", "translateAndAppend", "feature-voice_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class VoiceViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.neil.trantools.feature.voice.VoiceUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.neil.trantools.feature.voice.VoiceUiState> uiState = null;
    @org.jetbrains.annotations.Nullable()
    private com.google.mlkit.nl.translate.Translator translator;
    @org.jetbrains.annotations.Nullable()
    private kotlin.Pair<java.lang.String, java.lang.String> translatorPair;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String recentlyCommittedFromManualStop;
    
    @javax.inject.Inject()
    public VoiceViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.neil.trantools.feature.voice.VoiceUiState> getUiState() {
        return null;
    }
    
    public final void onMicPermissionChanged(boolean granted) {
    }
    
    public final void setListening(boolean listening) {
    }
    
    public final void cycleSourceLanguage() {
    }
    
    public final void cycleTargetLanguage() {
    }
    
    public final void swapLanguages() {
    }
    
    public final void setAutoSpeak(boolean enabled) {
    }
    
    public final void setAutoTurnTaking(boolean enabled) {
    }
    
    public final void consumeRestartListening() {
    }
    
    public final void applyHistoryItem(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.data.history.TranslationHistoryEntity item) {
    }
    
    public final void onSpeechRecognized(@org.jetbrains.annotations.NotNull()
    java.lang.String sourceText) {
    }
    
    public final void onSpeechError(@org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }
    
    public final void clearError() {
    }
    
    public final void onPartialSpeech(@org.jetbrains.annotations.NotNull()
    java.lang.String partialText) {
    }
    
    public final void commitPendingSpeechIfAny() {
    }
    
    private final void translateAndAppend(java.lang.String sourceText) {
    }
    
    private final com.google.mlkit.nl.translate.Translator ensureTranslator(java.lang.String sourceLanguage, java.lang.String targetLanguage) {
        return null;
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}