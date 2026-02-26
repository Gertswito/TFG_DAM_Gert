package com.gert.tfgdam.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.api.ApiError
import com.gert.tfgdam.model.Cliente
import com.gert.tfgdam.repository.ClienteRepository
import com.gert.tfgdam.util.JwtManager
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okio.IOException
import org.json.JSONObject

class UserSettingsViewModel() : ViewModel() {
    private val repository = ClienteRepository()

    var usuarioSesionEntero by mutableStateOf<Cliente?>(null)

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    fun cargarUsuarioSesion(context: Context) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val usuarioSesion = JwtManager.getUserInfoFlow(context).first()

                if (usuarioSesion?.sub.isNullOrBlank()) {
                    usuarioSesionEntero = null
                    errorMessage = "No se pudo cargar la información del usuario"
                    return@launch
                }

                val response = repository.getPorUsuario(usuarioSesion.sub)

                if (response.isSuccessful) {
                    usuarioSesionEntero = response.body() ?: null
                } else {
                    usuarioSesionEntero = null
                    errorMessage = "No se pudo cargar la información del usuario"
                }
            } catch (e: IOException) {
                usuarioSesionEntero = null
                errorMessage = "No se pudo cargar la información del usuario"
            } finally {
                isLoading = false
            }
        }
    }

    var nombreCambiado by mutableStateOf("")
    var apellidosCambiados by mutableStateOf("")
    var emailCambiado by mutableStateOf("")
    var isEditarClicked by mutableStateOf(false)

    var isLoadingEditar by mutableStateOf(false)
    var errorMessageEditar by mutableStateOf("")
    var successMessageEditar by mutableStateOf("")

    fun editarFormulario(usuario: Cliente, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoadingEditar = true
            errorMessageEditar = ""
            successMessageEditar = ""

            try {
                val usuarioId = usuario.id
                val emailCambiadoTrim = emailCambiado.trim()

                val clienteActualizado = Cliente(
                    id = usuario.id,
                    usuario = usuario.usuario,
                    nombre = nombreCambiado.trim(),
                    apellidos = apellidosCambiados.trim(),
                    email = emailCambiado.trim()
                )

                if (clienteActualizado.nombre.isNullOrBlank() || clienteActualizado.apellidos.isNullOrBlank() || clienteActualizado.email.isNullOrBlank()){
                    errorMessageEditar = "Por favor, rellene todos los campos"
                    isLoadingEditar = false
                    return@launch
                }

                if (!isEmailValid(emailCambiadoTrim)) {
                    errorMessageEditar = "Correo no válido, vuelva a intentarlo"
                    isLoadingEditar = false
                    return@launch
                }

                if (usuarioId == null) {
                    errorMessageEditar = "No se ha cargado el usuario"
                    isLoadingEditar = false
                    return@launch
                }

                val response = repository.update(usuarioId, clienteActualizado)
                if(response.isSuccessful()) {
                    successMessageEditar = "Usuario actualizado exitosamente"

                    usuarioSesionEntero = response.body()

                    delay(500)
                    isEditarClicked = false
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error al actualizar el usuario"
                    }
                    restaurarCamposUsuario(usuario)
                }
            } catch (e: IOException) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposUsuario(usuario)
                errorMessageEditar = apiError.message ?: "Error desconocido"
            } finally {
                isLoadingEditar = false
            }
        }
    }

    fun clickEditarOCancelar(usuario: Cliente) {
        isEditarClicked = !isEditarClicked
        restaurarCamposUsuario(usuario)
    }

    fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$".toRegex()
        return email.matches(emailRegex)
    }

    fun restaurarCamposUsuario(usuario: Cliente) {
        nombreCambiado = usuario.nombre ?: ""
        apellidosCambiados = usuario.apellidos ?: ""
        emailCambiado = usuario.email ?: ""
    }
}