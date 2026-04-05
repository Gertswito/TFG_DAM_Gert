package com.gert.tfgdam.feature.user.libro.porautor.viewmodel

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

class LibrosPorAutorViewModel : ViewModel() {
    private val repository = LibroRepository()

    var autorSeleccionado by mutableStateOf("")

    var buscador by mutableStateOf("")
        private set
    private val buscadorFlow = MutableStateFlow("")
    var isLoadingBusqueda by mutableStateOf(false)

    var librosPorAutor by mutableStateOf<List<Libro>>(emptyList())
        private set
    var librosPorAutorFiltrados by mutableStateOf<List<Libro>>(emptyList())
        private set
    init {
        observarBuscador()
    }

    fun cargarLibrosPorAutor(autor: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllPorNombreDeAutor(autor)

                autorSeleccionado = autor
                if (response.isSuccessful) {
                    librosPorAutor = response.body() ?: emptyList()
                } else {
                    librosPorAutor = emptyList()
                }
            } catch (e: Exception) {
                librosPorAutor = emptyList()
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
                    buscarLibrosPorAutor(autorSeleccionado, texto)
                }
        }
    }

    fun buscarLibrosPorAutor(autor: String, texto: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllPorNombreDeAutorBusqueda(autor, texto)

                if (response.isSuccessful) {
                    librosPorAutorFiltrados = response.body() ?: emptyList()
                } else {
                    librosPorAutorFiltrados = emptyList()
                }
            } catch (e: Exception) {
                librosPorAutorFiltrados = emptyList()
            } finally {
                isLoadingBusqueda = false
            }
        }
    }
}