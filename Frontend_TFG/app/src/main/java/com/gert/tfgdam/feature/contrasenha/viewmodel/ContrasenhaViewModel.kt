package com.gert.tfgdam.feature.contrasenha.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.core.network.ApiError
import com.gert.tfgdam.feature.admin.cliente.repository.ClienteRepository
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class ContrasenhaViewModel : ViewModel() {
    private val repository = ClienteRepository()

    var usuario by mutableStateOf("")
    var email by mutableStateOf("")
    var contrasenha1 by mutableStateOf("")
    var contrasenha2 by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun cambiarContrasenha(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val usuarioTrim = usuario.trim()
                val emailTrim = email.trim()
                val contrasenha1Trim = contrasenha1.trim()
                val contrasenha2Trim = contrasenha2.trim()

                if (emailTrim.isBlank() || contrasenha1Trim.isBlank() || contrasenha2Trim.isBlank() || usuarioTrim.isBlank()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                if (!isEmailValid(emailTrim)) {
                    errorMessage = "Correo no válido, vuelva a intentarlo"
                    isLoading = false
                    return@launch
                }

                if (contrasenha1Trim != contrasenha2Trim) {
                    errorMessage = "Las contraseñas no coinciden"
                    isLoading = false
                    return@launch
                }

                val response = repository.cambiarContrasenhaSinSesion(usuarioTrim, emailTrim, contrasenha1Trim)
                if (response.isSuccessful) {
                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("message")
                    } catch (e: Exception) {
                        "Error al cambiar la contraseña"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                usuario = ""
                contrasenha1 = ""
                contrasenha2 = ""

                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$".toRegex()
        return email.matches(emailRegex)
    }
}