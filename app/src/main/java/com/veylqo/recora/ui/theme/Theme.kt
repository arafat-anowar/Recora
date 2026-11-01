package com.veylqo.recora.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = CosmicPrimary,
    onPrimary = CosmicOnPrimary,
    primaryContainer = CosmicPrimaryContainer,
    background = CosmicBackground,
    surface = CosmicSurface,
    surfaceVariant = CosmicSurfaceVariant,
    onBackground = CosmicTextPrimary,
    onSurface = CosmicTextPrimary,
    onSurfaceVariant = CosmicTextSecondary,
    outline = CosmicCardBorder
)

@Composable
fun RecoraTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
