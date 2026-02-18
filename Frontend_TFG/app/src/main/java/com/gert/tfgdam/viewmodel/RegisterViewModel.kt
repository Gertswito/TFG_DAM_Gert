package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.model.Cliente
import com.gert.tfgdam.repository.ClienteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val repository = ClienteRepository()

    var usuario by mutableStateOf("")
    var nombre by mutableStateOf("")
    var apellidos by mutableStateOf("")
    var email by mutableStateOf("")
    var contrasenha by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var successMessage by mutableStateOf("")

    fun register(onSuccess: () -> Unit = {}) {
        if (!isEmailValid(email.trim())) {
            errorMessage = "Correo no válido"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            successMessage = ""

            try {
                val cliente = Cliente(
                    usuario = usuario.trim(),
                    nombre = nombre.trim(),
                    apellidos = apellidos.trim(),
                    email = email.trim(),
                    contrasenha = contrasenha.trim()
                )

                val response = repository.create(cliente)
                if (response.isSuccessful) {
                    successMessage = "Usuario registrado exitosamente"

                    delay(1000)
                    onSuccess()
                } else {
                    errorMessage = "Error en el registro: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Error desconocido"
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