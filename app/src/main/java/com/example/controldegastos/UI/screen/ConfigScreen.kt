package com.example.controldegastos.UI.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.controldegastos.UI.Screen
import com.example.controldegastos.UI.Gastos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(navController: NavController, viewModel: Gastos) {
    // Estados locales temporales para capturar lo que escribe el usuario
    var presupuestoInput by remember { mutableStateOf("") }
    var periodoInput by remember { mutableStateOf("Mensual") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "⚙️ Configuración inicial",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Define tu plan financiero para comenzar",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Tarjeta contenedora del formulario
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Monto del Presupuesto",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                // Campo para ingresar dinero (Solo números)
                OutlinedTextField(
                    value = presupuestoInput,
                    onValueChange = {
                        presupuestoInput = it
                        if (it.isNotEmpty()) errorMessage = ""
                    },
                    label = { Text("Ej: 3000") },
                    prefix = { Text("$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = errorMessage.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Periodo del Plan",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                // Selector simple de periodo (Botones de opción en fila)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val opciones = listOf("Semanal", "Quincenal", "Mensual")
                    opciones.forEach { opcion ->
                        FilterChip(
                            selected = (periodoInput == opcion),
                            onClick = { periodoInput = opcion },
                            label = { Text(opcion) }
                        )
                    }
                }
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón para avanzar al Planificador (Paso 2)
        // Modifica el onClick de tu botón en ConfigScreen.kt
        Button(
            onClick = {
                val monto = presupuestoInput.toDoubleOrNull()
                if (presupuestoInput.isBlank() || monto == null) {
                    errorMessage = "Por favor, ingresa un monto numérico válido."
                } else {
                    errorMessage = ""

                    // 🔥 AQUÍ SE GUARDA EN EL VIEWMODEL EL DATO REAL:
                    viewModel.guardarConfiguracionInicial(monto, periodoInput)

                    // Avanza al planificador
                    navController.navigate(Screen.Planner.route)
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Iniciar Planificación 🚀", fontSize = 16.sp)
        }
    }
}