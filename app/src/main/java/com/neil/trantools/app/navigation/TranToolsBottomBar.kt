package com.neil.trantools.app.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy

@Composable
fun TranToolsBottomBar(
    appState: TranToolsAppState,
) {
    if (!appState.shouldShowBottomBar()) return

    NavigationBar {
        val currentDestination = appState.currentDestination()
        topLevelDestinations.forEach { destination ->
            val selected = currentDestination
                ?.hierarchy
                ?.any { it.route == destination.route } == true

            NavigationBarItem(
                selected = selected,
                onClick = { appState.navigateToTopLevel(destination.route) },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(destination.labelRes)
                    )
                },
                label = { Text(text = stringResource(destination.labelRes)) }
            )
        }
    }
}
