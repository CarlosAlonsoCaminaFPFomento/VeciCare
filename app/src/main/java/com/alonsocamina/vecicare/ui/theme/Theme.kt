package com.alonsocamina.vecicare.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.alonsocamina.vecicare.ui.theme.Shapes
import androidx.compose.ui.graphics.Brush
import com.alonsocamina.vecicare.ui.theme.Typography


private val LightColorScheme = lightColorScheme(
    primary = LightPrimaryColor,
    secondary = LightSecondaryColor,
    background = LightBackgroundColor,
    surface = LightSurfaceColor,
    onPrimary = LightOnPrimaryColor,
    onSecondary = LightOnSecondaryColor,
    onBackground = LightOnBackgroundColor
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimaryColor,
    secondary = DarkSecondaryColor,
    background = DarkBackgroundColor,
    surface = DarkSurfaceColor,
    onPrimary = DarkOnPrimaryColor,
    onSecondary = DarkOnSecondaryColor,
    onBackground = DarkOnBackgroundColor

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun VeciCareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
   val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // Usa la tipografía personalizada
        shapes = Shapes,
        content = content
    )
}

@Composable
fun gradientBrush(darkTheme: Boolean): Brush {
    return if (darkTheme){
        Brush.verticalGradient(
            colors = listOf(DarkGradientStart, DarkGradientEnd)
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(LightGradientStart, LightGradientEnd)
        )
    }
}