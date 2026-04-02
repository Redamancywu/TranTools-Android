package com.neil.trantools.feature.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.neil.trantools.core.ui.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Translate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {
    private val appContext = getApplication<Application>().applicationContext
    private val _uiState = MutableStateFlow(
        HomeUiState(
            title = appContext.getString(R.string.app_name),
            subtitle = appContext.getString(R.string.home_subtitle),
            quickActions = listOf(
                HomeQuickAction(
                    title = appContext.getString(R.string.home_photo_translate),
                    description = appContext.getString(R.string.home_photo_translate_desc),
                    icon = Icons.Outlined.PhotoCamera
                ),
                HomeQuickAction(
                    title = appContext.getString(R.string.home_quick_text),
                    description = appContext.getString(R.string.translate_text_headline),
                    icon = Icons.Outlined.Translate
                ),
                HomeQuickAction(
                    title = appContext.getString(R.string.nav_wiki),
                    description = appContext.getString(R.string.wiki_headline),
                    icon = Icons.Outlined.AutoStories
                ),
                HomeQuickAction(
                    title = appContext.getString(R.string.nav_gems),
                    description = appContext.getString(R.string.gems_screen_title),
                    icon = Icons.Outlined.Explore
                )
            ),
            recentItems = listOf(
                appContext.getString(R.string.home_recent_1),
                appContext.getString(R.string.home_recent_2),
                appContext.getString(R.string.home_recent_3)
            ),
            statusItems = listOf(
                appContext.getString(R.string.home_status_1),
                appContext.getString(R.string.home_status_2),
                appContext.getString(R.string.home_status_3)
            )
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}
