package com.gert.tfgdam.feature.user.libro.portipo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.admin.libro.repository.LibroRepository
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