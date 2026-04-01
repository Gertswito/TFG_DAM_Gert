package com.gert.tfgdam.feature.admin.genero.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.core.network.ApiError
import com.gert.tfgdam.feature.admin.genero.model.Genero
import com.gert.tfgdam.feature.admin.genero.repository.GeneroRepository
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class GeneroAdminViewModel : ViewModel() {
    private val repository = GeneroRepository()

    var nombreGenero by mutableStateOf("")
    var idGenero by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    var generos by mutableStateOf<List<Genero>>(emptyList())
        private set
    init {
        cargarGeneros()
    }

    private fun cargarGeneros() {
        viewModelScope.launch {
            try {
                val response = repository.getAll()

                if (response.isSuccessful) {
                    generos = (response.body() ?: emptyList()).sortedBy { it.id }
                } else {
                    generos = emptyList()
                }
            } catch (e: IOException) {
                generos = emptyList()
            }
        }
    }

    fun crearGenero(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val generoNuevo = Genero(
                    nombre = nombreGenero.trim()
                )

                if (nombreGenero.isEmpty()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                val response = repository.create(generoNuevo)
                if (response.isSuccessful) {
                    cargarGeneros()
                    restaurarCamposGenero(null)

                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error al crear el género"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposGenero(null)
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun editarGenero(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val generoActualizado = Genero(
                    id = idGenero.toLong(),
                    nombre = nombreGenero.trim()
                )

                if (idGenero.isEmpty() || nombreGenero.isEmpty()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                val response = repository.update(idGenero.toLong(), generoActualizado)
                if (response.isSuccessful) {
                    cargarGeneros()
                    restaurarCamposGenero(null)

                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error al editar el género"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposGenero(null)
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun restaurarCamposGenero(genero: Genero? = null) {
        if (genero != null) {
            nombreGenero = genero.nombre ?: ""
            idGenero = genero.id.toString() ?: ""
        } else {
            nombreGenero = ""
            idGenero = ""
        }
    }
}