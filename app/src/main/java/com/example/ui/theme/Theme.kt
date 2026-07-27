package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val ImposterDarkColorScheme = darkColorScheme(
    primary = PrimaryAccent,
    onPrimary = TextPrimary,
    primaryContainer = DarkSurfaceVariant,
    onPrimaryContainer = PrimaryAccentLight,
    secondary = CyberCyan,
    onSecondary = DarkBackground,
    tertiary = GoldPrimary,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    error = ImposterRed,
    onError = TextPrimary,
    errorContainer = ImposterRedContainer,
    onErrorContainer = ImposterRed
)

@Composable
fun ImposterGameTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = ImposterDarkColorScheme,
            typography = Typography,
            content = content
        )
    }
}
