package com.alonsocamina.vecicare.ui.theme

import androidx.compose.ui.graphics.Color

//Modo Claro (Light Mode)
val LightPrimaryColor = Color(0xFF1E88E5) //Azul vibrante
val LightSecondaryColor = Color(0xFF00ACC1) //Turquesa
val LightBackgroundColor = Color(0xFFF9F9F9) //Crema claro
val LightSurfaceColor = Color(0xFFFFFFFF) //Blanco puro
val LightOnPrimaryColor = Color(0xFFFFFFFF) //Texto blanco sobre azul
val LightOnSecondaryColor = Color(0xFF000000) //Texto negro sobre turquesa
val LightOnBackgroundColor = Color(0xFF000000) //Texto negro sobre fondo claro
val LightGradientStart = LightPrimaryColor.copy(alpha = 0.8f) //Azul vibrante con transparencia
val LightGradientEnd = LightBackgroundColor //Crema claro

//Modo Oscuro (Dark Mode)
val DarkPrimaryColor = Color(0xFF0D47A1) //Azul oscuro
val DarkSecondaryColor = Color(0xFF00838F) //Turquesa oscuro
val DarkBackgroundColor = Color(0xFF121212) //Gris oscuro
val DarkSurfaceColor = Color(0xFF1E1E1E) //Gris oscuro
val DarkOnPrimaryColor = Color(0xFFFFFFFF) //Texto blanco sobre azul oscuro
val DarkOnSecondaryColor = Color(0xFFFFFFFF) //Texto blanco sobre turquesa oscuro
val DarkOnBackgroundColor = Color(0xFFFFFFFF) //Texto blanco sobre fondo oscuro
val DarkGradientStart = DarkPrimaryColor.copy(alpha = 0.8f) //Azul oscuro con transparencia
val DarkGradientEnd = DarkBackgroundColor //Gris oscuro