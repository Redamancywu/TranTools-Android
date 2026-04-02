package com.neil.trantools.data.chat;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002Jf\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\b2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\b2\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0006J\u0010\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\fH\u0002J\u0010\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u000eH\u0002J\u0010\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0018\u001a\u00020\nH\u0002J&\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00062\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00060\u001d2\u0006\u0010\u001e\u001a\u00020\u0006H\u0002\u00a8\u0006\u001f"}, d2 = {"Lcom/neil/trantools/data/chat/LocalAssistantEngine;", "", "()V", "answer", "Lcom/neil/trantools/data/chat/LocalAssistantAnswer;", "question", "", "previousUserTurns", "", "wikiArticles", "Lcom/neil/trantools/data/wiki/WikiArticle;", "gems", "Lcom/neil/trantools/data/gems/GemPoi;", "history", "Lcom/neil/trantools/data/history/TranslationHistoryEntity;", "fallbackAnswer", "translateHistoryLabel", "photoHistoryLabel", "voiceHistoryLabel", "buildGemHaystack", "gem", "buildHistoryHaystack", "item", "buildWikiHaystack", "article", "score", "", "haystack", "keywords", "", "originalQuestion", "core-data_debug"})
public final class LocalAssistantEngine {
    @org.jetbrains.annotations.NotNull()
    public static final com.neil.trantools.data.chat.LocalAssistantEngine INSTANCE = null;
    
    private LocalAssistantEngine() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.neil.trantools.data.chat.LocalAssistantAnswer answer(@org.jetbrains.annotations.NotNull()
    java.lang.String question, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> previousUserTurns, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.wiki.WikiArticle> wikiArticles, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.gems.GemPoi> gems, @org.jetbrains.annotations.NotNull()
    java.util.List<com.neil.trantools.data.history.TranslationHistoryEntity> history, @org.jetbrains.annotations.NotNull()
    java.lang.String fallbackAnswer, @org.jetbrains.annotations.NotNull()
    java.lang.String translateHistoryLabel, @org.jetbrains.annotations.NotNull()
    java.lang.String photoHistoryLabel, @org.jetbrains.annotations.NotNull()
    java.lang.String voiceHistoryLabel) {
        return null;
    }
    
    private final java.lang.String buildWikiHaystack(com.neil.trantools.data.wiki.WikiArticle article) {
        return null;
    }
    
    private final java.lang.String buildGemHaystack(com.neil.trantools.data.gems.GemPoi gem) {
        return null;
    }
    
    private final java.lang.String buildHistoryHaystack(com.neil.trantools.data.history.TranslationHistoryEntity item) {
        return null;
    }
    
    private final int score(java.lang.String haystack, java.util.Set<java.lang.String> keywords, java.lang.String originalQuestion) {
        return 0;
    }
}