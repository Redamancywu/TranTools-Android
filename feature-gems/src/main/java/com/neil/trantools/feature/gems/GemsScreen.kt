package com.neil.trantools.feature.gems

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.neil.trantools.core.ui.R
import com.neil.trantools.data.gems.GemCategory
import com.neil.trantools.data.gems.GemPoi
import com.neil.trantools.data.gems.GemPoiWithDistance
import com.neil.trantools.data.gems.GemsRepository
import com.neil.trantools.domain.gems.BuildGemWikiContextUseCase
import com.neil.trantools.ui.components.VoyagerTopBar
import com.neil.trantools.ui.theme.TranToolsTheme

@Composable
fun GemsRoute(
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit = {},
    onOpenDetail: (String) -> Unit = {},
) {
    val context = LocalContext.current
    val viewModel: GemsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onLocationPermissionChanged(granted)
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        viewModel.onLocationPermissionChanged(granted)
    }

    GemsScreen(
        uiState = uiState,
        modifier = modifier,
        onOpenSettings = onOpenSettings,
        onQueryChange = viewModel::setQuery,
        onCategorySelect = viewModel::setCategory,
        onToggleFavorite = viewModel::toggleFavorite,
        onOpenDetail = onOpenDetail,
        onRequestLocationPermission = {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        },
        onRefreshLocation = viewModel::refreshLocation
    )
}

@Composable
fun GemDetailRoute(
    poiId: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit = {},
    onOpenWikiArticle: (String) -> Unit = {},
    onOpenWikiQuestion: (String) -> Unit = {},
    onOpenChatQuestion: (String) -> Unit = {},
) {
    val context = LocalContext.current
    val viewModel: GemsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val poi = viewModel.findPoi(poiId)
    val buildGemWikiContext = remember(context) {
        BuildGemWikiContextUseCase(
            context = context,
            questionTemplate = { title ->
                context.getString(R.string.gems_wiki_question_template, title)
            },
            fallbackAnswer = context.getString(R.string.wiki_qa_no_match)
        )
    }
    val wikiContext = remember(poiId, poi) {
        poi?.let { buildGemWikiContext(it) }
    }

    GemDetailScreen(
        poi = poi,
        isFavorite = poi?.id in uiState.favorites,
        wikiContext = wikiContext,
        modifier = modifier,
        onBack = onBack,
        onOpenSettings = onOpenSettings,
        onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
        onOpenWikiArticle = onOpenWikiArticle,
        onOpenWikiQuestion = onOpenWikiQuestion,
        onOpenChatQuestion = onOpenChatQuestion
    )
}

@Composable
fun GemsScreen(
    uiState: GemsUiState,
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit = {},
    onQueryChange: (String) -> Unit = {},
    onCategorySelect: (GemCategory?) -> Unit = {},
    onToggleFavorite: (String) -> Unit = {},
    onOpenDetail: (String) -> Unit = {},
    onRequestLocationPermission: () -> Unit = {},
    onRefreshLocation: () -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { VoyagerTopBar(title = stringResource(R.string.nav_gems), onSettingsClick = onOpenSettings) }
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = stringResource(R.string.gems_screen_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = stringResource(R.string.gems_screen_subtitle),
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
                placeholder = { Text(stringResource(R.string.gems_search_placeholder)) },
                shape = RoundedCornerShape(20.dp),
                singleLine = true
            )
        }
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    FilterChip(
                        selected = uiState.selectedCategory == null,
                        onClick = { onCategorySelect(null) },
                        label = { Text(stringResource(R.string.gems_category_all)) }
                    )
                }
                items(GemCategory.entries) { category ->
                    FilterChip(
                        selected = uiState.selectedCategory == category,
                        onClick = { onCategorySelect(category) },
                        label = { Text(category.displayLabel()) }
                    )
                }
            }
        }
        item {
            LocationStatusCard(
                uiState = uiState,
                modifier = Modifier.padding(horizontal = 20.dp),
                onRequestLocationPermission = onRequestLocationPermission,
                onRefreshLocation = onRefreshLocation
            )
        }
        item {
            NearbyMapCard(
                pois = uiState.featuredPois,
                center = uiState.currentLocation,
                modifier = Modifier.padding(horizontal = 20.dp),
                onOpenDetail = onOpenDetail
            )
        }
        item {
            Text(
                text = stringResource(R.string.gems_nearby_list),
                modifier = Modifier.padding(horizontal = 20.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        if (uiState.visiblePois.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.gems_empty),
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(uiState.visiblePois, key = { it.poi.id }) { item ->
                GemCard(
                    item = item,
                    isFavorite = item.poi.id in uiState.favorites,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    onToggleFavorite = { onToggleFavorite(item.poi.id) },
                    onClick = { onOpenDetail(item.poi.id) }
                )
            }
        }
    }
}

