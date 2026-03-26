package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.model.LineaVenta
import com.gert.tfgdam.model.Venta
import com.gert.tfgdam.repository.LineaVentaRepository
import com.gert.tfgdam.repository.VentaRepository
import kotlinx.coroutines.launch
import java.io.IOException

class VentaAdminViewModel : ViewModel() {
    private val ventaRepository = VentaRepository()
    private val lineaVentaRepository = LineaVentaRepository()

    var idWidth by mutableIntStateOf(0)
        private set
    var usuarioWidth by mutableIntStateOf(0)
        private set
    var direccionWidth by mutableIntStateOf(0)
        private set

    var ventas by mutableStateOf<List<Venta>>(emptyList())
        private set
    var lineasPorVenta by mutableStateOf<Map<Long, List<LineaVenta>>>(emptyMap())
        private set

    init {
        cargarVentas()
    }

    private fun cargarVentas() {
        viewModelScope.launch {
            try {
                val response = ventaRepository.getAll()

                if (response.isSuccessful) {
                    ventas = response.body() ?: emptyList()
                    calcularAnchuras()
                } else {
                    ventas = emptyList()
                }
            } catch (e: IOException) {
                ventas = emptyList()
            }
        }
    }

    fun cargarLineasVentasPorVenta(ventaId: Long) {
        if (lineasPorVenta.containsKey(ventaId)) return

        viewModelScope.launch {
            try {
                val response = lineaVentaRepository.getAllPorVenta(ventaId)

                if (response.isSuccessful) {
                    val nuevas = response.body() ?: emptyList()

                    lineasPorVenta = lineasPorVenta.toMutableMap().apply {
                        put(ventaId, nuevas)
                    }
                }
            } catch (_: IOException) {}
        }
    }

    fun limpiarLineasVenta(ventaId: Long) {
        lineasPorVenta = lineasPorVenta.toMutableMap().apply {
            remove(ventaId)
        }
    }

    private fun calcularAnchuras() {
        idWidth = ventas.maxOfOrNull { it.id.toString().length } ?: 0
        usuarioWidth = ventas.maxOfOrNull { it.cliente?.usuario?.length ?: 0 } ?: 0
        direccionWidth = ventas.maxOfOrNull { (it.direccion?.calle?.length ?: 0) + (it.direccion?.numero.toString().length ?: 0) + (it.direccion?.piso.toString().length ?: 0) } ?: 0
    }

    fun cambiarDeCharacteresADp(nChars: Int): Dp {
        return maxOf((nChars * 11).dp, 60.dp)
    }
}