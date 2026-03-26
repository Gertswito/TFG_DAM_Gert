package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.model.Autor
import com.gert.tfgdam.repository.AutorRepository
import kotlinx.coroutines.launch
import java.io.IOException

class AutorAdminViewModel : ViewModel() {
    private val repository = AutorRepository()

    var autores by mutableStateOf<List<Autor>>(emptyList())
        private set
    init {
        cargarAutores()
    }

    private fun cargarAutores() {
        viewModelScope.launch {
            try {
                val response = repository.getAll()

                if (response.isSuccessful) {
                    autores = response.body() ?: emptyList()
                } else {
                    autores = emptyList()
                }
            } catch (e: IOException) {
                autores = emptyList()
            }
        }
    }
}