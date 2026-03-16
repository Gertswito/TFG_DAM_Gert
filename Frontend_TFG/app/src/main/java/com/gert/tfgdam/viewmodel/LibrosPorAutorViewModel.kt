package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.model.Libro
import com.gert.tfgdam.repository.LibroRepository
import kotlinx.coroutines.launch

class LibrosPorAutorViewModel : ViewModel() {
    private val repository = LibroRepository()
    var librosPorAutor by mutableStateOf<List<Libro>>(emptyList())
        private set

    fun cargarLibrosPorAutor(autor: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllPorNombreDeAutor(autor)

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
}