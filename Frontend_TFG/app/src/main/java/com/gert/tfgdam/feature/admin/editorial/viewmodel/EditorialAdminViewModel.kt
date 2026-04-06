package com.gert.tfgdam.feature.admin.editorial.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.core.network.ApiError
import com.gert.tfgdam.feature.admin.editorial.model.Editorial
import com.gert.tfgdam.feature.admin.editorial.repository.EditorialRepository
import com.google.gson.Gson
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class EditorialAdminViewModel : ViewModel() {
    private val repository = EditorialRepository()

    var nombreEditorial by mutableStateOf("")
    var idEditorial by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    var buscador by mutableStateOf("")
        private set
    private val buscadorFlow = MutableStateFlow("")
    var isLoadingBusqueda by mutableStateOf(false)

    var editoriales by mutableStateOf<List<Editorial>>(emptyList())
        private set
    var editorialesFiltradas by mutableStateOf<List<Editorial>>(emptyList())
        private set
    init {
        cargarEditoriales()
        observarBuscador()
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

    fun onBuscadorChange(texto: String) {
        buscador = texto
        buscadorFlow.value = texto
        if (texto.isNotEmpty()) {
            isLoadingBusqueda = true
        }
    }

    @OptIn(FlowPreview::class)
    private fun observarBuscador() {
        viewModelScope.launch {
            buscadorFlow
                .debounce(500)
                .distinctUntilChanged()
                .collect { texto ->
                    buscarEditoriales(texto)
                }
        }
    }

    private fun buscarEditoriales(texto: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllPorBusqueda(texto)

                if (response.isSuccessful) {
                    editorialesFiltradas = (response.body() ?: emptyList()).sortedBy { it.id }
                } else {
                    editorialesFiltradas = emptyList()
                }
            } catch (e: IOException) {
                editorialesFiltradas = emptyList()
            } finally {
                isLoadingBusqueda = false
            }
        }
    }

    fun crearEditorial(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val editorialNuevo = Editorial(
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
                        jsonObject.getString("message")
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
                val editorialActualizada = Editorial(
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
                        jsonObject.getString("message")
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

    fun eliminarEditorial(idEditorial: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val response = repository.delete(idEditorial)
                if (response.isSuccessful) {
                    cargarEditoriales()
                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("message")
                    } catch (e: Exception) {
                        "Error al eliminar la editorial"
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