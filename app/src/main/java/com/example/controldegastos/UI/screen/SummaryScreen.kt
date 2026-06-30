package com.example.controldegastos.UI.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.controldegastos.UI.Screen
import com.example.controldegastos.UI.Gastos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(navController: NavController, viewModel: Gastos) {
    val presupuestoInicial = viewModel.presupuestoInicial
    val totalGastado = viewModel.totalGastado
    val saldoRestante = viewModel.saldoRestante

    // Evita la división entre cero si regresa al inicio
    val porcentajeGastado = if (presupuestoInicial > 0) (totalGastado / presupuestoInicial) * 100 else 0.0

    // Lógica del Semáforo Financiero para determinar el color de alerta
    val (colorSemaforo, mensajeAlerta) = when {
        porcentajeGastado < 50.0 -> Color(0xFF4CAF50) to "¡Vas excelente! Tu presupuesto está bajo control. 🟢"
        porcentajeGastado in 50.0..85.0 -> Color(0xFFFFEB3B) to "Cuidado, estás cerca del límite establecido. 🟡"
        else -> Color(0xFFF44336) to "¡Alerta de Gasto! Has excedido el margen de seguridad. 🔴"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Análisis de Gastos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Planner.route) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Sección 1: Tarjeta de Balance General
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Saldo Disponible", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("$${String.format("%.2f", saldoRestante)}", fontSize = 32.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Inicial: $${presupuestoInicial}", fontSize = 13.sp)
                        Text("Gastado: $${totalGastado}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Sección 2: El Semáforo Visual (Rúbrica: Componente UI Dinámico)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Estado de Alerta Financiera",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Círculo luminoso del semáforo
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(color = colorSemaforo, shape = CircleShape)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = mensajeAlerta,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Sección 3: Acciones Finales (Exportar Datos / Reiniciar)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón de Exportación CSV
                ElevatedButton(
                    onClick = {
                        // Aquí conectaremos la lógica de FileProvider más adelante
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Exportar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Exportar Reporte (CSV) 📄", fontSize = 15.sp)
                }

                // Botón para volver a empezar el flujo
                Button(
                    onClick = {
                        // Limpia la pila de navegación y regresa al inicio
                        navController.navigate(Screen.Welcome.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Crear Nuevo Plan 🔄", fontSize = 15.sp)
                }
            }
        }
    }
}