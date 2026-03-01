package com.gert.tfgdam.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.model.Cliente
import com.gert.tfgdam.repository.ClienteRepository
import com.gert.tfgdam.util.JwtManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel
import com.gert.tfgdam.api.ApiError
import com.google.gson.Gson

class LoginViewModel(application: Application) : AndroidViewModel(application)  {
    private val repository = ClienteRepository()
    private val context = getApplication<Application>()

    var usuario by mutableStateOf("")
    var contrasenha by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var successMessage by mutableStateOf("")

    fun login(onSuccessUser: () -> Unit = {}, onSuccessAdmin: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            successMessage = ""

            try {
                val cliente = Cliente(
                    usuario = usuario.trim(),
                    contrasenha = contrasenha.trim()
                )

                if (cliente.usuario.isNullOrBlank() || cliente.contrasenha.isNullOrBlank()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                val token: String = repository.login(cliente)

                JwtManager.saveToken(context, token)

                val payload = JwtManager.getUserInfoFromToken(token)
                val rol = payload?.rol

                successMessage = "Login exitoso"
                delay(500)

                when (rol) {
                    "ADMIN" -> onSuccessAdmin()
                    "USER" -> onSuccessUser()
                    else -> errorMessage = "Rol desconocido"
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                if(apiError.status != 401) {
                    usuario = ""
                }
                contrasenha = ""

                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }
}