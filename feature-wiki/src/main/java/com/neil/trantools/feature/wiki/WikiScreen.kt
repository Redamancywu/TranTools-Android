package com.neil.trantools.feature.wiki

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neil.trantools.core.ui.R
import com.neil.trantools.data.wiki.WikiArticle
import com.neil.trantools.data.wiki.WikiCategory
import com.neil.trantools.data.wiki.WikiAnswerSource
import com.neil.trantools.data.wiki.WikiRepository
import com.neil.trantools.ui.components.VoyagerTopBar
import com.neil.trantools.ui.theme.TranToolsTheme

@Composable
fun WikiRoute(
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit = {},
    onOpenArticle: (String) -> Unit = {},
    onOpenChat: (String) -> Unit = {},
    prefillQuestion: String? = null,
    onPrefillConsumed: () -> Unit = {},
) {
    val viewModel: WikiViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    androidx.compose.runtime.LaunchedEffect(prefillQuestion) {
        val question = prefillQuestion ?: return@LaunchedEffect
        viewModel.applyQuestion(question, autoAsk = true)
        onPrefillConsumed()
    }
    WikiScreen(
        uiState = uiState,
        modifier = modifier,
        onOpenSettings = onOpenSettings,
        onQueryChange = viewModel::setQuery,
        onQuestionChange = viewModel::setQuestion,
        onAskQuestion = viewModel::askQuestion,
        onUseSuggestedQuestion = { question ->
            viewModel.setQuestion(question)
            viewModel.askQuestion()
        },
        onOpenChat = onOpenChat,
        onCategorySelect = viewModel::setCategory,
        onToggleFavorite = viewModel::toggleFavorite,
        onOpenArticle = { articleId ->
            viewModel.markViewed(articleId)
            onOpenArticle(articleId)
        }
    )
}

@Composable
fun WikiDetailRoute(
    articleId: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit = {},
) {
    val viewModel: WikiViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val article = viewModel.findArticle(articleId)
    if (article != null) {
        androidx.compose.runtime.LaunchedEffect(articleId) {
            viewModel.markViewed(articleId)
        }
    }
    WikiDetailScreen(
        article = article,
        favorites = uiState.favorites,
        modifier = modifier,
        onBack = onBack,
        onOpenSettings = onOpenSettings,
        onToggleFavorite = { id -> viewModel.toggleFavorite(id) }
    )
}

@Composable
fun WikiScreen(
    uiState: WikiUiState,
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit = {},
    onQueryChange: (String) -> Unit = {},
    onQuestionChange: (String) -> Unit = {},
    onAskQuestion: () -> Unit = {},
    onUseSuggestedQuestion: (String) -> Unit = {},
    onOpenChat: (String) -> Unit = {},
    onCategorySelect: (WikiCategory?) -> Unit = {},
    onToggleFavorite: (String) -> Unit = {},
    onOpenArticle: (String) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 30.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { VoyagerTopBar(title = stringResource(R.string.wiki_title), onSettingsClick = onOpenSettings) }
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = stringResource(R.string.wiki_headline),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = stringResource(R.string.wiki_subtitle),
                    modifier = Modifier.padding(top = 6.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                leadingIcon = {
                    Icon(Icons.Outlined.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                },
                placeholder = { Text(stringResource(R.string.wiki_search_placeholder)) },
                shape = RoundedCornerShape(20.dp),
                singleLine = true
            )
        }
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    FilterChip(
                        selected = uiState.selectedCategory == null,
                        onClick = { onCategorySelect(null) },
                        label = { Text(stringResource(R.string.wiki_all)) }
                    )
                }
                items(WikiCategory.entries) { category ->
                    FilterChip(
                        selected = uiState.selectedCategory == category,
                        onClick = { onCategorySelect(category) },
                        label = { Text(category.displayLabel()) }
                    )
                }
            }
        }

        if (uiState.query.isBlank() && uiState.recentArticles.isNotEmpty()) {
            item {
                SectionTitle(
                    title = stringResource(R.string.wiki_recent_reads),
                    icon = Icons.Outlined.History,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.recentArticles) { article ->
                        RecentArticleCard(
                            article = article,
                            isFavorite = article.id in uiState.favorites,
                            onToggleFavorite = { onToggleFavorite(article.id) },
                            onClick = { onOpenArticle(article.id) }
                        )
                    }
                }
            }
        }

        if (uiState.query.isBlank() && uiState.favoriteArticles.isNotEmpty()) {
            item {
                SectionTitle(
                    title = stringResource(R.string.wiki_favorites),
                    icon = Icons.Filled.Star,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            items(uiState.favoriteArticles, key = { it.id }) { article ->
                ArticleCard(
                    article = article,
                    isFavorite = true,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    onToggleFavorite = { onToggleFavorite(article.id) },
                    onClick = { onOpenArticle(article.id) }
                )
            }
        }

        uiState.featuredArticle?.let { article ->
            if (uiState.query.isBlank()) {
                item {
                    FeaturedFactCard(
                        article = article,
                        modifier = Modifier.padding(horizontal = 20.dp),
                        onClick = { onOpenArticle(article.id) }
                    )
                }
            }
        }

        item {
            LocalAnswerCard(
                question = uiState.question,
                answer = uiState.answer?.answer,
                sources = uiState.answer?.sources.orEmpty(),
                suggestedQuestions = uiState.answer?.suggestedQuestions.orEmpty(),
                onQuestionChange = onQuestionChange,
                onAskQuestion = onAskQuestion,
                onUseSuggestedQuestion = onUseSuggestedQuestion,
                onOpenChat = onOpenChat,
                onOpenArticle = onOpenArticle,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        item {
            SectionTitle(
                title = if (uiState.query.isBlank()) {
                    stringResource(R.string.wiki_all_articles)
                } else {
                    stringResource(R.string.wiki_search_results)
                },
                icon = Icons.Outlined.AutoStories,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        if (uiState.articles.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.wiki_empty),
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(uiState.articles, key = { it.id }) { article ->
                ArticleCard(
                    article = article,
                    isFavorite = article.id in uiState.favorites,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    onToggleFavorite = { onToggleFavorite(article.id) },
                    onClick = { onOpenArticle(article.id) }
                )
            }
        }
    }
}

@Composable
fun WikiDetailScreen(
    article: WikiArticle?,
    favorites: Set<String>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onToggleFavorite: (String) -> Unit = {},
) {
    if (article == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.wiki_article_not_found))
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 30.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.action_back),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = stringResource(R.string.wiki_detail_title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onToggleFavorite(article.id) }) {
                        Icon(
                            imageVector = if (article.id in favorites) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = stringResource(R.string.wiki_favorite),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(R.string.wiki_settings),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        LabelBadge(article.category.displayLabel())
                        LabelBadge(article.place)
                    }
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = article.summary,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Card(
                            onClick = { onToggleFavorite(article.id) },
                            shape = RoundedCornerShape(999.dp),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (article.id in favorites) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = if (article.id in favorites) {
                                        stringResource(R.string.wiki_saved)
                                    } else {
                                        stringResource(R.string.wiki_save)
                                    },
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.wiki_quick_fact), color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = article.fact,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        items(article.content) { paragraph ->
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLowest)
            ) {
                Text(
                    text = paragraph,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        item {
            SectionTitle(title = stringResource(R.string.wiki_tags), icon = Icons.Outlined.Tag)
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(article.tags) { tag ->
                    LabelBadge(tag)
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun FeaturedFactCard(
    article: WikiArticle,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.wiki_featured_fact), color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = article.fact,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = article.title,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.88f)
            )
        }
    }
}

