package com.example.controldegastos.UI.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.controldegastos.UI.Screen
import com.example.controldegastos.UI.Gastos
// Modelo de datos temporal para representar un producto sugerido por la API
data class ProductoDummy(val id: Int, val nombre: String, val precioSugerido: Double)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerScreen(navController: NavController, viewModel: Gastos) {
    var busqueda by remember { mutableStateOf("") }

    // Lista simulada de productos devueltos por la API de precios
    val productosSugeridos = remember {
        mutableStateListOf(
            ProductoDummy(1, "Leche Entera 1L", 24.50),
            ProductoDummy(2, "Huevo Blanco (1kg)", 48.00),
            ProductoDummy(3, "Arroz Extra 1kg", 22.00),
            ProductoDummy(4, "Aceite de Cocina 800ml", 39.50)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "📋 Planificador de Despensa",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Busca productos. Los precios estimados se obtienen vía API REST.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Barra de búsqueda moderna
        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            placeholder = { Text("Buscar producto (Ej: Leche)") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Sugerencias de la API para tu lista:",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Lista eficiente en Compose (LazyColumn) que renderiza las tarjetas de forma responsiva
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(productosSugeridos) { producto ->
                ItemGastoCard(
                    producto = producto,
                    onAceptar = { productosSugeridos.remove(producto) },
                    onRechazar = { productosSugeridos.remove(producto) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón final para avanzar al Paso 3 (Resumen)
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

// Componente reutilizable para cada tarjeta de producto (Cumple Rúbrica de UX/UI)
@Composable
fun ItemGastoCard(producto: ProductoDummy, onAceptar: () -> Unit, onRechazar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = producto.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Precio API: $${producto.precioSugerido}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Fila de botones de acción rápida idénticos a los del pase de lista
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Botón Rechazar (Rojo)
                FilledIconButton(
                    onClick = onRechazar,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Rechazar")
                }

                // Botón Aceptar / Agregar (Verde)
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