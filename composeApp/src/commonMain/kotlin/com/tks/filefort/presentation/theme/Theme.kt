package com.tks.filefort.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light theme colors
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF667eea),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFe8eaf6),
    onPrimaryContainer = Color(0xFF1a237e),
    secondary = Color(0xFF764ba2),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFe1bee7),
    onSecondaryContainer = Color(0xFF4a148c),
    tertiary = Color(0xFFff7043),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFffe0d6),
    onTertiaryContainer = Color(0xFFbf360c),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410E0B),
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0)
)

// Dark theme colors
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9FA8DA),
    onPrimary = Color(0xFF000051),
    primaryContainer = Color(0xFF303F9F),
    onPrimaryContainer = Color(0xFFE8EAF6),
    secondary = Color(0xFFCE93D8),
    onSecondary = Color(0xFF2E0854),
    secondaryContainer = Color(0xFF7B1FA2),
    onSecondaryContainer = Color(0xFFE1BEE7),
    tertiary = Color(0xFFFFAB91),
    onTertiary = Color(0xFF5D1A00),
    tertiaryContainer = Color(0xFFD84315),
    onTertiaryContainer = Color(0xFFFFE0D6),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F)
)

@Composable
fun FileFortTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}