package com.vifor.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.vifor.app.R

// app/src/main/res/font/takugiregular.ttf
val AppFont = FontFamily(Font(R.font.takugiregular))

val HeadingFont: FontFamily = AppFont
val BodyFont: FontFamily = AppFont

// Hierarchy: each step is clearly bigger/heavier than the one below it.
//   Display 44  >  Headline 30  >  Title 22 / 18 / 15  >  Body 16 / 14 / 12  >  Label 12 caps / 11
val Typography = Typography(
    displayMedium = TextStyle(fontFamily = HeadingFont, fontWeight = FontWeight.Bold, fontSize = 44.sp, lineHeight = 50.sp),
    headlineMedium = TextStyle(fontFamily = HeadingFont, fontWeight = FontWeight.Bold, fontSize = 30.sp, lineHeight = 36.sp),
    titleLarge = TextStyle(fontFamily = HeadingFont, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium = TextStyle(fontFamily = HeadingFont, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, lineHeight = 24.sp),
    titleSmall = TextStyle(fontFamily = HeadingFont, fontWeight = FontWeight.Medium, fontSize = 15.sp, lineHeight = 20.sp),
    bodyLarge = TextStyle(fontFamily = BodyFont, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = BodyFont, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 21.sp),
    bodySmall = TextStyle(fontFamily = BodyFont, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 17.sp),
    labelMedium = TextStyle(fontFamily = BodyFont, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 1.5.sp),
    labelSmall = TextStyle(fontFamily = BodyFont, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 14.sp),
)