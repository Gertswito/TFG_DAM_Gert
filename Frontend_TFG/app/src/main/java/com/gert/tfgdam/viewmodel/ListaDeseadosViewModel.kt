package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.api.ApiError
import com.gert.tfgdam.model.Libro
import com.gert.tfgdam.repository.LibroRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class ListaDeseadosViewModel : ViewModel() {
    private val repository = LibroRepository()

    var libros by mutableStateOf<List<Libro>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun cargarLibrosListaDeseados(usuario: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllListaDeseados(usuario)

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

    fun deleteLibroListaDeseados(libroSeleccionado: Libro, usuario: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val libroId = libroSeleccionado.id
                val usuarioLogueado = usuario.trim()

                if(libroId == null) {
                    errorMessage = "No se ha cargado el libro"
                    isLoading = false
                    return@launch
                }

                if(usuarioLogueado.isNullOrBlank()) {
                    errorMessage = "No se ha cargado el usuario"
                    isLoading = false
                    return@launch
                }

                val response = repository.deleteLibroListaDeseados(libroId, usuarioLogueado)
                if(response.isSuccessful) {
                    onSuccess()
                    cargarLibrosListaDeseados(usuario)
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error en al eliminar de la lista"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }
}