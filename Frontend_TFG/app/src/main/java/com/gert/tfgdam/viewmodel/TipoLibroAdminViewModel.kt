package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.model.TipoLibro
import com.gert.tfgdam.repository.TipoLibroRepository
import kotlinx.coroutines.launch
import java.io.IOException

class TipoLibroAdminViewModel : ViewModel() {
    private val repository = TipoLibroRepository()

    var tiposlibros by mutableStateOf<List<TipoLibro>>(emptyList())
        private set
    init {
        cargarTiposlibros()
    }

    private fun cargarTiposlibros() {
        viewModelScope.launch {
            try {
                val response = repository.getAll()

                if (response.isSuccessful) {
                    tiposlibros = response.body() ?: emptyList()
                } else {
                    tiposlibros = emptyList()
                }
            } catch (e: IOException) {
                tiposlibros = emptyList()
            }
        }
    }
}