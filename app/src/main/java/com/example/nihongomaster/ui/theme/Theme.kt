package com.example.nihongomaster.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = LacquerRedDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF2A1215),
    onPrimaryContainer = Color(0xFFFFD8D8),
    secondary = SageGreenDark,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF162B3D),
    onSecondaryContainer = Color(0xFFCBE6FF),
    tertiary = OchreAmberDark,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF2E230B),
    onTertiaryContainer = Color(0xFFFFE08A),
    background = DarkBg,
    onBackground = TextPrimaryDark,
    surface = DarkCard,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkCardSubtle,
    onSurfaceVariant = TextMutedDark,
    outline = DarkBorderNatural,
    outlineVariant = DarkBorderSubtle
)

private val LightColorScheme = lightColorScheme(
    primary = LacquerRed,
    onPrimary = Color.White,
    primaryContainer = LacquerRedLight,
    onPrimaryContainer = LacquerRedHover,
    secondary = SageGreen,
    onSecondary = Color.White,
    secondaryContainer = SageGreenLight,
    onSecondaryContainer = Color(0xFF1E3A25),
    tertiary = OchreAmber,
    onTertiary = Color.White,
    tertiaryContainer = OchreAmberLight,
    onTertiaryContainer = Color(0xFF4A330A),
    background = WarmCreamBg,
    onBackground = TextPrimaryLight,
    surface = Color.White,
    onSurface = TextPrimaryLight,
    surfaceVariant = CardSubtle,
    onSurfaceVariant = TextMutedLight,
    outline = BorderNatural,
    outlineVariant = BorderSubtle
)

@Composable
fun NihongoMasterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
