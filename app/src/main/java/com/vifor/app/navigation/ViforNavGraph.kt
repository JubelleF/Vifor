package com.vifor.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vifor.app.ui.favorites.FavoritesScreen
import com.vifor.app.ui.history.HistoryScreen
import com.vifor.app.ui.home.HomeScreen
import com.vifor.app.ui.result.FoodResultScreen
import com.vifor.app.ui.scan.ScanScreen
import com.vifor.app.ui.search.SearchScreen
import com.vifor.app.ui.settings.SettingsScreen

// The NavController now lives in MainActivity so the bottom bar can share it.
@Composable
fun ViforNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val back: () -> Unit = { navController.popBackStack() }

    NavHost(navController, startDestination = Routes.HOME, modifier = modifier) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigate = { route ->
                    // Tabs switch like the bottom bar; Scan is a normal push
                    if (route in Routes.TABS) navController.navigateToTab(route)
                    else navController.navigate(route)
                },
                onFoodClick = { navController.navigate(Routes.result(it)) }
            )
        }
        composable(Routes.SEARCH) {
            SearchScreen(
                onFoodClick = { navController.navigate(Routes.result(it)) },
                onBack = back
            )
        }
        composable(
            Routes.RESULT,
            arguments = listOf(navArgument("foodId") { type = NavType.IntType })
        ) { entry ->
            FoodResultScreen(
                foodId = entry.arguments?.getInt("foodId") ?: 0,
                onBack = back
            )
        }
        composable(Routes.SCAN) {
            ScanScreen(
                onResult = { foodId ->
                    navController.navigate(Routes.result(foodId)) {
                        popUpTo(Routes.SCAN) { inclusive = true }
                    }
                },
                onBack = back
            )
        }
        composable(Routes.HISTORY) { HistoryScreen(onBack = back) }
        composable(Routes.FAVORITES) {
            FavoritesScreen(
                onFoodClick = { navController.navigate(Routes.result(it)) },
                onBack = back
            )
        }
        composable(Routes.SETTINGS) { SettingsScreen(onBack = back) }
    }
}

fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(Routes.HOME) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}