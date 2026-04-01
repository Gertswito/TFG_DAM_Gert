package com.gert.tfgdam.feature.user.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.admin.libro.repository.LibroRepository
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel : ViewModel() {
    private val repository = LibroRepository()

    var libros by mutableStateOf<List<Libro>>(emptyList())
    private set
    init {
        cargarLibros()
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
}