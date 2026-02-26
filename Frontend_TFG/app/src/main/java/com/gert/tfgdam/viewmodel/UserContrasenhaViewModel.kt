package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.api.ApiError
import com.gert.tfgdam.model.Cliente
import com.gert.tfgdam.repository.ClienteRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okio.IOException
import org.json.JSONObject

class UserContrasenhaViewModel : ViewModel() {
    private val repository = ClienteRepository()

    var contrasenha by mutableStateOf("")
    var contrasenhaRepetida by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var successMessage by mutableStateOf("")

    fun cambiarContrasenha(usuario: Cliente, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            successMessage = ""

            try {
                val usuarioId = usuario.id
                val contrasenhaTrim = contrasenha.trim()
                val contrasenhaRepetidaTrim = contrasenhaRepetida.trim()


                if (contrasenhaTrim.isBlank() || contrasenhaRepetidaTrim.isBlank()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false

                    restaurarContrasenha()
                    return@launch
                }

                if (contrasenhaTrim != contrasenhaRepetidaTrim) {
                    errorMessage = "Las contraseñas no coinciden"
                    isLoading = false

                    restaurarContrasenha()
                    return@launch
                }

                if (usuarioId == null) {
                    errorMessage = "No se ha cargado el usuario"
                    isLoading = false

                    restaurarContrasenha()
                    return@launch
                }

                val response = repository.cambiarContrasenha(usuarioId, contrasenhaTrim)
                if (response.isSuccessful) {
                    successMessage = "Contraseña cambiada exitosamente"
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error al cambiar la contraseña"
                    }
                }
            } catch (e: IOException) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
                restaurarContrasenha()
            }
        }
    }

    fun restaurarContrasenha() {
        contrasenha = ""
        contrasenhaRepetida = ""
    }
}