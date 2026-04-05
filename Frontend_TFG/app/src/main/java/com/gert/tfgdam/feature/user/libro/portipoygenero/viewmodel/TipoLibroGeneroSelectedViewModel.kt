package com.gert.tfgdam.feature.user.libro.portipoygenero.viewmodel

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

class TipoLibroGeneroSelectedViewModel : ViewModel() {
    private val repository = LibroRepository()

    var tipoLibroSeleccionado by mutableStateOf<String?>(null)
    var generoSeleccionado by mutableStateOf<String?>(null)

    var buscador by mutableStateOf("")
        private set
    private val buscadorFlow = MutableStateFlow("")
    var isLoadingBusqueda by mutableStateOf(false)

    var librosPorTipoGenero by mutableStateOf<List<Libro>>(emptyList())
        private set
    var librosFiltradosPorTipoGenero by mutableStateOf<List<Libro>>(emptyList())
        private set
    init {
        observarBuscador()
    }

    fun cargarLibrosPorTipoGenero(tipoLibro: String, genero: String) {
        viewModelScope.launch {
            try {
                tipoLibroSeleccionado = tipoLibro
                generoSeleccionado = genero
                val response = repository.getAllPorTipoGenero(tipoLibroSeleccionado ?: "", generoSeleccionado ?: "")

                if (response.isSuccessful) {
                    librosPorTipoGenero = response.body() ?: emptyList()
                } else {
                    librosPorTipoGenero = emptyList()
                }

            } catch (e: Exception) {
                librosPorTipoGenero = emptyList()
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
                    buscarLibrosPorTipoGenero(texto, tipoLibroSeleccionado ?: "", generoSeleccionado ?: "")
                }
        }
    }

    private fun buscarLibrosPorTipoGenero(texto: String, tipoLibro: String, genero: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllPorBusquedaTipoGenero(texto, tipoLibro, genero)

                if (response.isSuccessful) {
                    librosFiltradosPorTipoGenero = response.body() ?: emptyList()
                } else {
                    librosFiltradosPorTipoGenero = emptyList()
                }
            } catch (e: IOException) {
                librosFiltradosPorTipoGenero = emptyList()
            } finally {
                isLoadingBusqueda = false
            }
        }
    }
}