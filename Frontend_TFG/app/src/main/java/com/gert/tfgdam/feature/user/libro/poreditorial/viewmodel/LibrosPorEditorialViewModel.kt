package com.gert.tfgdam.feature.user.libro.poreditorial.viewmodel

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

class LibrosPorEditorialViewModel : ViewModel() {
    private val repository = LibroRepository()

    var editorialSeleccionada by mutableStateOf("")

    var buscador by mutableStateOf("")
        private set
    private val buscadorFlow = MutableStateFlow("")
    var isLoadingBusqueda by mutableStateOf(false)

    var librosPorEditorial by mutableStateOf<List<Libro>>(emptyList())
        private set
    var librosPorEditorialFiltrados by mutableStateOf<List<Libro>>(emptyList())
        private set
    init {
        observarBuscador()
    }

    fun cargarLibrosPorEditorial(editorial: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllPorNombreDeEditorial(editorial)

                editorialSeleccionada = editorial
                if (response.isSuccessful) {
                    librosPorEditorial = response.body() ?: emptyList()
                } else {
                    librosPorEditorial = emptyList()
                }
            } catch (e: Exception) {
                librosPorEditorial = emptyList()
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
                    buscarLibrosPorEditorial(editorialSeleccionada, texto)
                }
        }
    }

    fun buscarLibrosPorEditorial(editorial: String, texto: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllPorNombreDeEditorialBusqueda(editorial, texto)

                if (response.isSuccessful) {
                    librosPorEditorialFiltrados = response.body() ?: emptyList()
                } else {
                    librosPorEditorialFiltrados = emptyList()
                }
            } catch (e: Exception) {
                librosPorEditorialFiltrados = emptyList()
            } finally {
                isLoadingBusqueda = false
            }
        }
    }
}