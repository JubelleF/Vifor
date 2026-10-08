package com.vifor.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vifor.app.navigation.Routes

private data class Tab(val route: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    Tab(Routes.HOME, "Home", Icons.Outlined.Home),
    Tab(Routes.SEARCH, "Search", Icons.Outlined.Search),
    Tab(Routes.HISTORY, "History", Icons.Outlined.History),
    Tab(Routes.FAVORITES, "Favorites", Icons.Outlined.FavoriteBorder),
    Tab(Routes.SETTINGS, "Settings", Icons.Outlined.Settings),
)

@Composable
fun ViforBottomBar(currentRoute: String?, onSelect: (String) -> Unit) {
    val colors = MaterialTheme.colorScheme
    // The whole bar is one clay slab with rounded top corners
    Box(
        Modifier
            .fillMaxWidth()
            .clay(colors.surface, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp), elevation = 16.dp)
    ) {
        NavigationBar(containerColor = Color.Transparent, tonalElevation = 0.dp) {
            tabs.forEach { tab ->
                val selected = tab.route == currentRoute
                NavigationBarItem(
                    selected = selected,
                    onClick = { onSelect(tab.route) },
                    icon = { Icon(tab.icon, contentDescription = tab.label) },
                    label = {
                        Text(
                            tab.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colors.onPrimaryContainer,
                        selectedTextColor = colors.onSurface,
                        unselectedIconColor = colors.onSurfaceVariant,
                        unselectedTextColor = colors.onSurfaceVariant,
                        indicatorColor = colors.primaryContainer   // the selected tab is a clay-green pill
                    )
                )
            }
        }
    }
}