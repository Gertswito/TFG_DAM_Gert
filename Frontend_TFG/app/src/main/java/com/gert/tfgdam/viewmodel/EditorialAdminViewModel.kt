package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.api.ApiError
import com.gert.tfgdam.model.Editorial
import com.gert.tfgdam.repository.EditorialRepository
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class EditorialAdminViewModel : ViewModel() {
    private val repository = EditorialRepository()

    var nombreEditorial by mutableStateOf("")
    var idEditorial by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    var editoriales by mutableStateOf<List<Editorial>>(emptyList())
        private set
    init {
        cargarEditoriales()
    }

    private fun cargarEditoriales() {
        viewModelScope.launch {
            try {
                val response = repository.getAll()

                if (response.isSuccessful) {
                    editoriales = (response.body() ?: emptyList()).sortedBy { it.id }
                } else {
                    editoriales = emptyList()
                }
            } catch (e: IOException) {
                editoriales = emptyList()
            }
        }
    }

    fun crearEditorial(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val editorialNuevo = Editorial (
                    nombre = nombreEditorial.trim()
                )

                if (nombreEditorial.isEmpty()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                val response = repository.create(editorialNuevo)
                if (response.isSuccessful) {
                    cargarEditoriales()
                    restaurarCamposEditorial(null)

                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error al crear la editorial"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposEditorial(null)
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun editarEditorial(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val editorialActualizada = Editorial (
                    id = idEditorial.toLong(),
                    nombre = nombreEditorial.trim()
                )

                if (idEditorial.isEmpty() || nombreEditorial.isEmpty()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                val response = repository.update(idEditorial.toLong(), editorialActualizada)
                if (response.isSuccessful) {
                    cargarEditoriales()
                    restaurarCamposEditorial(null)

                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error al editar la editorial"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposEditorial(null)
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun restaurarCamposEditorial(editorial: Editorial? = null) {
        if (editorial != null) {
            nombreEditorial = editorial.nombre ?: ""
            idEditorial = editorial.id.toString() ?: ""
        } else {
            nombreEditorial = ""
            idEditorial = ""
        }
    }
}