package com.vifor.app.navigation

object Routes {
    const val HOME = "home"
    const val SCAN = "scan"
    const val SEARCH = "search"
    const val HISTORY = "history"
    const val FAVORITES = "favorites"
    const val SETTINGS = "settings"
    const val RESULT = "result/{foodId}"

    // Screens that show the bottom navigation bar
    val TABS = listOf(HOME, SEARCH, HISTORY, FAVORITES, SETTINGS)

    fun result(foodId: Int) = "result/$foodId"
}