package com.gert.tfgdam.feature.user.libro.detail.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.admin.libro.repository.LibroRepository
import com.gert.tfgdam.feature.user.listadeseados.viewmodel.ListaDeseadosViewModel
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