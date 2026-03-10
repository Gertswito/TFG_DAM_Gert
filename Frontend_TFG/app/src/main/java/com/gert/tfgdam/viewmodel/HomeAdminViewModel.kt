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

class HomeAdminViewModel : ViewModel() {
    private val repository = LibroRepository()

    var addStock by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    var libros by mutableStateOf<List<Libro>>(emptyList())
        private set

    init {
        cargarLibros()
    }

    private fun cargarLibros() {
        viewModelScope.launch {
            try {
                val response = repository.getAllLimitadoPorStock()

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

    fun actualizarStock(libro: Libro, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val libroId = libro.id

                if (libroId == null) {
                    errorMessage = "No se ha podido actualizar el stock"
                    isLoading = false
                    addStock = ""
                    return@launch
                }

                if(addStock.toIntOrNull() == null){
                    errorMessage = "El stock introducido debe ser un número"
                    isLoading = false
                    addStock = ""
                    return@launch
                }

                val stock = addStock.toInt()

                val response = repository.actualizarStock(libroId, stock)
                if(response.isSuccessful) {
                    cargarLibros()
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error al actualizar el stock"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
                addStock = ""
            }
        }
    }
}