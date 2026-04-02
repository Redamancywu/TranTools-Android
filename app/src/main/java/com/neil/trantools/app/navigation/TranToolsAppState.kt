package com.neil.trantools.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberTranToolsAppState(): TranToolsAppState {
    val navController = rememberNavController()
    return remember(navController) {
        TranToolsAppState(navController)
    }
}

@Stable
class TranToolsAppState(
    val navController: androidx.navigation.NavHostController,
) {
    @Composable
    fun currentDestination() = navController.currentBackStackEntryAsState().value?.destination

    @Composable
    fun shouldShowBottomBar(): Boolean {
        return topLevelDestinations.any { it.route == currentDestination()?.route }
    }

    fun navigateToTopLevel(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun isTopLevelSelected(destination: TopLevelDestination): Boolean {
        return navController.currentDestination
            ?.hierarchy
            ?.any { it.route == destination.route } == true
    }
}
