package com.alonsocamina.vecicare.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.alonsocamina.vecicare.R

// Configuración del proveedor de Google Fonts
val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// Configuración de fuentes personalizadas
val PoppinsFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Poppins"),
        fontProvider = fontProvider,
        weight = FontWeight.Bold
    )
)

val RobotoFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Roboto"),
        fontProvider = fontProvider,
        weight = FontWeight.Normal
    )
)

// Configuración de la tipografía para Material3
val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontSize = 28.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Bold
    ),
    headlineLarge = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontSize = 40.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Bold
    ),
    bodyLarge = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        fontFamily = RobotoFontFamily,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Normal
    )
)
