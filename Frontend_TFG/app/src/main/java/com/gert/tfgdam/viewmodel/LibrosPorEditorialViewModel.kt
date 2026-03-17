package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.model.Libro
import com.gert.tfgdam.repository.LibroRepository
import kotlinx.coroutines.launch

class LibrosPorEditorialViewModel : ViewModel() {
    private val repository = LibroRepository()
    var librosPorEditorial by mutableStateOf<List<Libro>>(emptyList())
        private set

    fun cargarLibrosPorEditorial(editorial: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllPorNombreDeEditorial(editorial)

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
}