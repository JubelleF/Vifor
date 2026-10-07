package com.vifor.app.ui.result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vifor.app.data.Favorite
import com.vifor.app.data.FoodWithDetails
import com.vifor.app.data.ViforDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

@Composable
fun FoodResultScreen(foodId: Int, onBack: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val db = remember { ViforDatabase.getInstance(context) }
    val favoriteDao = remember { db.favoriteDao() }
    val scope = rememberCoroutineScope()

    val details by produceState<FoodWithDetails?>(null, foodId) {
        value = db.foodDao().getFoodWithDetails(foodId)
    }
    val isFavorite by remember(foodId) { favoriteDao.isFavorite(foodId) }
        .collectAsState(initial = false)

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(onClick = onBack) { Text("‹ Back") }

        val d = details
        if (d == null) {
            Text("Loading…")
        } else {
            Text(d.food.foodName, style = MaterialTheme.typography.headlineMedium)
            Text(
                "${d.food.scientificName} • ${d.food.category}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(12.dp))

            Button(onClick = {
                scope.launch {
                    if (isFavorite) {
                        favoriteDao.deleteByFoodId(foodId)
                    } else {
                        val now = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        favoriteDao.insert(Favorite(foodId = foodId, dateAdded = now))
                    }
                }
            }) {
                Text(if (isFavorite) "Remove from Favorites" else "Add to Favorites")
            }

            Spacer(Modifier.height(20.dp))
            Text("Health Benefits", style = MaterialTheme.typography.titleLarge)
            d.benefits.forEach { b ->
                Spacer(Modifier.height(8.dp))
                Text(b.benefitTitle, style = MaterialTheme.typography.titleSmall)
                Text(b.benefitDescription, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(20.dp))
            Text("Nutrition Facts", style = MaterialTheme.typography.titleLarge)
            d.nutritionFacts.forEach { n ->
                val dv = n.dailyValuePercent?.let { " (${it.toInt()}% DV)" } ?: ""
                Text("${n.nutrientName}: ${n.amount} ${n.unit}$dv")
            }
        }
    }
}