@Composable
fun GemDetailScreen(
    poi: GemPoi?,
    isFavorite: Boolean,
    wikiContext: com.neil.trantools.data.gems.GemWikiContext?,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onToggleFavorite: (String) -> Unit = {},
    onOpenWikiArticle: (String) -> Unit = {},
    onOpenWikiQuestion: (String) -> Unit = {},
    onOpenChatQuestion: (String) -> Unit = {},
) {
    if (poi == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.gems_not_found))
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 24.dp),
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
                        text = stringResource(R.string.gems_detail_title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onToggleFavorite(poi.id) }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = stringResource(R.string.gems_open_favorite),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(R.string.action_settings),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        LabelBadge(poi.category.displayLabel())
                        LabelBadge(poi.address)
                    }
                    Text(
                        text = poi.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = poi.summary,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Card(
                        onClick = { onToggleFavorite(poi.id) },
                        shape = RoundedCornerShape(999.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = if (isFavorite) stringResource(R.string.gems_saved) else stringResource(R.string.gems_open_favorite),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
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
                    Text(
                        text = stringResource(R.string.gems_ai_tip),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = poi.tip,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        wikiContext?.let { context ->
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.gems_wiki_context_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.gems_wiki_context_desc),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = context.answer.answer,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(onClick = { onOpenWikiQuestion(context.question) }) {
                                Text(stringResource(R.string.gems_open_wiki_qa))
                            }
                            Button(onClick = { onOpenChatQuestion(context.question) }) {
                                Text(stringResource(R.string.gems_open_chat))
                            }
                        }
                        if (context.sourceArticles.isNotEmpty()) {
                            Text(
                                text = stringResource(R.string.gems_related_articles),
                                fontWeight = FontWeight.Bold
                            )
                            context.sourceArticles.forEach { article ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onOpenWikiArticle(article.id) }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Outlined.Place, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(Modifier.width(8.dp))
                                    Text(article.title, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(poi.tags) { tag ->
                    LabelBadge(tag)
                }
            }
        }
    }
}

@Composable
private fun LocationStatusCard(
    uiState: GemsUiState,
    modifier: Modifier = Modifier,
    onRequestLocationPermission: () -> Unit,
    onRefreshLocation: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.MyLocation, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(
                    text = uiState.currentLocation?.label ?: stringResource(R.string.gems_location_demo),
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = if (uiState.isRefreshingLocation) {
                    stringResource(R.string.gems_location_refreshing)
                } else if (uiState.currentLocation?.isFallback == true) {
                    stringResource(R.string.gems_location_using_demo)
                } else {
                    stringResource(R.string.gems_location_device)
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (!uiState.hasLocationPermission) {
                Text(
                    text = stringResource(R.string.gems_location_permission_required),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(onClick = onRequestLocationPermission) {
                    Text(stringResource(R.string.gems_grant_location))
                }
            } else {
                Button(onClick = onRefreshLocation, enabled = !uiState.isRefreshingLocation) {
                    Text(stringResource(R.string.gems_location_refresh))
                }
            }
        }
    }
}

@Composable
private fun NearbyMapCard(
    pois: List<GemPoiWithDistance>,
    center: com.neil.trantools.data.gems.GeoPoint?,
    modifier: Modifier = Modifier,
    onOpenDetail: (String) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.22f),
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.86f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.gems_nearby_map),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Canvas(modifier = Modifier.fillMaxSize()) {
                val current = center ?: return@Canvas
                val visible = pois.map { it.poi }
                val scale = 0.05
                visible.forEach { poi ->
                    val dx = (((poi.longitude - current.longitude) / scale) + 0.5).coerceIn(0.1, 0.9).toFloat()
                    val dy = (((current.latitude - poi.latitude) / scale) + 0.5).coerceIn(0.18, 0.88).toFloat()
                    drawCircle(
                        color = Color(0xFF73F1E7),
                        radius = 12.dp.toPx(),
                        center = Offset(size.width * dx, size.height * dy)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pois.forEach { item ->
                    Row(
                        modifier = Modifier.clickable { onOpenDetail(item.poi.id) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(Color(0xFF73F1E7), CircleShape)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(item.poi.title, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun GemCard(
    item: GemPoiWithDistance,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
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
                    Text(item.poi.address, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
                }
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = stringResource(R.string.gems_open_favorite),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Text(item.poi.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
            Text(item.poi.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                LabelBadge(item.poi.category.displayLabel())
                Text(distanceLabel(item.distanceMeters), color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(item.poi.rating.toString(), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
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
private fun GemCategory.displayLabel(): String {
    return when (this) {
        GemCategory.Food -> stringResource(R.string.gems_category_food)
        GemCategory.Culture -> stringResource(R.string.gems_category_culture)
        GemCategory.Nature -> stringResource(R.string.gems_category_nature)
        GemCategory.Viewpoint -> stringResource(R.string.gems_category_viewpoint)
    }
}

@Composable
private fun distanceLabel(distanceMeters: Double?): String {
    val safeDistance = distanceMeters ?: return ""
    return if (safeDistance < 1000) {
        stringResource(R.string.gems_distance_meters, safeDistance)
    } else {
        stringResource(R.string.gems_distance_km, safeDistance / 1000.0)
    }
}

@Preview(showBackground = true, heightDp = 850)
@Composable
private fun GemsScreenPreview() {
    TranToolsTheme {
        GemsScreen(
            uiState = GemsUiState(
                allPois = GemsRepository.loadPois(LocalContext.current),
                favorites = setOf("kiyomizu-dera"),
                currentLocation = com.neil.trantools.data.gems.GeoPoint(
                    latitude = 35.0116,
                    longitude = 135.7681,
                    label = "Kyoto demo center",
                    isFallback = true
                )
            )
        )
    }
}
