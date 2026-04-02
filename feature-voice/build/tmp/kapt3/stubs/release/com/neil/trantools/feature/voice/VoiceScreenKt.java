package com.neil.trantools.feature.voice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.animation.core.RepeatMode;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.CardDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.tooling.preview.Preview;
import androidx.core.content.ContextCompat;
import com.neil.trantools.core.ui.R;
import com.neil.trantools.data.history.TranslationHistoryEntity;
import com.neil.trantools.feature.translate.TranslateLanguageOption;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000`\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a\b\u0010\u0004\u001a\u00020\u0001H\u0003\u001a\u0018\u0010\u0005\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0003H\u0003\u001aX\u0010\b\u001a\u00020\u00012\b\b\u0002\u0010\t\u001a\u00020\n2\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\b\b\u0002\u0010\u0011\u001a\u00020\u0012H\u0007\u001a\u00a4\u0001\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u00010\u001c2\u0012\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u00010\u001c2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\f2\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a\b\u0010\u001f\u001a\u00020\u0001H\u0003\u001a\u0010\u0010 \u001a\u00020\u00012\u0006\u0010!\u001a\u00020\"H\u0003\u001a\u0018\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020(H\u0002\u001a\u0010\u0010)\u001a\u00020*2\u0006\u0010\'\u001a\u00020(H\u0002\u00a8\u0006+"}, d2 = {"ListeningBubble", "", "pendingTranscript", "", "ListeningWaveform", "MessageBubbleIn", "source", "translated", "VoiceRoute", "modifier", "Landroidx/compose/ui/Modifier;", "onOpenSettings", "Lkotlin/Function0;", "onOpenHistory", "prefillHistoryItem", "Lcom/neil/trantools/data/history/TranslationHistoryEntity;", "onPrefillConsumed", "viewModel", "Lcom/neil/trantools/feature/voice/VoiceViewModel;", "VoiceScreen", "uiState", "Lcom/neil/trantools/feature/voice/VoiceUiState;", "onMicClick", "onRequestMicPermission", "onCycleSourceLanguage", "onCycleTargetLanguage", "onSwapLanguages", "onToggleAutoSpeak", "Lkotlin/Function1;", "", "onToggleAutoTurnTaking", "VoiceScreenPreview", "WaveBar", "heightScale", "", "buildRecognizerIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "language", "Lcom/neil/trantools/feature/translate/TranslateLanguageOption;", "localeFor", "Ljava/util/Locale;", "feature-voice_release"})
public final class VoiceScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void VoiceRoute(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenHistory, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.history.TranslationHistoryEntity prefillHistoryItem, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onPrefillConsumed, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.voice.VoiceViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void VoiceScreen(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.voice.VoiceUiState uiState, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onMicClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRequestMicPermission, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCycleSourceLanguage, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCycleTargetLanguage, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSwapLanguages, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onToggleAutoSpeak, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onToggleAutoTurnTaking, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenHistory, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    private static final android.content.Intent buildRecognizerIntent(android.content.Context context, com.neil.trantools.feature.translate.TranslateLanguageOption language) {
        return null;
    }
    
    private static final java.util.Locale localeFor(com.neil.trantools.feature.translate.TranslateLanguageOption language) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MessageBubbleIn(java.lang.String source, java.lang.String translated) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ListeningBubble(java.lang.String pendingTranscript) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ListeningWaveform() {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void WaveBar(float heightScale) {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true, heightDp = 850)
    @androidx.compose.runtime.Composable()
    private static final void VoiceScreenPreview() {
    }
}