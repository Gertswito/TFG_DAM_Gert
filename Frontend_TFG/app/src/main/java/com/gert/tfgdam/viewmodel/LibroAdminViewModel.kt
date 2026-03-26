package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.model.Libro
import com.gert.tfgdam.repository.LibroRepository
import kotlinx.coroutines.launch
import java.io.IOException

class LibroAdminViewModel : ViewModel() {
    private val repository = LibroRepository()

    var idWidth by mutableIntStateOf(0)
        private set
    var isbnWidth by mutableIntStateOf(0)
        private set
    var tituloWidth by mutableIntStateOf(0)
        private set
    var editorialWidth by mutableIntStateOf(0)
        private set
    var autorWidth by mutableIntStateOf(0)
        private set
    var tipoLibroWidth by mutableIntStateOf(0)
        private set


    var libros by mutableStateOf<List<Libro>>(emptyList())
        private set

    init {
        cargarLibros()
    }

    private fun cargarLibros() {
        viewModelScope.launch {
            try {
                val response = repository.getAll()

                if (response.isSuccessful) {
                    libros = response.body() ?: emptyList()
                    calcularAnchuras()
                } else {
                    libros = emptyList()
                }
            } catch (e: IOException) {
                libros = emptyList()
            }
        }
    }

    private fun calcularAnchuras() {
        idWidth = libros.maxOfOrNull { it.id.toString().length } ?: 0
        isbnWidth = libros.maxOfOrNull { it.isbn?.length ?: 0 } ?: 0
        tituloWidth = libros.maxOfOrNull { it.titulo?.length ?: 0 } ?: 0
        autorWidth = libros.maxOfOrNull { it.autor?.nombre?.length ?: 0 } ?: 0
        editorialWidth = libros.maxOfOrNull { it.editorial?.nombre?.length ?: 0 } ?: 0
        tipoLibroWidth = libros.maxOfOrNull { it.tipoLibro?.nombre?.length ?: 0 } ?: 0
    }

    fun cambiarDeCharacteresADp(nChars: Int): Dp {
        return maxOf((nChars * 11).dp, 60.dp)
    }
}