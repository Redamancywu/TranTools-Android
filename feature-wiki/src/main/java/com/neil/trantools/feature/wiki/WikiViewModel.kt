package com.neil.trantools.feature.wiki

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neil.trantools.core.ui.R
import com.neil.trantools.data.wiki.WikiAnswerEngine
import com.neil.trantools.data.wiki.WikiArticle
import com.neil.trantools.data.wiki.WikiCategory
import com.neil.trantools.data.wiki.WikiPreferencesStore
import com.neil.trantools.data.wiki.WikiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class WikiViewModel @Inject constructor(
    @ApplicationContext
    private val appContext: Context,
) : ViewModel() {
    private val allArticles = MutableStateFlow<List<WikiArticle>>(WikiRepository.loadArticles(appContext))
    private val query = MutableStateFlow("")
    private val question = MutableStateFlow("")
    private val answer = MutableStateFlow<com.neil.trantools.data.wiki.WikiAnswer?>(null)
    private val selectedCategory = MutableStateFlow<WikiCategory?>(null)

    private val preferenceState = combine(
        WikiPreferencesStore.observeFavoriteIds(appContext),
        WikiPreferencesStore.observeRecentIds(appContext)
    ) { favorites, recentIds ->
        favorites to recentIds
    }

    private val screenState = combine(
        query,
        question,
        answer,
        selectedCategory
    ) { currentQuery, currentQuestion, currentAnswer, category ->
        ScreenState(
            query = currentQuery,
            question = currentQuestion,
            answer = currentAnswer,
            category = category
        )
    }

    val uiState: StateFlow<WikiUiState> = combine(
        allArticles,
        screenState,
        preferenceState
    ) { articles, screen, preferences ->
        WikiUiState(
            allArticles = articles,
            query = screen.query,
            question = screen.question,
            answer = screen.answer,
            selectedCategory = screen.category,
            favorites = preferences.first,
            recentIds = preferences.second
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WikiUiState()
    )

    fun setQuery(value: String) {
        query.value = value
    }

    fun setQuestion(value: String) {
        question.value = value
        if (value.isBlank()) {
            answer.value = null
        }
    }

    fun setCategory(category: WikiCategory?) {
        selectedCategory.value = category
    }

    fun askQuestion() {
        viewModelScope.launch {
            val indexedArticles = WikiRepository.searchIndexedArticles(
                context = appContext,
                query = question.value,
                limit = 6
            )
            answer.value = WikiAnswerEngine.answer(
                question = question.value,
                articles = indexedArticles.ifEmpty { allArticles.value },
                fallbackAnswer = appContext.getString(R.string.wiki_qa_no_match)
            )
        }
    }

    fun applyQuestion(value: String, autoAsk: Boolean) {
        question.value = value
        if (autoAsk) {
            askQuestion()
        } else if (value.isBlank()) {
            answer.value = null
        }
    }

    fun toggleFavorite(articleId: String) {
        viewModelScope.launch {
            WikiPreferencesStore.toggleFavorite(appContext, articleId)
        }
    }

    fun markViewed(articleId: String) {
        viewModelScope.launch {
            WikiPreferencesStore.addRecent(appContext, articleId)
        }
    }

    fun findArticle(articleId: String): WikiArticle? {
        return WikiRepository.findById(allArticles.value, articleId)
    }
}

private data class ScreenState(
    val query: String,
    val question: String,
    val answer: com.neil.trantools.data.wiki.WikiAnswer?,
    val category: WikiCategory?,
)
