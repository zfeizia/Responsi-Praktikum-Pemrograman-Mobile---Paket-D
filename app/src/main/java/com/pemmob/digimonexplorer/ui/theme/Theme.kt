package com.pemmob.digimonexplorer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = DigiOrange,
    onPrimary = Color.White,
    secondary = DigiBlue,
    onSecondary = Color.White,
    background = DigiBackground,
    surface = DigiSurface,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

private val DarkColors = darkColorScheme(
    primary = DigiOrangeLight,
    onPrimary = Color(0xFF3E2200),
    secondary = DigiBlueLight,
    onSecondary = Color(0xFF00214A),
    background = DigiDarkBackground,
    surface = DigiDarkSurface,
    onBackground = Color(0xFFEAE1DA),
    onSurface = Color(0xFFEAE1DA)
)

@Composable
fun DigimonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = DigimonTypography,
        content = content
    )
}