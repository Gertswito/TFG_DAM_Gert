package com.gert.tfgdam.feature.user.libro.porautor.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.admin.libro.repository.LibroRepository
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