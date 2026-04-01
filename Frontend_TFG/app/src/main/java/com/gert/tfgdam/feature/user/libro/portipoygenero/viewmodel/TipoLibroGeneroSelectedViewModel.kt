package com.gert.tfgdam.feature.user.libro.portipoygenero.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.admin.libro.repository.LibroRepository
import kotlinx.coroutines.launch

class TipoLibroGeneroSelectedViewModel : ViewModel() {
    private val repository = LibroRepository()

    var librosPorTipoGenero by mutableStateOf<List<Libro>>(emptyList())
        private set

    fun cargarLibrosPorTipoGenero(tipoLibro: String, genero: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllPorTipoGenero(tipoLibro, genero)

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
}