@Composable
private fun LocalAnswerCard(
    question: String,
    answer: String?,
    sources: List<WikiAnswerSource>,
    suggestedQuestions: List<String>,
    onQuestionChange: (String) -> Unit,
    onAskQuestion: () -> Unit,
    onUseSuggestedQuestion: (String) -> Unit,
    onOpenChat: (String) -> Unit,
    onOpenArticle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SectionTitle(
                title = stringResource(R.string.wiki_qa_title),
                icon = Icons.Outlined.Lightbulb
            )
            OutlinedTextField(
                value = question,
                onValueChange = onQuestionChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.wiki_qa_placeholder)) },
                shape = RoundedCornerShape(18.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(
                    onClick = onAskQuestion,
                    shape = RoundedCornerShape(999.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = stringResource(R.string.wiki_qa_action),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Card(
                    onClick = { if (question.isNotBlank()) onOpenChat(question) },
                    shape = RoundedCornerShape(999.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Text(
                        text = stringResource(R.string.wiki_continue_chat),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            if (answer.isNullOrBlank()) {
                Text(
                    text = stringResource(R.string.wiki_qa_empty),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = answer,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.wiki_detected_from_local),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                if (sources.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.wiki_qa_sources),
                        fontWeight = FontWeight.Bold
                    )
                    sources.forEach { source ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            onClick = { onOpenArticle(source.articleId) },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLowest)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Outlined.AutoStories, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text(source.title, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                                    Text(
                                        text = source.excerpt,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
                if (suggestedQuestions.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.wiki_qa_suggestions),
                        fontWeight = FontWeight.Bold
                    )
                    suggestedQuestions.forEach { suggested ->
                        Card(
                            onClick = { onUseSuggestedQuestion(suggested) },
                            shape = RoundedCornerShape(999.dp),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
                        ) {
                            Text(
                                text = suggested,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentArticleCard(
    article: WikiArticle,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.width(260.dp),
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LabelBadge(article.category.displayLabel())
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = stringResource(R.string.wiki_favorite),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Text(article.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(article.summary, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 3)
        }
    }
}

@Composable
private fun ArticleCard(
    article: WikiArticle,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onToggleFavorite: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Place, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(6.dp))
                    Text(article.place, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
                }
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = stringResource(R.string.wiki_favorite),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Text(article.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
            Text(article.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
            LabelBadge(article.category.displayLabel())
        }
    }
}

@Composable
private fun LabelBadge(text: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun WikiCategory.displayLabel(): String {
    return when (this) {
        WikiCategory.Food -> stringResource(R.string.wiki_category_food)
        WikiCategory.History -> stringResource(R.string.wiki_category_history)
        WikiCategory.Culture -> stringResource(R.string.wiki_category_culture)
        WikiCategory.Architecture -> stringResource(R.string.wiki_category_architecture)
    }
}

@Preview(showBackground = true, heightDp = 850)
@Composable
private fun WikiScreenPreview() {
    TranToolsTheme {
        WikiScreen(
            uiState = WikiUiState(
                allArticles = WikiRepository.loadArticles(LocalContext.current),
                favorites = setOf("taj-mahal"),
                recentIds = listOf("venice-canals", "ramen-basics")
            )
        )
    }
}

@Preview(showBackground = true, heightDp = 850)
@Composable
private fun WikiDetailScreenPreview() {
    TranToolsTheme {
        WikiDetailScreen(
            article = WikiRepository.loadArticles(LocalContext.current).firstOrNull { it.id == "taj-mahal" },
            favorites = setOf("taj-mahal")
        )
    }
}
