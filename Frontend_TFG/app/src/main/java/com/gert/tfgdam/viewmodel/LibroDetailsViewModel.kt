package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.model.Libro
import com.gert.tfgdam.repository.LibroRepository
import kotlinx.coroutines.launch

class LibroDetailsViewModel : ViewModel() {
    private val repository = LibroRepository()
    private val listaDeseadosViewModel = ListaDeseadosViewModel()
    var libroEspecifico by mutableStateOf<Libro?>(null)
        private set

    fun cargarLibrosDetail(libroId: Long) {
        viewModelScope.launch {
            listaDeseadosViewModel.isLoading = true
            try {
                val response = repository.getPorId(libroId)

                if (response.isSuccessful) {
                    libroEspecifico = response.body() ?: null
                } else {
                    libroEspecifico = null
                }

            } catch (e: Exception) {
                libroEspecifico = null
            } finally {
                listaDeseadosViewModel.isLoading = false
            }
        }
    }
}