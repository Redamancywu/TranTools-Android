package com.neil.trantools.feature.gems;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.CardDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.tooling.preview.Preview;
import androidx.core.content.ContextCompat;
import com.neil.trantools.core.ui.R;
import com.neil.trantools.data.gems.GemCategory;
import com.neil.trantools.data.gems.GemPoi;
import com.neil.trantools.data.gems.GemPoiWithDistance;
import com.neil.trantools.data.gems.GemsRepository;
import com.neil.trantools.domain.gems.BuildGemWikiContextUseCase;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\u001a>\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001az\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u0006\u001a\u00020\u00072\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u0014\b\u0002\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00112\u0014\b\u0002\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00112\u0014\b\u0002\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u0011H\u0007\u001a\u00a6\u0001\u0010\u0014\u001a\u00020\u00012\b\u0010\u0015\u001a\u0004\u0018\u00010\u00162\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u0014\b\u0002\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00112\u0014\b\u0002\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00112\u0014\b\u0002\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00112\u0014\b\u0002\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u0011H\u0007\u001a8\u0010\u0019\u001a\u00020\u00012\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u0014\b\u0002\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u0011H\u0007\u001a\u00a4\u0001\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u0014\b\u0002\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00112\u0016\b\u0002\u0010\u001f\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010 \u0012\u0004\u0012\u00020\u00010\u00112\u0014\b\u0002\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00112\u0014\b\u0002\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u00112\u000e\b\u0002\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u000e\b\u0002\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0007\u001a\b\u0010#\u001a\u00020\u0001H\u0003\u001a\u0010\u0010$\u001a\u00020\u00012\u0006\u0010%\u001a\u00020\rH\u0003\u001a6\u0010&\u001a\u00020\u00012\u0006\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u0006\u001a\u00020\u00072\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a>\u0010\'\u001a\u00020\u00012\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00030)2\b\u0010*\u001a\u0004\u0018\u00010+2\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u0011H\u0003\u001a\u0017\u0010,\u001a\u00020\r2\b\u0010-\u001a\u0004\u0018\u00010.H\u0003\u00a2\u0006\u0002\u0010/\u001a\f\u00100\u001a\u00020\r*\u00020 H\u0003\u00a8\u00061"}, d2 = {"GemCard", "", "item", "Lcom/neil/trantools/data/gems/GemPoiWithDistance;", "isFavorite", "", "modifier", "Landroidx/compose/ui/Modifier;", "onToggleFavorite", "Lkotlin/Function0;", "onClick", "GemDetailRoute", "poiId", "", "onBack", "onOpenSettings", "onOpenWikiArticle", "Lkotlin/Function1;", "onOpenWikiQuestion", "onOpenChatQuestion", "GemDetailScreen", "poi", "Lcom/neil/trantools/data/gems/GemPoi;", "wikiContext", "Lcom/neil/trantools/data/gems/GemWikiContext;", "GemsRoute", "onOpenDetail", "GemsScreen", "uiState", "Lcom/neil/trantools/feature/gems/GemsUiState;", "onQueryChange", "onCategorySelect", "Lcom/neil/trantools/data/gems/GemCategory;", "onRequestLocationPermission", "onRefreshLocation", "GemsScreenPreview", "LabelBadge", "text", "LocationStatusCard", "NearbyMapCard", "pois", "", "center", "Lcom/neil/trantools/data/gems/GeoPoint;", "distanceLabel", "distanceMeters", "", "(Ljava/lang/Double;)Ljava/lang/String;", "displayLabel", "feature-gems_release"})
public final class GemsScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void GemsRoute(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOpenDetail) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void GemDetailRoute(@org.jetbrains.annotations.NotNull()
    java.lang.String poiId, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOpenWikiArticle, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOpenWikiQuestion, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOpenChatQuestion) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void GemsScreen(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.gems.GemsUiState uiState, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onQueryChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.neil.trantools.data.gems.GemCategory, kotlin.Unit> onCategorySelect, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onToggleFavorite, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOpenDetail, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRequestLocationPermission, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRefreshLocation) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void GemDetailScreen(@org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.gems.GemPoi poi, boolean isFavorite, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.gems.GemWikiContext wikiContext, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onToggleFavorite, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOpenWikiArticle, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOpenWikiQuestion, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOpenChatQuestion) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LocationStatusCard(com.neil.trantools.feature.gems.GemsUiState uiState, androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function0<kotlin.Unit> onRequestLocationPermission, kotlin.jvm.functions.Function0<kotlin.Unit> onRefreshLocation) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void NearbyMapCard(java.util.List<com.neil.trantools.data.gems.GemPoiWithDistance> pois, com.neil.trantools.data.gems.GeoPoint center, androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOpenDetail) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void GemCard(com.neil.trantools.data.gems.GemPoiWithDistance item, boolean isFavorite, androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function0<kotlin.Unit> onToggleFavorite, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LabelBadge(java.lang.String text) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final java.lang.String displayLabel(com.neil.trantools.data.gems.GemCategory $this$displayLabel) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final java.lang.String distanceLabel(java.lang.Double distanceMeters) {
        return null;
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true, heightDp = 850)
    @androidx.compose.runtime.Composable()
    private static final void GemsScreenPreview() {
    }
}