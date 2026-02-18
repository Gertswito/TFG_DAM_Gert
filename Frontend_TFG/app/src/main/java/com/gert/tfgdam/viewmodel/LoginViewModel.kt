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

                val token: String = repository.login(cliente)

                JwtManager.saveToken(context, token)

                val payload = JwtManager.getUserInfoFromToken(token)
                val role = payload?.rol

                successMessage = "Login exitoso"
                delay(300)

                when (role) {
                    "ADMIN" -> onSuccessAdmin()
                    "USER" -> onSuccessUser()
                    else -> errorMessage = "Rol desconocido"
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }
}