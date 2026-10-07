package com.vifor.app.navigation

import androidx.compose.runtime.Composable
import com.vifor.app.ui.scan.ScanScreen
import androidx.compose.ui.Modifier
import com.vifor.app.ui.settings.SettingsScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vifor.app.ui.favorites.FavoritesScreen
import com.vifor.app.ui.history.HistoryScreen
import com.vifor.app.ui.home.HomeScreen
import com.vifor.app.ui.result.FoodResultScreen
import com.vifor.app.ui.search.SearchScreen

@Composable
fun ViforNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val back: () -> Unit = { navController.popBackStack() }

    NavHost(navController, startDestination = Routes.HOME, modifier = modifier) {
        composable(Routes.HOME) {
            HomeScreen(onNavigate = { navController.navigate(it) })
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