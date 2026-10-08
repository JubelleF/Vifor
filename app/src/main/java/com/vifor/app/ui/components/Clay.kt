package com.vifor.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Claymorphism surface: a puffy, soft 3D shape.
 *  - tinted outer shadow (the clay sitting on the page)
 *  - fill that is slightly lighter at the top (rounded, inflated look)
 *  - gradient rim: bright highlight top-left, soft shade bottom-right (the "inner" light and shadow)
 * Chain .clickable { } after this so the ripple stays inside the shape.
 */
fun Modifier.clay(
    color: Color,
    shape: Shape = RoundedCornerShape(28.dp),
    elevation: Dp = 10.dp,
): Modifier {
    val shade = lerp(color, Color.Black, 0.40f)
    val rim = Brush.linearGradient(
        listOf(
            Color.White.copy(alpha = 0.75f),
            Color.Transparent,
            lerp(color, Color.Black, 0.25f).copy(alpha = 0.45f)
        )
    )
    return this
        .shadow(elevation, shape, clip = false, ambientColor = shade, spotColor = shade)
        .clip(shape)
        .background(Brush.verticalGradient(listOf(lerp(color, Color.White, 0.14f), color)), shape)
        .border(2.dp, rim, shape)
}