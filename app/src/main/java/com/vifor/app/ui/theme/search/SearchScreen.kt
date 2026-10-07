package com.vifor.app.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vifor.app.data.ViforDatabase

@Composable
fun SearchScreen(
    onFoodClick: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dao = remember { ViforDatabase.getInstance(context).foodDao() }
    var query by remember { mutableStateOf("") }
    val results by remember(query) { dao.search(query.trim()) }
        .collectAsState(initial = emptyList())

    Column(modifier.fillMaxSize().padding(16.dp)) {
        TextButton(onClick = onBack) { Text("‹ Back") }
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search fruits and vegetables") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        if (results.isEmpty()) {
            Text("No results found")
        } else {
            LazyColumn {
                items(results, key = { it.foodId }) { food ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onFoodClick(food.foodId) }
                            .padding(vertical = 12.dp)
                    ) {
                        Text(food.foodName, style = MaterialTheme.typography.titleMedium)
                        Text(
                            "${food.scientificName} • ${food.category}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}