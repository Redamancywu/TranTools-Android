package com.neil.trantools.feature.wiki;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.neil.trantools.core.ui.R;
import com.neil.trantools.data.wiki.WikiAnswerEngine;
import com.neil.trantools.data.wiki.WikiArticle;
import com.neil.trantools.data.wiki.WikiCategory;
import com.neil.trantools.data.wiki.WikiPreferencesStore;
import com.neil.trantools.data.wiki.WikiRepository;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\n\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\u001fJ\u0006\u0010 \u001a\u00020\u001cJ\u0010\u0010!\u001a\u0004\u0018\u00010\b2\u0006\u0010\"\u001a\u00020\u000fJ\u000e\u0010#\u001a\u00020\u001c2\u0006\u0010\"\u001a\u00020\u000fJ\u0010\u0010$\u001a\u00020\u001c2\b\u0010%\u001a\u0004\u0018\u00010\u0015J\u000e\u0010&\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u000fJ\u000e\u0010\'\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u000fJ\u000e\u0010(\u001a\u00020\u001c2\u0006\u0010\"\u001a\u00020\u000fR\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R,\u0010\u000b\u001a \u0012\u001c\u0012\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00070\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001a\u00a8\u0006)"}, d2 = {"Lcom/neil/trantools/feature/wiki/WikiViewModel;", "Landroidx/lifecycle/ViewModel;", "appContext", "Landroid/content/Context;", "(Landroid/content/Context;)V", "allArticles", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/neil/trantools/data/wiki/WikiArticle;", "answer", "Lcom/neil/trantools/data/wiki/WikiAnswer;", "preferenceState", "Lkotlinx/coroutines/flow/Flow;", "Lkotlin/Pair;", "", "", "query", "question", "screenState", "Lcom/neil/trantools/feature/wiki/ScreenState;", "selectedCategory", "Lcom/neil/trantools/data/wiki/WikiCategory;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/neil/trantools/feature/wiki/WikiUiState;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "applyQuestion", "", "value", "autoAsk", "", "askQuestion", "findArticle", "articleId", "markViewed", "setCategory", "category", "setQuery", "setQuestion", "toggleFavorite", "feature-wiki_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class WikiViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context appContext = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.neil.trantools.data.wiki.WikiArticle>> allArticles = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> query = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> question = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.neil.trantools.data.wiki.WikiAnswer> answer = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.neil.trantools.data.wiki.WikiCategory> selectedCategory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<kotlin.Pair<java.util.Set<java.lang.String>, java.util.List<java.lang.String>>> preferenceState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.neil.trantools.feature.wiki.ScreenState> screenState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.neil.trantools.feature.wiki.WikiUiState> uiState = null;
    
    @javax.inject.Inject()
    public WikiViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context appContext) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.neil.trantools.feature.wiki.WikiUiState> getUiState() {
        return null;
    }
    
    public final void setQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void setQuestion(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void setCategory(@org.jetbrains.annotations.Nullable()
    com.neil.trantools.data.wiki.WikiCategory category) {
    }
    
    public final void askQuestion() {
    }
    
    public final void applyQuestion(@org.jetbrains.annotations.NotNull()
    java.lang.String value, boolean autoAsk) {
    }
    
    public final void toggleFavorite(@org.jetbrains.annotations.NotNull()
    java.lang.String articleId) {
    }
    
    public final void markViewed(@org.jetbrains.annotations.NotNull()
    java.lang.String articleId) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.neil.trantools.data.wiki.WikiArticle findArticle(@org.jetbrains.annotations.NotNull()
    java.lang.String articleId) {
        return null;
    }
}