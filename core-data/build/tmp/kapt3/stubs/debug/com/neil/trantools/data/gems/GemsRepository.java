package com.neil.trantools.data.gems;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J(\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\tH\u0002J\u001e\u0010\u000e\u001a\u0004\u0018\u00010\u00072\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u0010\u001a\u00020\u0005J\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u0015\u001a\u00020\u0005H\u0002J6\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u00062\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u0018\u001a\u00020\u00052\b\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cR \u0010\u0003\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/neil/trantools/data/gems/GemsRepository;", "", "()V", "cache", "", "", "", "Lcom/neil/trantools/data/gems/GemPoi;", "distanceMeters", "", "fromLatitude", "fromLongitude", "toLatitude", "toLongitude", "findById", "pois", "id", "loadPois", "context", "Landroid/content/Context;", "parsePois", "json", "search", "Lcom/neil/trantools/data/gems/GemPoiWithDistance;", "query", "category", "Lcom/neil/trantools/data/gems/GemCategory;", "currentLocation", "Lcom/neil/trantools/data/gems/GeoPoint;", "core-data_debug"})
public final class GemsRepository {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Map<java.lang.String, java.util.List<com.neil.trantools.data.gems.GemPoi>> cache = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.neil.trantools.data.gems.GemsRepository INSTANCE = null;
    
    private GemsRepository() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.gems.GemPoi> loadPois(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.gems.GemPoiWithDistance> search(@org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.gems.GemPoi> pois, @org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.gems.GemCategory category, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.gems.GeoPoint currentLocation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.data.gems.GemPoi findById(@org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.gems.GemPoi> pois, @org.jetbrains.annotations.NotNull()
    java.lang.String id) {
        return null;
    }
    
    private final java.util.List<com.neil.trantools.data.gems.GemPoi> parsePois(java.lang.String json) {
        return null;
    }
    
    private final double distanceMeters(double fromLatitude, double fromLongitude, double toLatitude, double toLongitude) {
        return 0.0;
    }
}