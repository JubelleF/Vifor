package com.vifor.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vifor.app.data.ViforDatabase
import com.vifor.app.navigation.Routes
import com.vifor.app.ui.components.FoodImage
import com.vifor.app.ui.components.clay
import com.vifor.app.ui.theme.Spacing
import java.time.LocalTime

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    onFoodClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val recent by remember { ViforDatabase.getInstance(context).scanDao().getRecentFoods(8) }
        .collectAsState(initial = emptyList())
    val greeting = remember {
        when (LocalTime.now().hour) {
            in 5..11 -> "Good morning!"
            in 12..17 -> "Good afternoon!"
            else -> "Good evening!"
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Header: one big clay slab, rounded at the bottom, running under the status bar
        Column(
            Modifier
                .fillMaxWidth()
                .clay(
                    colors.primaryContainer,
                    RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp),
                    elevation = 14.dp
                )
                .statusBarsPadding()
                .padding(Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.md)
            ) {
                Box(
                    Modifier.size(52.dp).clay(colors.surface, CircleShape, elevation = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.CameraAlt, null, tint = colors.primary, modifier = Modifier.size(26.dp))
                }
                Text("ViFoR", style = MaterialTheme.typography.headlineMedium, color = colors.onPrimaryContainer)
            }
            Text(
                greeting,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onPrimaryContainer.copy(alpha = 0.75f)
            )
            Text(
                "Identify your food instantly",
                style = MaterialTheme.typography.titleLarge,
                color = colors.onPrimaryContainer
            )
        }

        Column(
            Modifier.padding(Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.lg)
        ) {
            ScanCard(onClick = { onNavigate(Routes.SCAN) })

            Text(
                "ViFoR uses image recognition to identify food items and provide health benefit " +
                        "information to support informed dietary choices.",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .clay(colors.surface, RoundedCornerShape(28.dp), elevation = 6.dp)
                    .padding(Spacing.lg)
            )

            Section("QUICK ACCESS") {
                // Three analogous clay colours: lime, green, teal
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                    ClayTile(
                        "Search", Icons.Outlined.Search,
                        colors.tertiaryContainer, colors.onTertiaryContainer,
                        { onNavigate(Routes.SEARCH) }, Modifier.weight(1f)
                    )
                    ClayTile(
                        "History", Icons.Outlined.History,
                        colors.primaryContainer, colors.onPrimaryContainer,
                        { onNavigate(Routes.HISTORY) }, Modifier.weight(1f)
                    )
                    ClayTile(
                        "Favorites", Icons.Outlined.FavoriteBorder,
                        colors.secondaryContainer, colors.onSecondaryContainer,
                        { onNavigate(Routes.FAVORITES) }, Modifier.weight(1f)
                    )
                }
            }

            Section("RECENT SCANS") {
                if (recent.isEmpty()) {
                    Text(
                        "No scans yet. Tap Scan Food to get started.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant
                    )
                } else {
                    // Padding keeps the clay shadows from being cut off by the row
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                        contentPadding = PaddingValues(horizontal = Spacing.xs, vertical = Spacing.sm)
                    ) {
                        items(recent, key = { it.foodId }) { food ->
                            Column(
                                Modifier.width(76.dp).clickable { onFoodClick(food.foodId) },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                            ) {
                                Box(Modifier.size(76.dp).clay(colors.surface, RoundedCornerShape(24.dp), elevation = 8.dp)) {
                                    FoodImage(
                                        food.imageResource, food.foodName,
                                        Modifier.fillMaxSize().padding(5.dp),
                                        corner = 19.dp
                                    )
                                }
                                Text(
                                    food.foodName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScanCard(onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Row(
        Modifier
            .fillMaxWidth()
            .clay(colors.primary, RoundedCornerShape(32.dp), elevation = 14.dp)
            .clickable(onClick = onClick)
            .padding(Spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        Box(
            Modifier.size(60.dp).clay(lerp(colors.primary, Color.White, 0.25f), RoundedCornerShape(20.dp), elevation = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.CameraAlt, null, tint = colors.onPrimary, modifier = Modifier.size(28.dp))
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Text("Scan Food", style = MaterialTheme.typography.titleLarge, color = colors.onPrimary)
            Text(
                "Point camera at any food item",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onPrimary.copy(alpha = 0.85f)
            )
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = colors.onPrimary)
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        Text(
            title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        content()
    }
}

@Composable
private fun ClayTile(
    label: String,
    icon: ImageVector,
    container: Color,
    onContainer: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .height(92.dp)
            .clay(container, RoundedCornerShape(28.dp), elevation = 10.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.sm, Alignment.CenterVertically)
    ) {
        Icon(icon, null, tint = onContainer, modifier = Modifier.size(26.dp))
        Text(label, style = MaterialTheme.typography.titleSmall, color = onContainer)
    }
}