package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.api.ApiError
import com.gert.tfgdam.model.Cliente
import com.gert.tfgdam.model.Libro
import com.gert.tfgdam.repository.LibroRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okio.IOException
import org.json.JSONObject

class LibroDetailsViewModel : ViewModel() {
    private val repository = LibroRepository()

    var libroEspecifico by mutableStateOf<Libro?>(null)
        private set

    var isLibroYaDeseado by mutableStateOf(false)

    var isLoadingDeseado by mutableStateOf(false)
    var errorMessageDeseado by mutableStateOf("")

    fun cargarLibrosDetail(libroId: Long) {
        viewModelScope.launch {
            isLoadingDeseado = true
            try {
                val response = repository.getPorId(libroId)

                if (response.isSuccessful) {
                    libroEspecifico = response.body() ?: null
                } else {
                    libroEspecifico = null
                }

            } catch (e: Exception) {
                libroEspecifico = null
            } finally {
                isLoadingDeseado = false
            }
        }
    }

    fun buscarLibroEnListaDeseados(libroId: Long, usuario: String) {
        viewModelScope.launch {
            try {
                val response = repository.getLibroEnListaDeseados(libroId, usuario)
                if (response.isSuccessful()) {
                    isLibroYaDeseado = true
                } else {
                    isLibroYaDeseado = false
                }
            } catch (e: IOException) {
                isLibroYaDeseado = false
            }
        }
    }

    fun addLibroListaDeseados(libroSeleccionado: Libro, usuario: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoadingDeseado = true
            errorMessageDeseado = ""

            try {
                val libroId = libroSeleccionado.id
                val cliente = Cliente(
                    usuario = usuario.trim()
                )

                if(libroId == null) {
                    errorMessageDeseado = "No se ha cargado el libro"
                    isLoadingDeseado = false
                    return@launch
                }

                if(cliente.usuario.isNullOrBlank()) {
                    errorMessageDeseado = "No se ha cargado el usuario"
                    isLoadingDeseado = false
                    return@launch
                }

                val response = repository.addLibroListaDeseados(libroId, cliente)
                if(response.isSuccessful) {
                    isLibroYaDeseado = true
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessageDeseado = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error en al añadir a la lista"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                errorMessageDeseado = apiError.message ?: "Error desconocido"
            } finally {
                isLoadingDeseado = false
            }
        }
    }

    fun deleteLibroListaDeseados(libroSeleccionado: Libro, usuario: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoadingDeseado = true
            errorMessageDeseado = ""

            try {
                val libroId = libroSeleccionado.id
                val usuarioLogueado = usuario.trim()

                if(libroId == null) {
                    errorMessageDeseado = "No se ha cargado el libro"
                    isLoadingDeseado = false
                    return@launch
                }

                if(usuarioLogueado.isNullOrBlank()) {
                    errorMessageDeseado = "No se ha cargado el usuario"
                    isLoadingDeseado = false
                    return@launch
                }

                val response = repository.deleteLibroListaDeseados(libroId, usuarioLogueado)
                if(response.isSuccessful) {
                    isLibroYaDeseado = false
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessageDeseado = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error en al eliminar de la lista"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                errorMessageDeseado = apiError.message ?: "Error desconocido"
            } finally {
                isLoadingDeseado = false
            }
        }
    }
}