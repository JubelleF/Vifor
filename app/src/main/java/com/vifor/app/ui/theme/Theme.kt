package com.vifor.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    primaryContainer = ClayGreen,
    onPrimaryContainer = ClayGreenDeep,
    secondary = Color(0xFF3F9C8E),
    onSecondary = Color.White,
    secondaryContainer = ClayTeal,
    onSecondaryContainer = ClayTealDeep,
    tertiary = Color(0xFF8CAF2E),
    onTertiary = Color.White,
    tertiaryContainer = ClayLime,
    onTertiaryContainer = ClayLimeDeep,
    background = MintCream,
    onBackground = ForestInk,
    surface = MintSurface,
    onSurface = ForestInk,
    surfaceVariant = MintVariant,
    onSurfaceVariant = ForestMute,
    outline = SageOutline,
    outlineVariant = SageLine,
)

private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimaryDark,
    onPrimary = Color(0xFF0B2A18),
    primaryContainer = ClayGreenDark,
    onPrimaryContainer = ClayGreenDarkOn,
    secondary = Color(0xFF7ED8CA),
    onSecondary = Color(0xFF06302B),
    secondaryContainer = ClayTealDark,
    onSecondaryContainer = ClayTealDarkOn,
    tertiary = Color(0xFFCDE87A),
    onTertiary = Color(0xFF263300),
    tertiaryContainer = ClayLimeDark,
    onTertiaryContainer = ClayLimeDarkOn,
    background = NightBackground,
    onBackground = NightInk,
    surface = NightSurface,
    onSurface = NightInk,
    surfaceVariant = NightVariant,
    onSurfaceVariant = NightMute,
    outline = NightOutline,
    outlineVariant = NightLine,
)

// Rounded: generous corners everywhere
private val ViforShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(32.dp),
    extraLarge = RoundedCornerShape(40.dp),
)

@Composable
fun ViforTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        shapes = ViforShapes,
        content = content,
    )
}