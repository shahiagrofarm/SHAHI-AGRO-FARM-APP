package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = ShahiForestGreen,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = Color(0xFFD7EED3),
    onPrimaryContainer = ShahiEmerald,
    secondary = ShahiLeafGreen,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = Color(0xFFE8F5E9),
    onSecondaryContainer = Color(0xFF0F3815),
    tertiary = ShahiGold,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    tertiaryContainer = ShahiGoldContainer,
    onTertiaryContainer = Color(0xFF4A3400),
    background = ShahiLightBg,
    onBackground = ShahiLightTextPrimary,
    surface = ShahiLightSurface,
    onSurface = ShahiLightTextPrimary,
    surfaceVariant = ShahiLightSurfaceVariant,
    onSurfaceVariant = ShahiLightTextSecondary,
    outline = ShahiLightOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = ShahiDarkGreen,
    onPrimary = Color(0xFF003912),
    primaryContainer = ShahiDarkGreenContainer,
    onPrimaryContainer = Color(0xFFC8E6C9),
    secondary = Color(0xFF81C784),
    onSecondary = Color(0xFF003811),
    secondaryContainer = Color(0xFF1B4021),
    onSecondaryContainer = Color(0xFFD4EED8),
    tertiary = Color(0xFFFFD54F),
    onTertiary = Color(0xFF432C00),
    tertiaryContainer = Color(0xFF5F4200),
    onTertiaryContainer = Color(0xFFFFE082),
    background = ShahiDarkBg,
    onBackground = ShahiDarkTextPrimary,
    surface = ShahiDarkSurface,
    onSurface = ShahiDarkTextPrimary,
    surfaceVariant = ShahiDarkSurfaceVariant,
    onSurfaceVariant = ShahiDarkTextSecondary,
    outline = Color(0xFF384D3E)
)

@Composable
fun ShahiAgroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false to prioritize authentic brand green palette
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
