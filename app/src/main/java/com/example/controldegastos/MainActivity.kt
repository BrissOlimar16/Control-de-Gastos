package com.example.controldegastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.controldegastos.UI.Screen
import com.example.controldegastos.UI.screen.*
import com.example.controldegastos.ui.theme.ControlDeGastosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ControlDeGastosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Welcome.route
                    ) {
                        composable(Screen.Welcome.route) {
                            WelcomeScreen(navController = navController)
                        }
                        composable(Screen.Config.route) {
                            ConfigScreen(navController = navController)
                        }
                        composable(Screen.Planner.route) {
                            PlannerScreen(navController = navController)
                        }
                        composable(Screen.Summary.route) {
                            SummaryScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}