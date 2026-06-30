package com.example.controldegastos.UI.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.controldegastos.UI.Gastos
import com.example.controldegastos.UI.Screen

// Modelo de datos flexible para representar cualquier gasto en la lista temporal
data class GastoItem(val id: String, val nombre: String, val precio: Double, val esFijo: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerScreen(navController: NavController, viewModel: Gastos) {
    var busqueda by remember { mutableStateOf("") }

    // Estados para el formulario de Gastos Fijos/Servicios
    var nombreGastoFijo by remember { mutableStateOf("") }
    var costoGastoFijo by remember { mutableStateOf("") }
    var errorGastoFijo by remember { mutableStateOf("") }

    // Lista unificada que contiene tanto sugerencias como lo que agregue el usuario
    val listaGastos = remember {
        mutableStateListOf(
            GastoItem("1", "Leche Entera 1L", 24.50),
            GastoItem("2", "Huevo Blanco (1kg)", 48.00),
            GastoItem("3", "Arroz Extra 1kg", 22.00),
            GastoItem("4", "Aceite de Cocina 800ml", 39.50)
        )
    }

    // Filtrar la lista según la barra de búsqueda
    val listaFiltrada = listaGastos.filter {
        it.nombre.contains(busqueda, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "📋 Planificador de Gastos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Agrega tus servicios fijos o selecciona productos sugeridos.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 🔥 NUEVA SECCIÓN: Formulario para Gastos Fijos (Renta, Luz, Servicios)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("🏠 Agregar Servicio o Gasto Fijo", fontWeight = FontWeight.Bold, fontSize = 15.sp)

                    OutlinedTextField(
                        value = nombreGastoFijo,
                        onValueChange = { nombreGastoFijo = it },
                        label = { Text("Ej: Pago de Renta, Luz, Internet") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = costoGastoFijo,
                        onValueChange = { costoGastoFijo = it },
                        label = { Text("Costo") },
                        prefix = { Text("$ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorGastoFijo.isNotEmpty()) {
                        Text(errorGastoFijo, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            val costo = costoGastoFijo.toDoubleOrNull()
                            if (nombreGastoFijo.isBlank() || costo == null || costo <= 0) {
                                errorGastoFijo = "Ingresa un nombre y costo válidos."
                            } else {
                                errorGastoFijo = ""
                                // Añadir el gasto fijo directamente a la lista con una bandera especial
                                listaGastos.add(0, GastoItem(
                                    id = System.currentTimeMillis().toString(),
                                    nombre = nombreGastoFijo,
                                    precio = costo,
                                    esFijo = true
                                ))
                                // Limpiar los campos del formulario
                                nombreGastoFijo = ""
                                costoGastoFijo = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Añadir a la lista")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Barra de búsqueda de productos
        item {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar en la lista...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Renderizado dinámico de la lista (Gastos fijos agregados + sugerencias de productos)
        items(listaFiltrada, key = { it.id }) { itemGasto ->
            ItemGastoCard(
                gasto = itemGasto,
                onAceptar = {
                    viewModel.registrarNuevoGasto(itemGasto.precio)
                    listaGastos.remove(itemGasto)
                },
                onRechazar = {
                    listaGastos.remove(itemGasto)
                }
            )
        }

        // Botón final de navegación para avanzar al Resumen
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate(Screen.Summary.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Finalizar y Ver Balance 📊", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ItemGastoCard(gasto: GastoItem, onAceptar: () -> Unit, onRechazar: () -> Unit) {
    // Si es un gasto fijo, le ponemos un fondo ligeramente distinto para diferenciarlo
    val colorContenedor = if (gasto.esFijo) {
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorContenedor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (gasto.esFijo) "🏠 ${gasto.nombre}" else gasto.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (gasto.esFijo) "Monto Fijo: $${gasto.precio}" else "Precio Estimado: $${gasto.precio}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilledIconButton(
                    onClick = onRechazar,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Rechazar")
                }

                FilledIconButton(
                    onClick = onAceptar,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Agregar")
                }
            }
        }
    }
}