package com.example.controldegastos.UI

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome_screen")
    object Config : Screen("config_screen")
    object Planner : Screen("planner_screen")
    object Summary : Screen("summary_screen")
}