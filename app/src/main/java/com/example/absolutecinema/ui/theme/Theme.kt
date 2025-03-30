package com.example.absolutecinema.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkColorPallet = darkColorScheme(
    background = Color(0xFF1E1E1E), //цвет заднего фона
    surface = Color(0xff323232),    //доп. цвет заднего фона
    primary = Color(0xFFFFFFFF),    //цвет текста
    secondary = Color(0xFFC1C0C0),  //доп. цвет текста
)
val LightColorPallet = lightColorScheme(
    background = Color(0xFFFFFFFF), //цвет заднего фона
    surface = Color(0xffB4B4B4),    //доп. цвет заднего фона
    primary = Color(0xFF262626),    //цвет текста
    secondary = Color(0xFF6A6A6A),  //доп. цвет текста
)

@Composable
fun AbsoluteCinemaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorPallet
        else -> LightColorPallet
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}