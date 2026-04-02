package com.neil.trantools.data.wiki;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nJ\u001e\u0010\u000b\u001a\u0004\u0018\u00010\u00072\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\r\u001a\u00020\u0005J\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\t\u001a\u00020\nJ\u0016\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u0010\u001a\u00020\u0005H\u0002J,\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u0012\u001a\u00020\u00052\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014R \u0010\u0003\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/neil/trantools/data/wiki/WikiRepository;", "", "()V", "cache", "", "", "", "Lcom/neil/trantools/data/wiki/WikiArticle;", "currentLanguage", "context", "Landroid/content/Context;", "findById", "articles", "id", "loadArticles", "parseArticles", "json", "search", "query", "category", "Lcom/neil/trantools/data/wiki/WikiCategory;", "core-data_debug"})
public final class WikiRepository {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Map<java.lang.String, java.util.List<com.neil.trantools.data.wiki.WikiArticle>> cache = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.neil.trantools.data.wiki.WikiRepository INSTANCE = null;
    
    private WikiRepository() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.wiki.WikiArticle> loadArticles(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String currentLanguage(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.wiki.WikiArticle> search(@org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.wiki.WikiArticle> articles, @org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.wiki.WikiCategory category) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.data.wiki.WikiArticle findById(@org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.wiki.WikiArticle> articles, @org.jetbrains.annotations.NotNull()
    java.lang.String id) {
        return null;
    }
    
    private final java.util.List<com.neil.trantools.data.wiki.WikiArticle> parseArticles(java.lang.String json) {
        return null;
    }
}