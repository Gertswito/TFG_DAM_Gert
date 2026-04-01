package com.gert.tfgdam.feature.user.libro.novedades.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.admin.libro.repository.LibroRepository
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class LibrosNovedadesViewModel : ViewModel() {
    private val repository = LibroRepository()

    var mesActual by mutableStateOf<String>("NINGUNO")
    var librosNovedadesPorMes by mutableStateOf<List<Libro>>(emptyList())
        private set
    var librosNovedadesPorUltimaAdicion by mutableStateOf<List<Libro>>(emptyList())
        private set
    init {
        cargarLibrosNovedadesPorMes()
        cargarLibrosNovedadesPorUltimaAdicion()
    }

    private fun cargarLibrosNovedadesPorMes() {
        viewModelScope.launch {
            try {
                val response = repository.getAllNovedadesPorMes()

                if (response.isSuccessful) {
                    librosNovedadesPorMes = response.body() ?: emptyList()

                    val mes = LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-ES"))
                    mesActual = mes.replaceFirstChar { it.uppercase() }
                } else {
                    librosNovedadesPorMes = emptyList()
                }
            } catch (e: IOException) {
                librosNovedadesPorMes = emptyList()
            }
        }
    }

    private fun cargarLibrosNovedadesPorUltimaAdicion() {
        viewModelScope.launch {
            try {
                val response = repository.getAllNovedadesPorUltimaAdicion()

                if (response.isSuccessful) {
                    librosNovedadesPorUltimaAdicion = response.body() ?: emptyList()
                } else {
                    librosNovedadesPorUltimaAdicion = emptyList()
                }
            } catch (e: IOException) {
                librosNovedadesPorUltimaAdicion = emptyList()
            }
        }
    }
}