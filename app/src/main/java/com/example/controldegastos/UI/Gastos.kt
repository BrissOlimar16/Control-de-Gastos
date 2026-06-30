package com.example.controldegastos.UI

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class Gastos : ViewModel() {

    // Estado global para el presupuesto inicial
    var presupuestoInicial by mutableStateOf(0.0)
        private set

    // Estado global para el periodo seleccionado
    var periodoSeleccionado by mutableStateOf("Mensual")
        private set

    // Estado global para acumular el total de los gastos aceptados en las tarjetas
    var totalGastado by mutableStateOf(0.0)
        private set

    // Funciones para modificar los estados desde las pantallas
    fun guardarConfiguracionInicial(monto: Double, periodo: String) {
        presupuestoInicial = monto
        periodoSeleccionado = periodo
        totalGastado = 0.0 // Reinicia el contador de gastos al crear un plan nuevo
    }

    fun registrarNuevoGasto(monto: Double) {
        totalGastado += monto
    }

    // Propiedad calculada automáticamente (Lógica de Negocio)
    val saldoRestante: Double
        get() = presupuestoInicial - totalGastado
}