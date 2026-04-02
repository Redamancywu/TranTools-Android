package com.neil.trantools.data.wiki;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J&\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u0006J&\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00060\u000f2\u0006\u0010\u0010\u001a\u00020\u0006H\u0002\u00a8\u0006\u0011"}, d2 = {"Lcom/neil/trantools/data/wiki/WikiAnswerEngine;", "", "()V", "answer", "Lcom/neil/trantools/data/wiki/WikiAnswer;", "question", "", "articles", "", "Lcom/neil/trantools/data/wiki/WikiArticle;", "fallbackAnswer", "scoreArticle", "", "article", "keywords", "", "originalQuestion", "core-data_debug"})
public final class WikiAnswerEngine {
    @org.jetbrains.annotations.NotNull()
    public static final com.neil.trantools.data.wiki.WikiAnswerEngine INSTANCE = null;
    
    private WikiAnswerEngine() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.data.wiki.WikiAnswer answer(@org.jetbrains.annotations.NotNull()
    java.lang.String question, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.wiki.WikiArticle> articles, @org.jetbrains.annotations.NotNull()
    java.lang.String fallbackAnswer) {
        return null;
    }
    
    private final int scoreArticle(com.neil.trantools.data.wiki.WikiArticle article, java.util.Set<java.lang.String> keywords, java.lang.String originalQuestion) {
        return 0;
    }
}