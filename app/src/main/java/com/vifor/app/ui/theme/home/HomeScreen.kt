package com.vifor.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vifor.app.navigation.Routes

@Composable
fun HomeScreen(onNavigate: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("ViFoR", style = MaterialTheme.typography.displayMedium)
        Text("Vision-Based Food Recognition", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(32.dp))
        MenuButton("Scan Food") { onNavigate(Routes.SCAN) }
        MenuButton("Search") { onNavigate(Routes.SEARCH) }
        MenuButton("History") { onNavigate(Routes.HISTORY) }
        MenuButton("Favorites") { onNavigate(Routes.FAVORITES) }
        MenuButton("Settings") { onNavigate(Routes.SETTINGS) }
    }
}

@Composable
private fun MenuButton(label: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label)
    }
}