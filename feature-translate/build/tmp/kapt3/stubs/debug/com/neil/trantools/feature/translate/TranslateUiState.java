package com.neil.trantools.feature.translate;

import com.neil.trantools.data.translation.TranslationPackStatus;
import com.neil.trantools.data.translation.FavoritePhrase;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b(\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u00bf\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\b\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\f\u0012\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f\u0012\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\f\u0012\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\f0\u000f\u0012\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\f0\u000f\u0012\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u000f\u0012\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\f\u00a2\u0006\u0002\u0010\u0018J\t\u0010,\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u00c6\u0003J\u000b\u0010.\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\u000f\u0010/\u001a\b\u0012\u0004\u0012\u00020\f0\u000fH\u00c6\u0003J\u000f\u00100\u001a\b\u0012\u0004\u0012\u00020\f0\u000fH\u00c6\u0003J\u000f\u00101\u001a\b\u0012\u0004\u0012\u00020\u00160\u000fH\u00c6\u0003J\u000b\u00102\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\t\u00103\u001a\u00020\u0005H\u00c6\u0003J\t\u00104\u001a\u00020\u0005H\u00c6\u0003J\t\u00105\u001a\u00020\bH\u00c6\u0003J\t\u00106\u001a\u00020\bH\u00c6\u0003J\u000b\u00107\u001a\u0004\u0018\u00010\bH\u00c6\u0003J\t\u00108\u001a\u00020\fH\u00c6\u0003J\t\u00109\u001a\u00020\fH\u00c6\u0003J\u000f\u0010:\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u00c6\u0003J\u00c3\u0001\u0010;\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\f2\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\f2\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\f0\u000f2\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\f0\u000f2\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u000f2\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\fH\u00c6\u0001J\u0013\u0010<\u001a\u00020\u00052\b\u0010=\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010>\u001a\u00020?H\u00d6\u0001J\t\u0010@\u001a\u00020\fH\u00d6\u0001R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0013\u0010\n\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0013\u0010\u0017\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001aR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010!R\u0013\u0010\u0012\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001eR\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\f0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001aR\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001aR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001cR\u0011\u0010\t\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001cR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001eR\u0011\u0010\r\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001eR\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\f0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001a\u00a8\u0006A"}, d2 = {"Lcom/neil/trantools/feature/translate/TranslateUiState;", "", "inputMode", "Lcom/neil/trantools/feature/translate/TranslateInputMode;", "hasCameraPermission", "", "isProcessing", "sourceLanguage", "Lcom/neil/trantools/feature/translate/TranslateLanguageOption;", "targetLanguage", "detectedTextSourceLanguage", "textInput", "", "textTranslation", "favoritePhrases", "", "Lcom/neil/trantools/data/translation/FavoritePhrase;", "commonPhrases", "ocrInsight", "recognizedLines", "translatedLines", "recognizedRegions", "Lcom/neil/trantools/feature/translate/RecognizedRegion;", "errorMessage", "(Lcom/neil/trantools/feature/translate/TranslateInputMode;ZZLcom/neil/trantools/feature/translate/TranslateLanguageOption;Lcom/neil/trantools/feature/translate/TranslateLanguageOption;Lcom/neil/trantools/feature/translate/TranslateLanguageOption;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/util/List;Ljava/lang/String;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/lang/String;)V", "getCommonPhrases", "()Ljava/util/List;", "getDetectedTextSourceLanguage", "()Lcom/neil/trantools/feature/translate/TranslateLanguageOption;", "getErrorMessage", "()Ljava/lang/String;", "getFavoritePhrases", "getHasCameraPermission", "()Z", "getInputMode", "()Lcom/neil/trantools/feature/translate/TranslateInputMode;", "getOcrInsight", "getRecognizedLines", "getRecognizedRegions", "getSourceLanguage", "getTargetLanguage", "getTextInput", "getTextTranslation", "getTranslatedLines", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "feature-translate_debug"})
public final class TranslateUiState {
    @org.jetbrains.annotations.NotNull()
    private final com.neil.trantools.feature.translate.TranslateInputMode inputMode = null;
    private final boolean hasCameraPermission = false;
    private final boolean isProcessing = false;
    @org.jetbrains.annotations.NotNull()
    private final com.neil.trantools.feature.translate.TranslateLanguageOption sourceLanguage = null;
    @org.jetbrains.annotations.NotNull()
    private final com.neil.trantools.feature.translate.TranslateLanguageOption targetLanguage = null;
    @org.jetbrains.annotations.Nullable()
    private final com.neil.trantools.feature.translate.TranslateLanguageOption detectedTextSourceLanguage = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String textInput = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String textTranslation = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.neil.trantools.data.translation.FavoritePhrase> favoritePhrases = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.neil.trantools.data.translation.FavoritePhrase> commonPhrases = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String ocrInsight = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> recognizedLines = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> translatedLines = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.neil.trantools.feature.translate.RecognizedRegion> recognizedRegions = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String errorMessage = null;
    
    public TranslateUiState(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateInputMode inputMode, boolean hasCameraPermission, boolean isProcessing, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateLanguageOption sourceLanguage, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateLanguageOption targetLanguage, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.feature.translate.TranslateLanguageOption detectedTextSourceLanguage, @org.jetbrains.annotations.NotNull()
    java.lang.String textInput, @org.jetbrains.annotations.NotNull()
    java.lang.String textTranslation, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.translation.FavoritePhrase> favoritePhrases, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.translation.FavoritePhrase> commonPhrases, @org.jetbrains.annotations.Nullable()
    java.lang.String ocrInsight, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> recognizedLines, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> translatedLines, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.feature.translate.RecognizedRegion> recognizedRegions, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.feature.translate.TranslateInputMode getInputMode() {
        return null;
    }
    
    public final boolean getHasCameraPermission() {
        return false;
    }
    
    public final boolean isProcessing() {
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
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.feature.translate.TranslateLanguageOption getDetectedTextSourceLanguage() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTextInput() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTextTranslation() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.translation.FavoritePhrase> getFavoritePhrases() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.translation.FavoritePhrase> getCommonPhrases() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getOcrInsight() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getRecognizedLines() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getTranslatedLines() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.feature.translate.RecognizedRegion> getRecognizedRegions() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErrorMessage() {
        return null;
    }
    
    public TranslateUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.feature.translate.TranslateInputMode component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.translation.FavoritePhrase> component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component12() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component13() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.feature.translate.RecognizedRegion> component14() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component15() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    public final boolean component3() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.feature.translate.TranslateLanguageOption component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.feature.translate.TranslateLanguageOption component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.feature.translate.TranslateLanguageOption component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.translation.FavoritePhrase> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.feature.translate.TranslateUiState copy(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateInputMode inputMode, boolean hasCameraPermission, boolean isProcessing, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateLanguageOption sourceLanguage, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateLanguageOption targetLanguage, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.feature.translate.TranslateLanguageOption detectedTextSourceLanguage, @org.jetbrains.annotations.NotNull()
    java.lang.String textInput, @org.jetbrains.annotations.NotNull()
    java.lang.String textTranslation, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.translation.FavoritePhrase> favoritePhrases, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.translation.FavoritePhrase> commonPhrases, @org.jetbrains.annotations.Nullable()
    java.lang.String ocrInsight, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> recognizedLines, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> translatedLines, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.feature.translate.RecognizedRegion> recognizedRegions, @org.jetbrains.annotations.Nullable()
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