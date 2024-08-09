package com.example.to_do.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.DarkGray

private val DarkColorScheme = darkColorScheme(
    primary = Green,
    secondary = Coral,
    background = Charcoal,
    surface = DarkGray,
    onPrimary = LightGray,
    onSecondary = LightGray,
    onBackground = LightGray,
    onSurface = LightGray
)

private val LightColorScheme = lightColorScheme(
    primary = Green,
    secondary = Coral,
    background = LightGray,
    surface = LightGreen,
    onPrimary = LightGray,
    onSecondary = Charcoal,
    onBackground = Charcoal,
    onSurface = Charcoal
)

@Composable
fun ToDoTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
