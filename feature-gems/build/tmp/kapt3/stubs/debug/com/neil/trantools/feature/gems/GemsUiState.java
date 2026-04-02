package com.neil.trantools.feature.gems;

import com.neil.trantools.data.gems.GemCategory;
import com.neil.trantools.data.gems.GemPoi;
import com.neil.trantools.data.gems.GemPoiWithDistance;
import com.neil.trantools.data.gems.GeoPoint;
import com.neil.trantools.data.gems.GemsRepository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B[\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u0012\b\b\u0002\u0010\r\u001a\u00020\u000e\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u000e\u00a2\u0006\u0002\u0010\u0010J\u000f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010#\u001a\u00020\u0006H\u00c6\u0003J\u000b\u0010$\u001a\u0004\u0018\u00010\bH\u00c6\u0003J\u000f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00060\nH\u00c6\u0003J\u000b\u0010&\u001a\u0004\u0018\u00010\fH\u00c6\u0003J\t\u0010\'\u001a\u00020\u000eH\u00c6\u0003J\t\u0010(\u001a\u00020\u000eH\u00c6\u0003J_\u0010)\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u000eH\u00c6\u0001J\u0013\u0010*\u001a\u00020\u000e2\b\u0010+\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010,\u001a\u00020-H\u00d6\u0001J\t\u0010.\u001a\u00020\u0006H\u00d6\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0012R\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u000f\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u001bR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0017\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00180\u00038F\u00a2\u0006\u0006\u001a\u0004\b!\u0010\u0012\u00a8\u0006/"}, d2 = {"Lcom/neil/trantools/feature/gems/GemsUiState;", "", "allPois", "", "Lcom/neil/trantools/data/gems/GemPoi;", "query", "", "selectedCategory", "Lcom/neil/trantools/data/gems/GemCategory;", "favorites", "", "currentLocation", "Lcom/neil/trantools/data/gems/GeoPoint;", "hasLocationPermission", "", "isRefreshingLocation", "(Ljava/util/List;Ljava/lang/String;Lcom/neil/trantools/data/gems/GemCategory;Ljava/util/Set;Lcom/neil/trantools/data/gems/GeoPoint;ZZ)V", "getAllPois", "()Ljava/util/List;", "getCurrentLocation", "()Lcom/neil/trantools/data/gems/GeoPoint;", "getFavorites", "()Ljava/util/Set;", "featuredPois", "Lcom/neil/trantools/data/gems/GemPoiWithDistance;", "getFeaturedPois", "getHasLocationPermission", "()Z", "getQuery", "()Ljava/lang/String;", "getSelectedCategory", "()Lcom/neil/trantools/data/gems/GemCategory;", "visiblePois", "getVisiblePois", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "feature-gems_debug"})
public final class GemsUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.neil.trantools.data.gems.GemPoi> allPois = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String query = null;
    @org.jetbrains.annotations.Nullable()
    private final com.neil.trantools.data.gems.GemCategory selectedCategory = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> favorites = null;
    @org.jetbrains.annotations.Nullable()
    private final com.neil.trantools.data.gems.GeoPoint currentLocation = null;
    private final boolean hasLocationPermission = false;
    private final boolean isRefreshingLocation = false;
    
    public GemsUiState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.gems.GemPoi> allPois, @org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.gems.GemCategory selectedCategory, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> favorites, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.gems.GeoPoint currentLocation, boolean hasLocationPermission, boolean isRefreshingLocation) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.gems.GemPoi> getAllPois() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.data.gems.GemCategory getSelectedCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> getFavorites() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.data.gems.GeoPoint getCurrentLocation() {
        return null;
    }
    
    public final boolean getHasLocationPermission() {
        return false;
    }
    
    public final boolean isRefreshingLocation() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.gems.GemPoiWithDistance> getVisiblePois() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.gems.GemPoiWithDistance> getFeaturedPois() {
        return null;
    }
    
    public GemsUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.gems.GemPoi> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.data.gems.GemCategory component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.data.gems.GeoPoint component5() {
        return null;
    }
    
    public final boolean component6() {
        return false;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.feature.gems.GemsUiState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.gems.GemPoi> allPois, @org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.gems.GemCategory selectedCategory, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> favorites, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.gems.GeoPoint currentLocation, boolean hasLocationPermission, boolean isRefreshingLocation) {
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