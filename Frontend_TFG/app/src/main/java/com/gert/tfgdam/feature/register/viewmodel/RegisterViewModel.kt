package com.gert.tfgdam.feature.register.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.core.network.ApiError
import com.gert.tfgdam.feature.admin.cliente.model.Cliente
import com.gert.tfgdam.feature.admin.cliente.repository.ClienteRepository
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

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
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            successMessage = ""

            try {
                val cliente = Cliente(
                    usuario = usuario.trim().lowercase(),
                    nombre = nombre.trim(),
                    apellidos = apellidos.trim(),
                    email = email.trim(),
                    contrasenha = contrasenha.trim()
                )

                if (cliente.usuario.isNullOrBlank() || cliente.nombre.isNullOrBlank() || cliente.apellidos.isNullOrBlank() || cliente.email.isNullOrBlank() || cliente.contrasenha.isNullOrBlank()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                if (!isEmailValid(email.trim())) {
                    errorMessage = "Correo no válido, vuelva a intentarlo"
                    isLoading = false
                    return@launch
                }

                val response = repository.create(cliente)
                if (response.isSuccessful) {
                    successMessage = "Usuario registrado exitosamente"

                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error en el registro"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                usuario = ""
                nombre = ""
                apellidos = ""
                email = ""
                contrasenha = ""

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