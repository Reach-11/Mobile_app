package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CoralAccent,
    onPrimary = Color.White,
    primaryContainer = CoralPrimaryDark,
    onPrimaryContainer = Color.White,
    secondary = GoldenAmber,
    onSecondary = Color.Black,
    secondaryContainer = SlateNavySurface,
    onSecondaryContainer = Color.White,
    tertiary = EmeraldGreen,
    onTertiary = Color.White,
    background = BgDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = BorderDark,
    outlineVariant = SlateNavyBorder
)

private val LightColorScheme = lightColorScheme(
    primary = CoralPrimary,
    onPrimary = Color.White,
    primaryContainer = CoralPrimaryContainer,
    onPrimaryContainer = CoralPrimaryDark,
    secondary = GoldenAmber,
    onSecondary = Color.Black,
    secondaryContainer = GoldenAmberLight,
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = EmeraldGreen,
    onTertiary = Color.White,
    background = BgLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = BorderLight,
    outlineVariant = Color(0xFFCBD5E1)
)

@Composable
fun AuraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep intentional brand colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
