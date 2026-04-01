package com.gert.tfgdam.feature.admin.autor.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.core.network.ApiError
import com.gert.tfgdam.feature.admin.autor.model.Autor
import com.gert.tfgdam.feature.admin.autor.repository.AutorRepository
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class AutorAdminViewModel : ViewModel() {
    private val repository = AutorRepository()

    var nombreAutor by mutableStateOf("")
    var idAutor by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

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
                    autores = (response.body() ?: emptyList()).sortedBy { it.id }
                } else {
                    autores = emptyList()
                }
            } catch (e: IOException) {
                autores = emptyList()
            }
        }
    }

    fun crearAutor(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val autorNuevo = Autor(
                    nombre = nombreAutor.trim()
                )

                if (nombreAutor.isEmpty()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                val response = repository.create(autorNuevo)
                if (response.isSuccessful) {
                    cargarAutores()
                    restaurarCamposAutor(null)

                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error al crear el autor"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposAutor(null)
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun editarAutor(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val autorActualizado = Autor(
                    id = idAutor.toLong(),
                    nombre = nombreAutor.trim()
                )

                if (idAutor.isEmpty() || nombreAutor.isEmpty()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                val response = repository.update(idAutor.toLong(), autorActualizado)
                if (response.isSuccessful) {
                    cargarAutores()
                    restaurarCamposAutor(null)

                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error al editar el autor"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposAutor(null)
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun restaurarCamposAutor(autor: Autor? = null) {
        if (autor != null) {
            nombreAutor = autor.nombre ?: ""
            idAutor = autor.id.toString() ?: ""
        } else {
            nombreAutor = ""
            idAutor = ""
        }
    }
}