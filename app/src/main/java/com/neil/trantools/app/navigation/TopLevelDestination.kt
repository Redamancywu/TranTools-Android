package com.neil.trantools.app.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.ui.graphics.vector.ImageVector
import com.neil.trantools.core.ui.R

data class TopLevelDestination(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    companion object {
        val Home = TopLevelDestination(
            route = "home",
            labelRes = R.string.nav_home,
            icon = Icons.Outlined.Home
        )
        val Translate = TopLevelDestination(
            route = "translate",
            labelRes = R.string.nav_translate,
            icon = Icons.Outlined.Translate
        )
        val Voice = TopLevelDestination(
            route = "voice",
            labelRes = R.string.nav_voice,
            icon = Icons.Outlined.RecordVoiceOver
        )
        val Wiki = TopLevelDestination(
            route = "wiki",
            labelRes = R.string.nav_wiki,
            icon = Icons.Outlined.AutoStories
        )
        val Gems = TopLevelDestination(
            route = "gems",
            labelRes = R.string.nav_gems,
            icon = Icons.Outlined.Explore
        )
    }
}

val topLevelDestinations = listOf(
    TopLevelDestination.Home,
    TopLevelDestination.Translate,
    TopLevelDestination.Voice,
    TopLevelDestination.Wiki,
    TopLevelDestination.Gems
)
