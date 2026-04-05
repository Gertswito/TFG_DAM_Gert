package com.gert.tfgdam.feature.user.libro.portipo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.admin.libro.repository.LibroRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.io.IOException

class TipoLibroGenerosViewModel : ViewModel() {
    private val repository = LibroRepository()

    var tipoLibroSeleccionado by mutableStateOf<String?>(null)

    var buscador by mutableStateOf("")
        private set
    private val buscadorFlow = MutableStateFlow("")
    var isLoadingBusqueda by mutableStateOf(false)

    var librosPorTipo by mutableStateOf<List<Libro>>(emptyList())
        private set
    var librosFiltradosPorTipo by mutableStateOf<List<Libro>>(emptyList())
        private set
    init {
        observarBuscador()
    }

    fun cargarLibrosPorTipo(tipoLibro: String) {
        viewModelScope.launch {
            try {
                tipoLibroSeleccionado = tipoLibro
                val response = repository.getAllPorTipo(tipoLibroSeleccionado ?: "")

                if (response.isSuccessful) {
                    librosPorTipo = response.body() ?: emptyList()
                } else {
                    librosPorTipo = emptyList()
                }

            } catch (e: Exception) {
                librosPorTipo = emptyList()
            }
        }
    }

    fun onBuscadorChange(texto: String) {
        buscador = texto
        buscadorFlow.value = texto
        if (texto.isNotEmpty()) {
            isLoadingBusqueda = true
        }
    }

    @OptIn(FlowPreview::class)
    private fun observarBuscador() {
        viewModelScope.launch {
            buscadorFlow
                .debounce(500)
                .distinctUntilChanged()
                .collect { texto ->
                    buscarLibrosPorTipo(texto, tipoLibroSeleccionado ?: "")
                }
        }
    }

    private fun buscarLibrosPorTipo(texto: String, tipoLibro: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllPorBusquedaTipo(texto, tipoLibro)

                if (response.isSuccessful) {
                    librosFiltradosPorTipo = response.body() ?: emptyList()
                } else {
                    librosFiltradosPorTipo = emptyList()
                }
            } catch (e: IOException) {
                librosFiltradosPorTipo = emptyList()
            } finally {
                isLoadingBusqueda = false
            }
        }
    }
}