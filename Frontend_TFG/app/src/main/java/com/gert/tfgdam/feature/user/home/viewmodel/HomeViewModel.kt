package com.gert.tfgdam.feature.user.home.viewmodel

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

class HomeViewModel : ViewModel() {
    private val repository = LibroRepository()

    var buscador by mutableStateOf("")
        private set
    private val buscadorFlow = MutableStateFlow("")
    var isLoadingBusqueda by mutableStateOf(false)

    var libros by mutableStateOf<List<Libro>>(emptyList())
        private set
    var librosFiltrados by mutableStateOf<List<Libro>>(emptyList())
        private set
    init {
        cargarLibros()
        observarBuscador()
    }

    private fun cargarLibros() {
        viewModelScope.launch {
            try {
                val response = repository.getAllLimitadoParaView()

                if (response.isSuccessful) {
                    libros = response.body() ?: emptyList()
                } else {
                    libros = emptyList()
                }
            } catch (e: IOException) {
                libros = emptyList()
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
                    buscarLibros(texto)
                }
        }
    }

    private fun buscarLibros(texto: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllPorBusquedaUser(texto)

                if (response.isSuccessful) {
                    librosFiltrados = response.body() ?: emptyList()
                } else {
                    librosFiltrados = emptyList()
                }
            } catch (e: IOException) {
                librosFiltrados = emptyList()
            } finally {
                isLoadingBusqueda = false
            }
        }
    }
}