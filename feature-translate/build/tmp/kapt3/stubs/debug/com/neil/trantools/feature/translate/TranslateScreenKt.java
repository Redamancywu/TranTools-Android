package com.neil.trantools.feature.translate;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.CardDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.graphics.drawscope.Stroke;
import androidx.compose.ui.text.font.FontWeight;
import androidx.core.content.ContextCompat;
import com.neil.trantools.core.ui.R;
import com.neil.trantools.data.history.TranslationHistoryEntity;
import com.neil.trantools.data.translation.FavoritePhrase;
import com.neil.trantools.data.translation.TranslationModelStore;
import com.neil.trantools.data.translation.TranslationPackStatus;
import androidx.camera.core.Preview;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u0088\u0001\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a\u001a\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0003\u001az\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u0006\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\u0012\u001a\u001e\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u00152\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\nH\u0003\u001a*\u0010\u0016\u001a\u00020\u00012\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u00182\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00010\nH\u0003\u001aD\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0003\u001aH\u0010\u001f\u001a\u00020\u00012\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020#0%2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u0010\'\u001a\u000e\u0012\u0004\u0012\u00020#\u0012\u0004\u0012\u00020\u00010(H\u0003\u001a.\u0010)\u001a\u00020\u00012\u0006\u0010*\u001a\u00020+2\u0012\u0010,\u001a\u000e\u0012\u0004\u0012\u00020+\u0012\u0004\u0012\u00020\u00010(2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0003\u001a \u0010-\u001a\u00020\u00012\f\u0010.\u001a\b\u0012\u0004\u0012\u00020/0%2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0003\u001a6\u00100\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0003\u001a \u00101\u001a\u00020\u00012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0003\u001a\u001e\u00102\u001a\u00020\u00012\u0006\u00103\u001a\u0002042\f\u00105\u001a\b\u0012\u0004\u0012\u00020\u00010\nH\u0003\u001a<\u00106\u001a\u00020\u00012\u0006\u00107\u001a\u0002082\u0006\u00109\u001a\u00020\u00102\u0006\u0010:\u001a\u00020\u00102\u0010\b\u0002\u00105\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\nH\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b;\u0010<\u001a\u009c\u0001\u0010=\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u00182\u0012\u0010>\u001a\u000e\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020\u00010(2\f\u0010?\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010@\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010A\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u0010B\u001a\u000e\u0012\u0004\u0012\u000204\u0012\u0004\u0012\u00020\u00010(2\f\u0010C\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0003\u001a\u0010\u0010D\u001a\u00020\u00012\u0006\u0010E\u001a\u00020!H\u0003\u001aX\u0010F\u001a\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u000e\b\u0002\u0010G\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u000e\b\u0002\u0010H\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\n\b\u0002\u0010I\u001a\u0004\u0018\u00010J2\u000e\b\u0002\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010L\u001a\u00020MH\u0007\u001a\u00c2\u0002\u0010N\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u00182\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010G\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010H\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u0010O\u001a\u000e\u0012\u0004\u0012\u00020#\u0012\u0004\u0012\u00020\u00010(2\u0012\u0010P\u001a\u000e\u0012\u0004\u0012\u00020#\u0012\u0004\u0012\u00020\u00010(2\u0012\u0010Q\u001a\u000e\u0012\u0004\u0012\u00020+\u0012\u0004\u0012\u00020\u00010(2\u0012\u0010>\u001a\u000e\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020\u00010(2\f\u0010?\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010@\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010A\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u0012\u0010B\u001a\u000e\u0012\u0004\u0012\u000204\u0012\u0004\u0012\u00020\u00010(2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007\u001a\b\u0010R\u001a\u00020\u0001H\u0003\u001a\b\u0010S\u001a\u00020\u0001H\u0003\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006T"}, d2 = {"CameraPreview", "", "imageCapture", "Landroidx/camera/core/ImageCapture;", "modifier", "Landroidx/compose/ui/Modifier;", "CameraTranslatePane", "uiState", "Lcom/neil/trantools/feature/translate/TranslateUiState;", "onCapture", "Lkotlin/Function0;", "onImportPhoto", "onShareResult", "onRequestCameraPermission", "onClearResult", "cameraFrame", "Landroidx/compose/ui/graphics/Color;", "CameraTranslatePane-Mcns07U", "(Lcom/neil/trantools/feature/translate/TranslateUiState;Landroidx/camera/core/ImageCapture;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;JLandroidx/compose/ui/Modifier;)V", "CaptureButton", "enabled", "", "CurrentPackStatusCard", "sourcePackStatus", "Lcom/neil/trantools/data/translation/TranslationPackStatus;", "targetPackStatus", "onDownloadMissingPacks", "LanguageBar", "onSwapLanguages", "onOpenSourceDialog", "onOpenTargetDialog", "LanguageSelectionDialog", "title", "", "selected", "Lcom/neil/trantools/feature/translate/TranslateLanguageOption;", "options", "", "onDismiss", "onSelect", "Lkotlin/Function1;", "ModeSwitcher", "selectedMode", "Lcom/neil/trantools/feature/translate/TranslateInputMode;", "onSelectMode", "OcrOverlay", "regions", "Lcom/neil/trantools/feature/translate/RecognizedRegion;", "OcrResultPanel", "PermissionContent", "PhraseCard", "phrase", "Lcom/neil/trantools/data/translation/FavoritePhrase;", "onClick", "RoundButton", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "container", "tint", "RoundButton-IbeAmgk", "(Landroidx/compose/ui/graphics/vector/ImageVector;JJLkotlin/jvm/functions/Function0;)V", "TextTranslatePane", "onTextInputChange", "onTranslateText", "onClearText", "onToggleFavoritePhrase", "onApplyPhrase", "onCopyTranslation", "TranslateLabel", "text", "TranslateRoute", "onOpenSettings", "onOpenHistory", "prefillHistoryItem", "Lcom/neil/trantools/data/history/TranslationHistoryEntity;", "onPrefillConsumed", "viewModel", "Lcom/neil/trantools/feature/translate/TranslateViewModel;", "TranslateScreen", "onSelectSourceLanguage", "onSelectTargetLanguage", "onSelectInputMode", "TranslateScreenPreviewCamera", "TranslateScreenPreviewText", "feature-translate_debug"})
public final class TranslateScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void TranslateRoute(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenHistory, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.history.TranslationHistoryEntity prefillHistoryItem, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onPrefillConsumed, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void TranslateScreen(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateUiState uiState, @org.jetbrains.annotations.NotNull()
    androidx.camera.core.ImageCapture imageCapture, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.translation.TranslationPackStatus sourcePackStatus, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.translation.TranslationPackStatus targetPackStatus, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onCapture, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenHistory, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSwapLanguages, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.neil.trantools.feature.translate.TranslateLanguageOption, kotlin.Unit> onSelectSourceLanguage, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.neil.trantools.feature.translate.TranslateLanguageOption, kotlin.Unit> onSelectTargetLanguage, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.neil.trantools.feature.translate.TranslateInputMode, kotlin.Unit> onSelectInputMode, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onTextInputChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onTranslateText, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRequestCameraPermission, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClearResult, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClearText, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onToggleFavoritePhrase, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.neil.trantools.data.translation.FavoritePhrase, kotlin.Unit> onApplyPhrase, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onImportPhoto, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onShareResult, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDownloadMissingPacks, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ModeSwitcher(com.neil.trantools.feature.translate.TranslateInputMode selectedMode, kotlin.jvm.functions.Function1<? super com.neil.trantools.feature.translate.TranslateInputMode, kotlin.Unit> onSelectMode, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LanguageBar(com.neil.trantools.feature.translate.TranslateUiState uiState, kotlin.jvm.functions.Function0<kotlin.Unit> onSwapLanguages, kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSourceDialog, kotlin.jvm.functions.Function0<kotlin.Unit> onOpenTargetDialog, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TextTranslatePane(com.neil.trantools.feature.translate.TranslateUiState uiState, com.neil.trantools.data.translation.TranslationPackStatus sourcePackStatus, com.neil.trantools.data.translation.TranslationPackStatus targetPackStatus, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onTextInputChange, kotlin.jvm.functions.Function0<kotlin.Unit> onTranslateText, kotlin.jvm.functions.Function0<kotlin.Unit> onClearText, kotlin.jvm.functions.Function0<kotlin.Unit> onToggleFavoritePhrase, kotlin.jvm.functions.Function1<? super com.neil.trantools.data.translation.FavoritePhrase, kotlin.Unit> onApplyPhrase, kotlin.jvm.functions.Function0<kotlin.Unit> onCopyTranslation, kotlin.jvm.functions.Function0<kotlin.Unit> onDownloadMissingPacks, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CurrentPackStatusCard(com.neil.trantools.data.translation.TranslationPackStatus sourcePackStatus, com.neil.trantools.data.translation.TranslationPackStatus targetPackStatus, kotlin.jvm.functions.Function0<kotlin.Unit> onDownloadMissingPacks) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void OcrOverlay(java.util.List<com.neil.trantools.feature.translate.RecognizedRegion> regions, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CameraPreview(androidx.camera.core.ImageCapture imageCapture, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CaptureButton(boolean enabled, kotlin.jvm.functions.Function0<kotlin.Unit> onCapture) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PermissionContent(kotlin.jvm.functions.Function0<kotlin.Unit> onRequestCameraPermission, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void OcrResultPanel(com.neil.trantools.feature.translate.TranslateUiState uiState, kotlin.jvm.functions.Function0<kotlin.Unit> onShareResult, kotlin.jvm.functions.Function0<kotlin.Unit> onClearResult, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PhraseCard(com.neil.trantools.data.translation.FavoritePhrase phrase, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TranslateLabel(java.lang.String text) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LanguageSelectionDialog(java.lang.String title, com.neil.trantools.feature.translate.TranslateLanguageOption selected, java.util.List<? extends com.neil.trantools.feature.translate.TranslateLanguageOption> options, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, kotlin.jvm.functions.Function1<? super com.neil.trantools.feature.translate.TranslateLanguageOption, kotlin.Unit> onSelect) {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true, heightDp = 850)
    @androidx.compose.runtime.Composable()
    private static final void TranslateScreenPreviewText() {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true, heightDp = 850)
    @androidx.compose.runtime.Composable()
    private static final void TranslateScreenPreviewCamera() {
    }
}