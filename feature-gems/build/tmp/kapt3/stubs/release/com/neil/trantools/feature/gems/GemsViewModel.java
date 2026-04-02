package com.neil.trantools.feature.gems;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import com.neil.trantools.core.ui.R;
import com.neil.trantools.data.gems.GemCategory;
import com.neil.trantools.data.gems.GemPoi;
import com.neil.trantools.data.gems.GeoPoint;
import com.neil.trantools.data.gems.GemsPreferencesStore;
import com.neil.trantools.data.gems.GemsRepository;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\n\b\u0007\u0018\u0000 )2\u00020\u0001:\u0001)B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u001d\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001e\u001a\u00020\u0015J\u000e\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u0012J\u0006\u0010\"\u001a\u00020 J\u0010\u0010#\u001a\u00020 2\b\u0010$\u001a\u0004\u0018\u00010\u0017J\u000e\u0010%\u001a\u00020 2\u0006\u0010&\u001a\u00020\u0015J\u000e\u0010\'\u001a\u00020 2\u0006\u0010(\u001a\u00020\u0015R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00120\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0016\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00170\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001c\u00a8\u0006*"}, d2 = {"Lcom/neil/trantools/feature/gems/GemsViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "allPois", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/neil/trantools/data/gems/GemPoi;", "appContext", "Landroid/content/Context;", "kotlin.jvm.PlatformType", "currentLocation", "Lcom/neil/trantools/data/gems/GeoPoint;", "filterState", "Lkotlinx/coroutines/flow/Flow;", "Lcom/neil/trantools/feature/gems/GemsFilterState;", "hasLocationPermission", "", "isRefreshingLocation", "query", "", "selectedCategory", "Lcom/neil/trantools/data/gems/GemCategory;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/neil/trantools/feature/gems/GemsUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "findPoi", "id", "onLocationPermissionChanged", "", "granted", "refreshLocation", "setCategory", "category", "setQuery", "value", "toggleFavorite", "poiId", "Companion", "feature-gems_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class GemsViewModel extends androidx.lifecycle.AndroidViewModel {
    private final android.content.Context appContext = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.neil.trantools.data.gems.GemPoi>> allPois = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> query = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.neil.trantools.data.gems.GemCategory> selectedCategory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.neil.trantools.data.gems.GeoPoint> currentLocation = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> hasLocationPermission = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> isRefreshingLocation = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.neil.trantools.feature.gems.GemsFilterState> filterState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.neil.trantools.feature.gems.GemsUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.neil.trantools.feature.gems.GemsViewModel.Companion Companion = null;
    
    @javax.inject.Inject()
    public GemsViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.neil.trantools.feature.gems.GemsUiState> getUiState() {
        return null;
    }
    
    public final void setQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void setCategory(@org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.gems.GemCategory category) {
    }
    
    public final void onLocationPermissionChanged(boolean granted) {
    }
    
    public final void refreshLocation() {
    }
    
    public final void toggleFavorite(@org.jetbrains.annotations.NotNull()
    java.lang.String poiId) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.data.gems.GemPoi findPoi(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\u0003\u001a\u00020\u00042\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u0012\u0010\t\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002\u00a8\u0006\n"}, d2 = {"Lcom/neil/trantools/feature/gems/GemsViewModel$Companion;", "", "()V", "defaultFallbackLocation", "Lcom/neil/trantools/data/gems/GeoPoint;", "context", "Landroid/content/Context;", "hasLocationPermission", "", "lastKnownLocation", "feature-gems_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        private final boolean hasLocationPermission(android.content.Context context) {
            return false;
        }
        
        private final com.neil.trantools.data.gems.GeoPoint lastKnownLocation(android.content.Context context) {
            return null;
        }
        
        private final com.neil.trantools.data.gems.GeoPoint defaultFallbackLocation(android.content.Context context) {
            return null;
        }
    }
}