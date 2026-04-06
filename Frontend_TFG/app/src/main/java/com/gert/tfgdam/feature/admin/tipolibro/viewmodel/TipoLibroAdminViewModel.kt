package com.gert.tfgdam.feature.admin.tipolibro.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.core.network.ApiError
import com.gert.tfgdam.feature.admin.tipolibro.model.TipoLibro
import com.gert.tfgdam.feature.admin.tipolibro.repository.TipoLibroRepository
import com.google.gson.Gson
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class TipoLibroAdminViewModel : ViewModel() {
    private val repository = TipoLibroRepository()

    var nombreTipoLibro by mutableStateOf("")
    var idTipoLibro by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    var buscador by mutableStateOf("")
        private set
    private val buscadorFlow = MutableStateFlow("")
    var isLoadingBusqueda by mutableStateOf(false)

    var tiposlibros by mutableStateOf<List<TipoLibro>>(emptyList())
        private set
    var tiposlibrosFiltrados by mutableStateOf<List<TipoLibro>>(emptyList())
        private set
    init {
        cargarTiposlibros()
        observarBuscador()
    }

    private fun cargarTiposlibros() {
        viewModelScope.launch {
            try {
                val response = repository.getAll()

                if (response.isSuccessful) {
                    tiposlibros = (response.body() ?: emptyList()).sortedBy { it.id }
                } else {
                    tiposlibros = emptyList()
                }
            } catch (e: IOException) {
                tiposlibros = emptyList()
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
                    buscarTiposLibros(texto)
                }
        }
    }

    private fun buscarTiposLibros(texto: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllPorBusqueda(texto)

                if (response.isSuccessful) {
                    tiposlibrosFiltrados = (response.body() ?: emptyList()).sortedBy { it.id }
                } else {
                    tiposlibrosFiltrados = emptyList()
                }
            } catch (e: IOException) {
                tiposlibrosFiltrados = emptyList()
            } finally {
                isLoadingBusqueda = false
            }
        }
    }

    fun crearTipoLibro(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val tipoLibroNuevo = TipoLibro(
                    nombre = nombreTipoLibro.trim()
                )

                if (nombreTipoLibro.isEmpty()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                val response = repository.create(tipoLibroNuevo)
                if (response.isSuccessful) {
                    cargarTiposlibros()
                    restaurarCamposTipoLibro(null)

                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("message")
                    } catch (e: Exception) {
                        "Error al crear el tipo de libro"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposTipoLibro(null)
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun editarTipoLibro(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val tipoLibroActualizado = TipoLibro(
                    id = idTipoLibro.toLong(),
                    nombre = nombreTipoLibro.trim()
                )

                if (idTipoLibro.isEmpty() || nombreTipoLibro.isEmpty()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                val response = repository.update(idTipoLibro.toLong(), tipoLibroActualizado)
                if (response.isSuccessful) {
                    cargarTiposlibros()
                    restaurarCamposTipoLibro(null)

                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("message")
                    } catch (e: Exception) {
                        "Error al editar el tipo de libro"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposTipoLibro(null)
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun eliminarTipoLibro(idTipoLibro: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val response = repository.delete(idTipoLibro)
                if (response.isSuccessful) {
                    cargarTiposlibros()
                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("message")
                    } catch (e: Exception) {
                        "Error al eliminar el tipo de libro"
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

    fun restaurarCamposTipoLibro(tipoLibro: TipoLibro? = null) {
        if (tipoLibro != null) {
            nombreTipoLibro = tipoLibro.nombre ?: ""
            idTipoLibro = tipoLibro.id.toString() ?: ""
        } else {
            nombreTipoLibro = ""
            idTipoLibro = ""
        }
    }
}