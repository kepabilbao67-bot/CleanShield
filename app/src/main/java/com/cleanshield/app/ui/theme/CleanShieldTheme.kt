package com.cleanshield.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

// CleanShield Brand Colors - Dark Theme with Green #00B894
val GreenPrimary = Color(0xFF00B894)
val GreenLight = Color(0xFF55EFC4)
val GreenDark = Color(0xFF009B77)
val DarkBackground = Color(0xFF0D1117)
val DarkSurface = Color(0xFF161B22)
val DarkSurfaceVariant = Color(0xFF21262D)
val DarkCard = Color(0xFF1C2128)
val AccentGold = Color(0xFFF9A825)
val AccentRed = Color(0xFFE74C3C)
val AccentOrange = Color(0xFFF39C12)
val AccentBlue = Color(0xFF3498DB)
val AccentPurple = Color(0xFF9B59B6)
val TextPrimary = Color(0xFFE6EDF3)
val TextSecondary = Color(0xFF8B949E)

private val CleanShieldDarkScheme = darkColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.Black,
    primaryContainer = GreenDark,
    onPrimaryContainer = GreenLight,
    secondary = AccentBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1A3A5C),
    onSecondaryContainer = Color(0xFFBBDEFB),
    tertiary = AccentPurple,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF3D2354),
    onTertiaryContainer = Color(0xFFE1BEE7),
    error = AccentRed,
    onError = Color.White,
    errorContainer = Color(0xFF5C1A1A),
    onErrorContainer = Color(0xFFFFCDD2),
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = Color(0xFF30363D)
)

private val CleanShieldTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)

@Composable
fun CleanShieldTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = CleanShieldDarkScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkBackground.toArgb()
            window.navigationBarColor = DarkBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CleanShieldTypography,
        content = content
    )
}

object CleanShieldColors {
    val GreenPrimary = Color(0xFF00B894)
    val GreenLight = Color(0xFF55EFC4)
    val GreenDark = Color(0xFF009B77)
    val StreakGold = Color(0xFFF9A825)
    val StreakGoldLight = Color(0xFFFFD95A)
    val StatusProtected = Color(0xFF00B894)
    val StatusWarning = Color(0xFFF39C12)
    val StatusDanger = Color(0xFFE74C3C)
    val StatusInactive = Color(0xFF636E72)
    val CardBackground = Color(0xFF1C2128)
    val DarkBackground = Color(0xFF0D1117)
    val AccentBlue = Color(0xFF3498DB)
    val AccentPurple = Color(0xFF9B59B6)
    val AccentOrange = Color(0xFFF39C12)
    val AccentRed = Color(0xFFE74C3C)
    val BlockedRedDark = Color(0xFF7F0000)
}
