package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.model.LineaVenta
import com.gert.tfgdam.repository.LineaVentaRepository
import kotlinx.coroutines.launch
import java.io.IOException

class HistorialCompraViewModel : ViewModel() {
    private val repository = LineaVentaRepository()

    var listaHistorial by mutableStateOf<List<LineaVenta>>(emptyList())
        private set

    fun cargarHistorial(usuario: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllPorUsuario(usuario)

                if (response.isSuccessful) {
                    listaHistorial = response.body() ?: emptyList()
                } else {
                    listaHistorial = emptyList()
                }
            } catch (e: IOException) {
                listaHistorial = emptyList()
            }
        }
    }
}