package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.model.Libro
import com.gert.tfgdam.repository.LibroRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = LibroRepository()

    var libros by mutableStateOf<List<Libro>>(emptyList())
    private set

            init {
                cargarLibros()
            }

    private fun cargarLibros() {
        viewModelScope.launch {
            val response = repository.getAll()
            if (response.isSuccessful) {
                libros = response.body() ?: emptyList()
            }
        }
    }
}