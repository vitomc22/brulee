package com.bruleetelie.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Stitch Brulee Telie Design Tokens
val BruleePrimary = Color(0xFF3B523E)
val BruleeOnPrimary = Color(0xFFFFFFFF)
val BruleePrimaryContainer = Color(0xFF526955)
val BruleeOnPrimaryContainer = Color(0xFFE6F3E5)

val BruleeSecondary = Color(0xFFB88C42)
val BruleeOnSecondary = Color(0xFFFFFFFF)
val BruleeSecondaryContainer = Color(0xFFE2BA73)
val BruleeOnSecondaryContainer = Color(0xFF3D2B07)

val BruleeTertiary = Color(0xFF705642)
val BruleeOnTertiary = Color(0xFFFFFFFF)
val BruleeTertiaryContainer = Color(0xFF9B7E67)
val BruleeOnTertiaryContainer = Color(0xFFFFF1E8)

val BruleeBackground = Color(0xFFFAF9F6)
val BruleeOnBackground = Color(0xFF1B1C1A)

val BruleeSurface = Color(0xFFFDF9F1)
val BruleeOnSurface = Color(0xFF1D1C1A)
val BruleeSurfaceVariant = Color(0xFFE3E2DF)
val BruleeOnSurfaceVariant = Color(0xFF4A4641)

val BruleeSurfaceContainerLowest = Color(0xFFFFFFFF)
val BruleeSurfaceContainerLow = Color(0xFFF7F3EB)
val BruleeSurfaceContainer = Color(0xFFF1EEE5)
val BruleeSurfaceContainerHigh = Color(0xFFEBE8DF)
val BruleeSurfaceContainerHighest = Color(0xFFE5E2D9)

val BruleeOutline = Color(0xFF7C7670)
val BruleeOutlineVariant = Color(0xFFCDC5BD)

val BruleeError = Color(0xFFBA1A1A)
val BruleeOnError = Color(0xFFFFFFFF)
val BruleeErrorContainer = Color(0xFFFFDAD6)
val BruleeOnErrorContainer = Color(0xFF410002)

// Fixed accents
val BruleePrimaryFixed = Color(0xFFCFEACF)
val BruleeSecondaryFixed = Color(0xFFFFDEAD)
val BruleeTertiaryFixed = Color(0xFFFFDCC3)

// Backward-compat aliases
val BruleeGreen = BruleePrimary
val BruleeGold = BruleeSecondary
val BruleeCream = BruleeSurface
val BruleeInk = BruleeOnSurface

private val BruleeColorScheme = lightColorScheme(
    primary = BruleePrimary,
    onPrimary = BruleeOnPrimary,
    primaryContainer = BruleePrimaryFixed,
    onPrimaryContainer = Color(0xFF0A2010),
    secondary = BruleeSecondary,
    onSecondary = BruleeOnSecondary,
    secondaryContainer = BruleeSecondaryContainer,
    onSecondaryContainer = BruleeOnSecondaryContainer,
    tertiary = BruleeTertiary,
    onTertiary = BruleeOnTertiary,
    tertiaryContainer = BruleeTertiaryContainer,
    onTertiaryContainer = BruleeOnTertiaryContainer,
    background = BruleeBackground,
    onBackground = BruleeOnBackground,
    surface = BruleeSurface,
    onSurface = BruleeOnSurface,
    surfaceVariant = BruleeSurfaceVariant,
    onSurfaceVariant = BruleeOnSurfaceVariant,
    surfaceContainerLowest = BruleeSurfaceContainerLowest,
    surfaceContainerLow = BruleeSurfaceContainerLow,
    surfaceContainer = BruleeSurfaceContainer,
    surfaceContainerHigh = BruleeSurfaceContainerHigh,
    surfaceContainerHighest = BruleeSurfaceContainerHighest,
    outline = BruleeOutline,
    outlineVariant = BruleeOutlineVariant,
    error = BruleeError,
    onError = BruleeOnError,
    errorContainer = BruleeErrorContainer,
    onErrorContainer = BruleeOnErrorContainer,
)

private val BruleeTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 56.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
    ),
)

@Composable
fun BruleeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BruleeColorScheme,
        typography = BruleeTypography,
        content = content,
    )
}
