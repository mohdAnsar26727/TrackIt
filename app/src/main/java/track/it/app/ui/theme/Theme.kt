package com.example.compose
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import track.it.app.ui.theme.AppTypography
import track.it.app.ui.theme.backgroundDark
import track.it.app.ui.theme.backgroundDarkHighContrast
import track.it.app.ui.theme.backgroundDarkMediumContrast
import track.it.app.ui.theme.backgroundLight
import track.it.app.ui.theme.backgroundLightHighContrast
import track.it.app.ui.theme.backgroundLightMediumContrast
import track.it.app.ui.theme.errorContainerDark
import track.it.app.ui.theme.errorContainerDarkHighContrast
import track.it.app.ui.theme.errorContainerDarkMediumContrast
import track.it.app.ui.theme.errorContainerLight
import track.it.app.ui.theme.errorContainerLightHighContrast
import track.it.app.ui.theme.errorContainerLightMediumContrast
import track.it.app.ui.theme.errorDark
import track.it.app.ui.theme.errorDarkHighContrast
import track.it.app.ui.theme.errorDarkMediumContrast
import track.it.app.ui.theme.errorLight
import track.it.app.ui.theme.errorLightHighContrast
import track.it.app.ui.theme.errorLightMediumContrast
import track.it.app.ui.theme.inverseOnSurfaceDark
import track.it.app.ui.theme.inverseOnSurfaceDarkHighContrast
import track.it.app.ui.theme.inverseOnSurfaceDarkMediumContrast
import track.it.app.ui.theme.inverseOnSurfaceLight
import track.it.app.ui.theme.inverseOnSurfaceLightHighContrast
import track.it.app.ui.theme.inverseOnSurfaceLightMediumContrast
import track.it.app.ui.theme.inversePrimaryDark
import track.it.app.ui.theme.inversePrimaryDarkHighContrast
import track.it.app.ui.theme.inversePrimaryDarkMediumContrast
import track.it.app.ui.theme.inversePrimaryLight
import track.it.app.ui.theme.inversePrimaryLightHighContrast
import track.it.app.ui.theme.inversePrimaryLightMediumContrast
import track.it.app.ui.theme.inverseSurfaceDark
import track.it.app.ui.theme.inverseSurfaceDarkHighContrast
import track.it.app.ui.theme.inverseSurfaceDarkMediumContrast
import track.it.app.ui.theme.inverseSurfaceLight
import track.it.app.ui.theme.inverseSurfaceLightHighContrast
import track.it.app.ui.theme.inverseSurfaceLightMediumContrast
import track.it.app.ui.theme.onBackgroundDark
import track.it.app.ui.theme.onBackgroundDarkHighContrast
import track.it.app.ui.theme.onBackgroundDarkMediumContrast
import track.it.app.ui.theme.onBackgroundLight
import track.it.app.ui.theme.onBackgroundLightHighContrast
import track.it.app.ui.theme.onBackgroundLightMediumContrast
import track.it.app.ui.theme.onErrorContainerDark
import track.it.app.ui.theme.onErrorContainerDarkHighContrast
import track.it.app.ui.theme.onErrorContainerDarkMediumContrast
import track.it.app.ui.theme.onErrorContainerLight
import track.it.app.ui.theme.onErrorContainerLightHighContrast
import track.it.app.ui.theme.onErrorContainerLightMediumContrast
import track.it.app.ui.theme.onErrorDark
import track.it.app.ui.theme.onErrorDarkHighContrast
import track.it.app.ui.theme.onErrorDarkMediumContrast
import track.it.app.ui.theme.onErrorLight
import track.it.app.ui.theme.onErrorLightHighContrast
import track.it.app.ui.theme.onErrorLightMediumContrast
import track.it.app.ui.theme.onPrimaryContainerDark
import track.it.app.ui.theme.onPrimaryContainerDarkHighContrast
import track.it.app.ui.theme.onPrimaryContainerDarkMediumContrast
import track.it.app.ui.theme.onPrimaryContainerLight
import track.it.app.ui.theme.onPrimaryContainerLightHighContrast
import track.it.app.ui.theme.onPrimaryContainerLightMediumContrast
import track.it.app.ui.theme.onPrimaryDark
import track.it.app.ui.theme.onPrimaryDarkHighContrast
import track.it.app.ui.theme.onPrimaryDarkMediumContrast
import track.it.app.ui.theme.onPrimaryLight
import track.it.app.ui.theme.onPrimaryLightHighContrast
import track.it.app.ui.theme.onPrimaryLightMediumContrast
import track.it.app.ui.theme.onSecondaryContainerDark
import track.it.app.ui.theme.onSecondaryContainerDarkHighContrast
import track.it.app.ui.theme.onSecondaryContainerDarkMediumContrast
import track.it.app.ui.theme.onSecondaryContainerLight
import track.it.app.ui.theme.onSecondaryContainerLightHighContrast
import track.it.app.ui.theme.onSecondaryContainerLightMediumContrast
import track.it.app.ui.theme.onSecondaryDark
import track.it.app.ui.theme.onSecondaryDarkHighContrast
import track.it.app.ui.theme.onSecondaryDarkMediumContrast
import track.it.app.ui.theme.onSecondaryLight
import track.it.app.ui.theme.onSecondaryLightHighContrast
import track.it.app.ui.theme.onSecondaryLightMediumContrast
import track.it.app.ui.theme.onSurfaceDark
import track.it.app.ui.theme.onSurfaceDarkHighContrast
import track.it.app.ui.theme.onSurfaceDarkMediumContrast
import track.it.app.ui.theme.onSurfaceLight
import track.it.app.ui.theme.onSurfaceLightHighContrast
import track.it.app.ui.theme.onSurfaceLightMediumContrast
import track.it.app.ui.theme.onSurfaceVariantDark
import track.it.app.ui.theme.onSurfaceVariantDarkHighContrast
import track.it.app.ui.theme.onSurfaceVariantDarkMediumContrast
import track.it.app.ui.theme.onSurfaceVariantLight
import track.it.app.ui.theme.onSurfaceVariantLightHighContrast
import track.it.app.ui.theme.onSurfaceVariantLightMediumContrast
import track.it.app.ui.theme.onTertiaryContainerDark
import track.it.app.ui.theme.onTertiaryContainerDarkHighContrast
import track.it.app.ui.theme.onTertiaryContainerDarkMediumContrast
import track.it.app.ui.theme.onTertiaryContainerLight
import track.it.app.ui.theme.onTertiaryContainerLightHighContrast
import track.it.app.ui.theme.onTertiaryContainerLightMediumContrast
import track.it.app.ui.theme.onTertiaryDark
import track.it.app.ui.theme.onTertiaryDarkHighContrast
import track.it.app.ui.theme.onTertiaryDarkMediumContrast
import track.it.app.ui.theme.onTertiaryLight
import track.it.app.ui.theme.onTertiaryLightHighContrast
import track.it.app.ui.theme.onTertiaryLightMediumContrast
import track.it.app.ui.theme.outlineDark
import track.it.app.ui.theme.outlineDarkHighContrast
import track.it.app.ui.theme.outlineDarkMediumContrast
import track.it.app.ui.theme.outlineLight
import track.it.app.ui.theme.outlineLightHighContrast
import track.it.app.ui.theme.outlineLightMediumContrast
import track.it.app.ui.theme.outlineVariantDark
import track.it.app.ui.theme.outlineVariantDarkHighContrast
import track.it.app.ui.theme.outlineVariantDarkMediumContrast
import track.it.app.ui.theme.outlineVariantLight
import track.it.app.ui.theme.outlineVariantLightHighContrast
import track.it.app.ui.theme.outlineVariantLightMediumContrast
import track.it.app.ui.theme.primaryContainerDark
import track.it.app.ui.theme.primaryContainerDarkHighContrast
import track.it.app.ui.theme.primaryContainerDarkMediumContrast
import track.it.app.ui.theme.primaryContainerLight
import track.it.app.ui.theme.primaryContainerLightHighContrast
import track.it.app.ui.theme.primaryContainerLightMediumContrast
import track.it.app.ui.theme.primaryDark
import track.it.app.ui.theme.primaryDarkHighContrast
import track.it.app.ui.theme.primaryDarkMediumContrast
import track.it.app.ui.theme.primaryLight
import track.it.app.ui.theme.primaryLightHighContrast
import track.it.app.ui.theme.primaryLightMediumContrast
import track.it.app.ui.theme.scrimDark
import track.it.app.ui.theme.scrimDarkHighContrast
import track.it.app.ui.theme.scrimDarkMediumContrast
import track.it.app.ui.theme.scrimLight
import track.it.app.ui.theme.scrimLightHighContrast
import track.it.app.ui.theme.scrimLightMediumContrast
import track.it.app.ui.theme.secondaryContainerDark
import track.it.app.ui.theme.secondaryContainerDarkHighContrast
import track.it.app.ui.theme.secondaryContainerDarkMediumContrast
import track.it.app.ui.theme.secondaryContainerLight
import track.it.app.ui.theme.secondaryContainerLightHighContrast
import track.it.app.ui.theme.secondaryContainerLightMediumContrast
import track.it.app.ui.theme.secondaryDark
import track.it.app.ui.theme.secondaryDarkHighContrast
import track.it.app.ui.theme.secondaryDarkMediumContrast
import track.it.app.ui.theme.secondaryLight
import track.it.app.ui.theme.secondaryLightHighContrast
import track.it.app.ui.theme.secondaryLightMediumContrast
import track.it.app.ui.theme.surfaceBrightDark
import track.it.app.ui.theme.surfaceBrightDarkHighContrast
import track.it.app.ui.theme.surfaceBrightDarkMediumContrast
import track.it.app.ui.theme.surfaceBrightLight
import track.it.app.ui.theme.surfaceBrightLightHighContrast
import track.it.app.ui.theme.surfaceBrightLightMediumContrast
import track.it.app.ui.theme.surfaceContainerDark
import track.it.app.ui.theme.surfaceContainerDarkHighContrast
import track.it.app.ui.theme.surfaceContainerDarkMediumContrast
import track.it.app.ui.theme.surfaceContainerHighDark
import track.it.app.ui.theme.surfaceContainerHighDarkHighContrast
import track.it.app.ui.theme.surfaceContainerHighDarkMediumContrast
import track.it.app.ui.theme.surfaceContainerHighLight
import track.it.app.ui.theme.surfaceContainerHighLightHighContrast
import track.it.app.ui.theme.surfaceContainerHighLightMediumContrast
import track.it.app.ui.theme.surfaceContainerHighestDark
import track.it.app.ui.theme.surfaceContainerHighestDarkHighContrast
import track.it.app.ui.theme.surfaceContainerHighestDarkMediumContrast
import track.it.app.ui.theme.surfaceContainerHighestLight
import track.it.app.ui.theme.surfaceContainerHighestLightHighContrast
import track.it.app.ui.theme.surfaceContainerHighestLightMediumContrast
import track.it.app.ui.theme.surfaceContainerLight
import track.it.app.ui.theme.surfaceContainerLightHighContrast
import track.it.app.ui.theme.surfaceContainerLightMediumContrast
import track.it.app.ui.theme.surfaceContainerLowDark
import track.it.app.ui.theme.surfaceContainerLowDarkHighContrast
import track.it.app.ui.theme.surfaceContainerLowDarkMediumContrast
import track.it.app.ui.theme.surfaceContainerLowLight
import track.it.app.ui.theme.surfaceContainerLowLightHighContrast
import track.it.app.ui.theme.surfaceContainerLowLightMediumContrast
import track.it.app.ui.theme.surfaceContainerLowestDark
import track.it.app.ui.theme.surfaceContainerLowestDarkHighContrast
import track.it.app.ui.theme.surfaceContainerLowestDarkMediumContrast
import track.it.app.ui.theme.surfaceContainerLowestLight
import track.it.app.ui.theme.surfaceContainerLowestLightHighContrast
import track.it.app.ui.theme.surfaceContainerLowestLightMediumContrast
import track.it.app.ui.theme.surfaceDark
import track.it.app.ui.theme.surfaceDarkHighContrast
import track.it.app.ui.theme.surfaceDarkMediumContrast
import track.it.app.ui.theme.surfaceDimDark
import track.it.app.ui.theme.surfaceDimDarkHighContrast
import track.it.app.ui.theme.surfaceDimDarkMediumContrast
import track.it.app.ui.theme.surfaceDimLight
import track.it.app.ui.theme.surfaceDimLightHighContrast
import track.it.app.ui.theme.surfaceDimLightMediumContrast
import track.it.app.ui.theme.surfaceLight
import track.it.app.ui.theme.surfaceLightHighContrast
import track.it.app.ui.theme.surfaceLightMediumContrast
import track.it.app.ui.theme.surfaceVariantDark
import track.it.app.ui.theme.surfaceVariantDarkHighContrast
import track.it.app.ui.theme.surfaceVariantDarkMediumContrast
import track.it.app.ui.theme.surfaceVariantLight
import track.it.app.ui.theme.surfaceVariantLightHighContrast
import track.it.app.ui.theme.surfaceVariantLightMediumContrast
import track.it.app.ui.theme.tertiaryContainerDark
import track.it.app.ui.theme.tertiaryContainerDarkHighContrast
import track.it.app.ui.theme.tertiaryContainerDarkMediumContrast
import track.it.app.ui.theme.tertiaryContainerLight
import track.it.app.ui.theme.tertiaryContainerLightHighContrast
import track.it.app.ui.theme.tertiaryContainerLightMediumContrast
import track.it.app.ui.theme.tertiaryDark
import track.it.app.ui.theme.tertiaryDarkHighContrast
import track.it.app.ui.theme.tertiaryDarkMediumContrast
import track.it.app.ui.theme.tertiaryLight
import track.it.app.ui.theme.tertiaryLightHighContrast
import track.it.app.ui.theme.tertiaryLightMediumContrast

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable() () -> Unit
) {
  val colorScheme = when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
          val context = LocalContext.current
          if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      
      darkTheme -> darkScheme
      else -> lightScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = AppTypography,
    content = content
  )
}

