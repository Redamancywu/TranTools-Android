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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0014\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\b\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0016\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\tH\u00c6\u0003J\t\u0010\u0019\u001a\u00020\tH\u00c6\u0003J=\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\tH\u00c6\u0001J\u0013\u0010\u001b\u001a\u00020\t2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001d\u001a\u00020\u001eH\u00d6\u0001J\t\u0010\u001f\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\n\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0011\u00a8\u0006 "}, d2 = {"Lcom/neil/trantools/feature/gems/GemsFilterState;", "", "query", "", "category", "Lcom/neil/trantools/data/gems/GemCategory;", "location", "Lcom/neil/trantools/data/gems/GeoPoint;", "permission", "", "refreshing", "(Ljava/lang/String;Lcom/neil/trantools/data/gems/GemCategory;Lcom/neil/trantools/data/gems/GeoPoint;ZZ)V", "getCategory", "()Lcom/neil/trantools/data/gems/GemCategory;", "getLocation", "()Lcom/neil/trantools/data/gems/GeoPoint;", "getPermission", "()Z", "getQuery", "()Ljava/lang/String;", "getRefreshing", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "", "toString", "feature-gems_debug"})
final class GemsFilterState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String query = null;
    @org.jetbrains.annotations.Nullable()
    private final com.neil.trantools.data.gems.GemCategory category = null;
    @org.jetbrains.annotations.NotNull()
    private final com.neil.trantools.data.gems.GeoPoint location = null;
    private final boolean permission = false;
    private final boolean refreshing = false;
    
    public GemsFilterState(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.gems.GemCategory category, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.data.gems.GeoPoint location, boolean permission, boolean refreshing) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.data.gems.GemCategory getCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.data.gems.GeoPoint getLocation() {
        return null;
    }
    
    public final boolean getPermission() {
        return false;
    }
    
    public final boolean getRefreshing() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.data.gems.GemCategory component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.data.gems.GeoPoint component3() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.feature.gems.GemsFilterState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.gems.GemCategory category, @org.jetbrains.annotations.NotNull()
    com.neil.trantools.data.gems.GeoPoint location, boolean permission, boolean refreshing) {
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