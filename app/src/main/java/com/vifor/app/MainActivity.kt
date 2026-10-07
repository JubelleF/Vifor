package com.vifor.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.lifecycle.lifecycleScope
import com.vifor.app.data.DatabaseSeeder
import com.vifor.app.data.ViforDatabase
import com.vifor.app.navigation.ViforNavGraph
import com.vifor.app.ui.theme.ViforTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) { DatabaseSeeder.seedIfEmpty(applicationContext) }
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val settings by remember { ViforDatabase.getInstance(context).settingsDao().observe() }
                .collectAsState(initial = null)

            // Until settings load, follow the phone's own theme
            val darkTheme = settings?.let { it.darkMode == 1 } ?: isSystemInDarkTheme()
            val fontScale = (settings?.textSize ?: 1.0).toFloat()
            val density = LocalDensity.current

            CompositionLocalProvider(
                LocalDensity provides Density(density.density, fontScale)
            ) {
                ViforTheme(darkTheme = darkTheme) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        ViforNavGraph(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}