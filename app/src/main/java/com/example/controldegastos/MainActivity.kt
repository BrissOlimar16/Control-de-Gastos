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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.controldegastos.UI.Gastos

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

                    // Instancia única del ViewModel para toda la app
                    val gastosViewModel: Gastos = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Welcome.route
                    ) {
                        composable(Screen.Welcome.route) {
                            WelcomeScreen(navController = navController)
                        }
                        composable(Screen.Config.route) {
                            // Le pasamos el cerebro a la configuración
                            ConfigScreen(navController = navController, viewModel = gastosViewModel)
                        }
                        composable(Screen.Planner.route) {
                            // Le pasamos el cerebro al planificador
                            PlannerScreen(navController = navController, viewModel = gastosViewModel)
                        }
                        composable(Screen.Summary.route) {
                            // Le pasamos el cerebro al resumen final
                            SummaryScreen(navController = navController, viewModel = gastosViewModel)
                        }
                    }
                }
            }
        }
    }
}
