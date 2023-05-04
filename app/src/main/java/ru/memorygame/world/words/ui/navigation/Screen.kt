package ru.memorygame.world.words.ui.navigation

sealed class Screen(val route: String) {
    object Main: Screen("main_screen")
    object Auth: Screen("auth_screen")
    object Admin: Screen("admin_screen")
    object Settings: Screen("settings_screen")
    object Ads: Screen("ads_screen")
}