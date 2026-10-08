package com.vifor.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Rounded food thumbnail. [imageResource] is RecognizedFood.imageResource, treated as the
 * name of a drawable (e.g. "apple" or "apple.jpg" -> res/drawable/apple.*).
 * Falls back to the food's first letter when no drawable is found.
 */
@Composable
fun FoodImage(
    imageResource: String,
    name: String,
    modifier: Modifier = Modifier,
    corner: Dp = 16.dp
) {
    val context = LocalContext.current
    val resId = remember(imageResource) {
        context.resources.getIdentifier(
            imageResource.substringBeforeLast('.').lowercase().replace(' ', '_'),
            "drawable",
            context.packageName
        )
    }
    Box(
        modifier
            .clip(RoundedCornerShape(corner))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (resId != 0) {
            Image(
                painter = painterResource(resId),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                name.take(1).uppercase(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}