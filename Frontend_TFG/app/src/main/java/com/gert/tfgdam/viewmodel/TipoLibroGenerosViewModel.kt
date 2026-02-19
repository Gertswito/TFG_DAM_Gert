package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.model.Libro
import com.gert.tfgdam.repository.LibroRepository
import kotlinx.coroutines.launch

class TipoLibroGenerosViewModel : ViewModel() {
    private val repository = LibroRepository()

    var librosPorTipo by mutableStateOf<List<Libro>>(emptyList())
        private set

    fun cargarLibrosPorTipo(tipoLibro: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllPorTipo(tipoLibro)

                if (response.isSuccessful) {
                    librosPorTipo = response.body() ?: emptyList()
                } else {
                    librosPorTipo = emptyList()
                }

            } catch (e: Exception) {
                librosPorTipo = emptyList()
            }
        }
    }
}