package com.neil.trantools.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private fun lightSchemeFor(style: AppThemeStyle): ColorScheme {
    return when (style) {
        AppThemeStyle.Mint -> {
            lightColorScheme(
                primary = VoyagerPrimary,
                onPrimary = VoyagerOnPrimary,
                primaryContainer = VoyagerPrimaryContainer,
                secondary = VoyagerSecondary,
                secondaryContainer = VoyagerSecondaryContainer,
                onSecondaryContainer = VoyagerOnSecondaryContainer,
                tertiary = VoyagerTertiary,
                tertiaryContainer = VoyagerTertiaryContainer,
                background = VoyagerBackground,
                surface = VoyagerSurface,
                surfaceVariant = VoyagerSurfaceVariant,
                surfaceContainer = VoyagerSurfaceContainer,
                surfaceContainerHigh = VoyagerSurfaceContainerHigh,
                surfaceContainerLow = VoyagerSurfaceContainerLow,
                surfaceContainerLowest = VoyagerSurfaceContainerLowest,
                onSurface = VoyagerOnSurface,
                onSurfaceVariant = VoyagerOnSurfaceVariant,
                outline = VoyagerOutline
            )
        }

        AppThemeStyle.Warm -> {
            lightColorScheme(
                primary = WarmPrimary,
                onPrimary = WarmOnPrimary,
                primaryContainer = WarmPrimaryContainer,
                secondary = WarmSecondary,
                secondaryContainer = WarmSecondaryContainer,
                onSecondaryContainer = WarmOnSecondaryContainer,
                tertiary = WarmTertiary,
                tertiaryContainer = WarmTertiaryContainer,
                background = WarmBackground,
                surface = WarmSurface,
                surfaceVariant = WarmSurfaceVariant,
                surfaceContainer = WarmSurfaceContainer,
                surfaceContainerHigh = WarmSurfaceContainerHigh,
                surfaceContainerLow = WarmSurfaceContainerLow,
                surfaceContainerLowest = WarmSurfaceContainerLowest,
                onSurface = WarmOnSurface,
                onSurfaceVariant = WarmOnSurfaceVariant,
                outline = WarmOutline
            )
        }
    }
}

private fun darkSchemeFor(style: AppThemeStyle): ColorScheme {
    return when (style) {
        AppThemeStyle.Mint -> {
            darkColorScheme(
                primary = VoyagerPrimaryContainer,
                onPrimary = VoyagerDarkBackground,
                secondary = VoyagerSecondaryContainer,
                tertiary = VoyagerTertiaryContainer,
                background = VoyagerDarkBackground,
                surface = VoyagerDarkSurface,
                surfaceContainer = VoyagerDarkSurfaceContainer,
                onSurface = VoyagerDarkOnSurface,
                onSurfaceVariant = VoyagerDarkOnSurfaceVariant,
                outline = VoyagerOutline
            )
        }

        AppThemeStyle.Warm -> {
            darkColorScheme(
                primary = WarmPrimaryContainer,
                onPrimary = WarmDarkBackground,
                secondary = WarmSecondaryContainer,
                tertiary = WarmTertiaryContainer,
                background = WarmDarkBackground,
                surface = WarmDarkSurface,
                surfaceContainer = WarmDarkSurfaceContainer,
                onSurface = WarmDarkOnSurface,
                onSurfaceVariant = WarmDarkOnSurfaceVariant,
                outline = WarmOutline
            )
        }
    }
}

@Composable
fun TranToolsTheme(
    themeStyle: AppThemeStyle = AppThemeStyle.Mint,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) darkSchemeFor(themeStyle) else lightSchemeFor(themeStyle)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
