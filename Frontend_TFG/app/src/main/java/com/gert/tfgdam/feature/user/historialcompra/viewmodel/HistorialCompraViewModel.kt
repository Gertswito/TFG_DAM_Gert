package com.gert.tfgdam.feature.user.historialcompra.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.feature.admin.lineaventa.model.LineaVenta
import com.gert.tfgdam.feature.admin.lineaventa.repository.LineaVentaRepository
import com.gert.tfgdam.feature.admin.venta.model.Venta
import com.gert.tfgdam.feature.admin.venta.repository.VentaRepository
import kotlinx.coroutines.launch
import java.io.IOException

class HistorialCompraViewModel : ViewModel() {
    private val lineaVentarepository = LineaVentaRepository()
    private val ventaRepository = VentaRepository()

    var listaVentaHistorial by mutableStateOf<List<Venta>>(emptyList())
        private set
    var lineasPorVenta by mutableStateOf<Map<Long, List<LineaVenta>>>(emptyMap())
        private set

    fun cargarVentasHistorial(usuario: String) {
        viewModelScope.launch {
            try {
                val response = ventaRepository.getAllPorUsuario(usuario)

                if (response.isSuccessful) {
                    listaVentaHistorial = response.body() ?: emptyList()
                } else {
                    listaVentaHistorial = emptyList()
                }
            } catch (e: IOException) {
                listaVentaHistorial = emptyList()
            }
        }
    }

    fun cargarLineasVentasHistorial(ventaId: Long) {
        if (lineasPorVenta.containsKey(ventaId)) return

        viewModelScope.launch {
            try {
                val response = lineaVentarepository.getAllPorVenta(ventaId)

                if (response.isSuccessful) {
                    val nuevas = response.body() ?: emptyList()

                    lineasPorVenta = lineasPorVenta.toMutableMap().apply {
                        put(ventaId, nuevas)
                    }
                }
            } catch (_: IOException) {}
        }
    }
}