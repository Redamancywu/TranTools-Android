package com.neil.trantools.feature.voice;

import com.neil.trantools.feature.translate.TranslateLanguageOption;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u001d\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001Bq\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\f\u00a2\u0006\u0002\u0010\u0011J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010 \u001a\u0004\u0018\u00010\fH\u00c6\u0003J\t\u0010!\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\"\u001a\u00020\u0006H\u00c6\u0003J\t\u0010#\u001a\u00020\u0006H\u00c6\u0003J\t\u0010$\u001a\u00020\u0003H\u00c6\u0003J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\'\u001a\u00020\fH\u00c6\u0003J\u000f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u00c6\u0003Ju\u0010)\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\f2\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\fH\u00c6\u0001J\u0013\u0010*\u001a\u00020\u00032\b\u0010+\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010,\u001a\u00020-H\u00d6\u0001J\t\u0010.\u001a\u00020\fH\u00d6\u0001R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u0013\u0010\u0010\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0013R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016R\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0013R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001d\u00a8\u0006/"}, d2 = {"Lcom/neil/trantools/feature/voice/VoiceUiState;", "", "hasMicPermission", "", "isListening", "sourceLanguage", "Lcom/neil/trantools/feature/translate/TranslateLanguageOption;", "targetLanguage", "autoSpeak", "autoTurnTaking", "shouldRestartListening", "pendingTranscript", "", "messages", "", "Lcom/neil/trantools/feature/voice/VoiceMessage;", "errorMessage", "(ZZLcom/neil/trantools/feature/translate/TranslateLanguageOption;Lcom/neil/trantools/feature/translate/TranslateLanguageOption;ZZZLjava/lang/String;Ljava/util/List;Ljava/lang/String;)V", "getAutoSpeak", "()Z", "getAutoTurnTaking", "getErrorMessage", "()Ljava/lang/String;", "getHasMicPermission", "getMessages", "()Ljava/util/List;", "getPendingTranscript", "getShouldRestartListening", "getSourceLanguage", "()Lcom/neil/trantools/feature/translate/TranslateLanguageOption;", "getTargetLanguage", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "feature-voice_release"})
public final class VoiceUiState {
    private final boolean hasMicPermission = false;
    private final boolean isListening = false;
    @org.jetbrains.annotations.NotNull()
    private final com.neil.trantools.feature.translate.TranslateLanguageOption sourceLanguage = null;
    @org.jetbrains.annotations.NotNull()
    private final com.neil.trantools.feature.translate.TranslateLanguageOption targetLanguage = null;
    private final boolean autoSpeak = false;
    private final boolean autoTurnTaking = false;
    private final boolean shouldRestartListening = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String pendingTranscript = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.neil.trantools.feature.voice.VoiceMessage> messages = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String errorMessage = null;
    
    public VoiceUiState(boolean hasMicPermission, boolean isListening, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateLanguageOption sourceLanguage, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateLanguageOption targetLanguage, boolean autoSpeak, boolean autoTurnTaking, boolean shouldRestartListening, @org.jetbrains.annotations.NotNull()
    java.lang.String pendingTranscript, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.feature.voice.VoiceMessage> messages, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
        super();
    }
    
    public final boolean getHasMicPermission() {
        return false;
    }
    
    public final boolean isListening() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.feature.translate.TranslateLanguageOption getSourceLanguage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.feature.translate.TranslateLanguageOption getTargetLanguage() {
        return null;
    }
    
    public final boolean getAutoSpeak() {
        return false;
    }
    
    public final boolean getAutoTurnTaking() {
        return false;
    }
    
    public final boolean getShouldRestartListening() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPendingTranscript() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.feature.voice.VoiceMessage> getMessages() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErrorMessage() {
        return null;
    }
    
    public VoiceUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component10() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.feature.translate.TranslateLanguageOption component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.feature.translate.TranslateLanguageOption component4() {
        return null;
    }
    
    public final boolean component5() {
        return false;
    }
    
    public final boolean component6() {
        return false;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.feature.voice.VoiceMessage> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.feature.voice.VoiceUiState copy(boolean hasMicPermission, boolean isListening, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateLanguageOption sourceLanguage, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateLanguageOption targetLanguage, boolean autoSpeak, boolean autoTurnTaking, boolean shouldRestartListening, @org.jetbrains.annotations.NotNull()
    java.lang.String pendingTranscript, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.feature.voice.VoiceMessage> messages, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}