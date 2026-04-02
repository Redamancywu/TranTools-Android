package com.neil.trantools.feature.wiki;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.CardDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.tooling.preview.Preview;
import com.neil.trantools.core.ui.R;
import com.neil.trantools.data.wiki.WikiArticle;
import com.neil.trantools.data.wiki.WikiCategory;
import com.neil.trantools.data.wiki.WikiRepository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000X\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\"\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001aB\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a*\u0010\u000b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a\u0010\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000eH\u0003\u001a|\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u000e2\b\u0010\u0011\u001a\u0004\u0018\u00010\u000e2\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00030\u00132\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u00152\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u00152\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0003\u001a4\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a\"\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0003\u001a8\u0010\u001e\u001a\u00020\u00012\u0006\u0010\u001f\u001a\u00020\u000e2\b\b\u0002\u0010\u0006\u001a\u00020\u00072\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u000e\b\u0002\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0007\u001a`\u0010\"\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u000e0$2\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u000e\b\u0002\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u000e\b\u0002\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u0014\b\u0002\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u0015H\u0007\u001a\b\u0010%\u001a\u00020\u0001H\u0003\u001aj\u0010&\u001a\u00020\u00012\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u000e\b\u0002\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u0014\b\u0002\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u00152\u0014\b\u0002\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u00152\n\b\u0002\u0010\'\u001a\u0004\u0018\u00010\u000e2\u000e\b\u0002\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0007\u001a\u00c0\u0001\u0010)\u001a\u00020\u00012\u0006\u0010*\u001a\u00020+2\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u000e\b\u0002\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u0014\b\u0002\u0010,\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u00152\u0014\b\u0002\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u00152\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\u0014\b\u0002\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u00152\u0016\b\u0002\u0010-\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010.\u0012\u0004\u0012\u00020\u00010\u00152\u0014\b\u0002\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u00152\u0014\b\u0002\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\u0015H\u0007\u001a\b\u0010/\u001a\u00020\u0001H\u0003\u001a\f\u00100\u001a\u00020\u000e*\u00020.H\u0003\u00a8\u00061"}, d2 = {"ArticleCard", "", "article", "Lcom/neil/trantools/data/wiki/WikiArticle;", "isFavorite", "", "modifier", "Landroidx/compose/ui/Modifier;", "onToggleFavorite", "Lkotlin/Function0;", "onClick", "FeaturedFactCard", "LabelBadge", "text", "", "LocalAnswerCard", "question", "answer", "sourceArticles", "", "onQuestionChange", "Lkotlin/Function1;", "onAskQuestion", "onOpenChat", "onOpenArticle", "RecentArticleCard", "SectionTitle", "title", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "WikiDetailRoute", "articleId", "onBack", "onOpenSettings", "WikiDetailScreen", "favorites", "", "WikiDetailScreenPreview", "WikiRoute", "prefillQuestion", "onPrefillConsumed", "WikiScreen", "uiState", "Lcom/neil/trantools/feature/wiki/WikiUiState;", "onQueryChange", "onCategorySelect", "Lcom/neil/trantools/data/wiki/WikiCategory;", "WikiScreenPreview", "displayLabel", "feature-wiki_release"})
public final class WikiScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void WikiRoute(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOpenArticle, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOpenChat, @org.jetbrains.annotations.Nullable()
    java.lang.String prefillQuestion, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onPrefillConsumed) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void WikiDetailRoute(@org.jetbrains.annotations.NotNull()
    java.lang.String articleId, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void WikiScreen(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.wiki.WikiUiState uiState, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onQueryChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onQuestionChange, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onAskQuestion, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOpenChat, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.neil.trantools.data.wiki.WikiCategory, kotlin.Unit> onCategorySelect, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onToggleFavorite, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOpenArticle) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void WikiDetailScreen(@org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.wiki.WikiArticle article, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> favorites, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onToggleFavorite) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SectionTitle(java.lang.String title, androidx.compose.ui.graphics.vector.ImageVector icon, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FeaturedFactCard(com.neil.trantools.data.wiki.WikiArticle article, androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LocalAnswerCard(java.lang.String question, java.lang.String answer, java.util.List<com.neil.trantools.data.wiki.WikiArticle> sourceArticles, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onQuestionChange, kotlin.jvm.functions.Function0<kotlin.Unit> onAskQuestion, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOpenChat, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOpenArticle, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void RecentArticleCard(com.neil.trantools.data.wiki.WikiArticle article, boolean isFavorite, kotlin.jvm.functions.Function0<kotlin.Unit> onToggleFavorite, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ArticleCard(com.neil.trantools.data.wiki.WikiArticle article, boolean isFavorite, androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function0<kotlin.Unit> onToggleFavorite, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LabelBadge(java.lang.String text) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final java.lang.String displayLabel(com.neil.trantools.data.wiki.WikiCategory $this$displayLabel) {
        return null;
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true, heightDp = 850)
    @androidx.compose.runtime.Composable()
    private static final void WikiScreenPreview() {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true, heightDp = 850)
    @androidx.compose.runtime.Composable()
    private static final void WikiDetailScreenPreview() {
    }
}