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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vifor.app.data.DatabaseSeeder
import com.vifor.app.data.ViforDatabase
import com.vifor.app.navigation.Routes
import com.vifor.app.navigation.ViforNavGraph
import com.vifor.app.navigation.navigateToTab
import com.vifor.app.ui.components.ViforBottomBar
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

            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val route = backStackEntry?.destination?.route
            val isHome = route == null || route == Routes.HOME

            // Light theme -> dark status bar icons (Home's header is now a light clay colour too)
            val view = LocalView.current
            SideEffect {
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }

            CompositionLocalProvider(
                LocalDensity provides Density(density.density, fontScale)
            ) {
                ViforTheme(darkTheme = darkTheme) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            if (route in Routes.TABS) {
                                ViforBottomBar(currentRoute = route) { navController.navigateToTab(it) }
                            }
                        }
                    ) { innerPadding ->
                        ViforNavGraph(
                            navController = navController,
                            modifier = Modifier.padding(
                                // Home draws its own header under the status bar
                                top = if (isHome) 0.dp else innerPadding.calculateTopPadding(),
                                bottom = innerPadding.calculateBottomPadding()
                            )
                        )
                    }
                }
            }
        }
    }
}