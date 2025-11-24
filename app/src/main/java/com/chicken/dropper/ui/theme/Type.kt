package com.chicken.dropper.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.chicken.dropper.R

private val Tillana = FontFamily(
    Font(R.font.tillana_regular, FontWeight.Normal),
    Font(R.font.tillana_medium, FontWeight.Medium),
    Font(R.font.tillana_semi_bold, FontWeight.SemiBold),
    Font(R.font.tillana_bold, FontWeight.Bold),
    Font(R.font.tillana_extra_bold, FontWeight.ExtraBold)
)

val Typography = Typography(
    displayLarge = TextStyle(fontFamily = Tillana, fontWeight = FontWeight.ExtraBold, fontSize = 42.sp),
    displayMedium = TextStyle(fontFamily = Tillana, fontWeight = FontWeight.Bold, fontSize = 36.sp),
    displaySmall = TextStyle(fontFamily = Tillana, fontWeight = FontWeight.Bold, fontSize = 30.sp),
    headlineMedium = TextStyle(fontFamily = Tillana, fontWeight = FontWeight.SemiBold, fontSize = 24.sp),
    headlineSmall = TextStyle(fontFamily = Tillana, fontWeight = FontWeight.Medium, fontSize = 20.sp),
    bodyLarge = TextStyle(fontFamily = Tillana, fontWeight = FontWeight.Normal, fontSize = 18.sp),
    bodyMedium = TextStyle(fontFamily = Tillana, fontWeight = FontWeight.Normal, fontSize = 16.sp),
    labelLarge = TextStyle(fontFamily = Tillana, fontWeight = FontWeight.Bold, fontSize = 16.sp)
)