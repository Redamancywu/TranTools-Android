package com.neil.trantools.feature.wiki;

import com.neil.trantools.data.wiki.WikiArticle;
import com.neil.trantools.data.wiki.WikiAnswer;
import com.neil.trantools.data.wiki.WikiCategory;
import com.neil.trantools.data.wiki.WikiRepository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\b \n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001Ba\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00060\r\u0012\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u00a2\u0006\u0002\u0010\u000fJ\u000f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\'\u001a\u00020\u0006H\u00c6\u0003J\u000b\u0010(\u001a\u0004\u0018\u00010\tH\u00c6\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\u000f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00060\rH\u00c6\u0003J\u000f\u0010+\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003H\u00c6\u0003Je\u0010,\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00060\r2\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003H\u00c6\u0001J\u0013\u0010-\u001a\u00020.2\b\u0010/\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00100\u001a\u000201H\u00d6\u0001J\t\u00102\u001a\u00020\u0006H\u00d6\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00040\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0011R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00040\u00038F\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0011R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00060\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0013\u0010\u001a\u001a\u0004\u0018\u00010\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001eR\u0017\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00040\u00038F\u00a2\u0006\u0006\u001a\u0004\b!\u0010\u0011R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0011R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$\u00a8\u00063"}, d2 = {"Lcom/neil/trantools/feature/wiki/WikiUiState;", "", "allArticles", "", "Lcom/neil/trantools/data/wiki/WikiArticle;", "query", "", "question", "answer", "Lcom/neil/trantools/data/wiki/WikiAnswer;", "selectedCategory", "Lcom/neil/trantools/data/wiki/WikiCategory;", "favorites", "", "recentIds", "(Ljava/util/List;Ljava/lang/String;Ljava/lang/String;Lcom/neil/trantools/data/wiki/WikiAnswer;Lcom/neil/trantools/data/wiki/WikiCategory;Ljava/util/Set;Ljava/util/List;)V", "getAllArticles", "()Ljava/util/List;", "getAnswer", "()Lcom/neil/trantools/data/wiki/WikiAnswer;", "articles", "getArticles", "favoriteArticles", "getFavoriteArticles", "getFavorites", "()Ljava/util/Set;", "featuredArticle", "getFeaturedArticle", "()Lcom/neil/trantools/data/wiki/WikiArticle;", "getQuery", "()Ljava/lang/String;", "getQuestion", "recentArticles", "getRecentArticles", "getRecentIds", "getSelectedCategory", "()Lcom/neil/trantools/data/wiki/WikiCategory;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "", "toString", "feature-wiki_release"})
public final class WikiUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.neil.trantools.data.wiki.WikiArticle> allArticles = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String query = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String question = null;
    @org.jetbrains.annotations.Nullable()
    private final com.neil.trantools.data.wiki.WikiAnswer answer = null;
    @org.jetbrains.annotations.Nullable()
    private final com.neil.trantools.data.wiki.WikiCategory selectedCategory = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> favorites = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> recentIds = null;
    
    public WikiUiState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.wiki.WikiArticle> allArticles, @org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    java.lang.String question, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.wiki.WikiAnswer answer, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.wiki.WikiCategory selectedCategory, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> favorites, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> recentIds) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.wiki.WikiArticle> getAllArticles() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getQuestion() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.data.wiki.WikiAnswer getAnswer() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.data.wiki.WikiCategory getSelectedCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> getFavorites() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getRecentIds() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.wiki.WikiArticle> getArticles() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.data.wiki.WikiArticle getFeaturedArticle() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.wiki.WikiArticle> getRecentArticles() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.wiki.WikiArticle> getFavoriteArticles() {
        return null;
    }
    
    public WikiUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.neil.trantools.data.wiki.WikiArticle> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.data.wiki.WikiAnswer component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.data.wiki.WikiCategory component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.feature.wiki.WikiUiState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.wiki.WikiArticle> allArticles, @org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    java.lang.String question, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.wiki.WikiAnswer answer, @org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.wiki.WikiCategory selectedCategory, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> favorites, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> recentIds) {
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