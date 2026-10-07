package com.vifor.app.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vifor.app.data.ViforDatabase
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen(
    onFoodClick: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dao = remember { ViforDatabase.getInstance(context).favoriteDao() }
    val favorites by remember { dao.getFavoriteFoods() }.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    Column(modifier.fillMaxSize().padding(16.dp)) {
        TextButton(onClick = onBack) { Text("‹ Back") }
        Text("Favorites", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        if (favorites.isEmpty()) {
            Text("No favorites yet")
        } else {
            LazyColumn {
                items(favorites, key = { it.foodId }) { food ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onFoodClick(food.foodId) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(food.foodName, style = MaterialTheme.typography.titleMedium)
                            Text(food.category, style = MaterialTheme.typography.bodySmall)
                        }
                        TextButton(onClick = {
                            scope.launch { dao.deleteByFoodId(food.foodId) }
                        }) { Text("Remove") }
                    }
                }
            }
        }
    }
}