package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.api.ApiError
import com.gert.tfgdam.model.Cliente
import com.gert.tfgdam.model.Rol
import com.gert.tfgdam.repository.ClienteRepository
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import kotlin.collections.sortedBy

class ClienteAdminViewModel : ViewModel() {
    private val repository = ClienteRepository()

    var idWidth by mutableIntStateOf(0)
        private set
    var usuarioWidth by mutableIntStateOf(0)
        private set
    var nombreWidth by mutableIntStateOf(0)
        private set
    var apellidosWidth by mutableIntStateOf(0)
        private set
    var emailWidth by mutableIntStateOf(0)
        private set

    var usuarioCliente by mutableStateOf("")
    var nombreCliente by mutableStateOf("")
    var apellidoCliente by mutableStateOf("")
    var emailCliente by mutableStateOf("")
    var contrasenhaCliente by mutableStateOf("")
    var idCliente by mutableStateOf("")
    var rolCliente by mutableStateOf(Rol.USER)

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    var clientes by mutableStateOf<List<Cliente>>(emptyList())
        private set

    val listaRoles = listOf(Rol.USER, Rol.ADMIN)

    init {
        cargarClientes()
    }

    private fun cargarClientes() {
        viewModelScope.launch {
            try {
                val response = repository.getAll()

                if (response.isSuccessful) {
                    clientes = (response.body() ?: emptyList()).sortedBy { it.id }

                    calcularAnchuras()
                } else {
                    clientes = emptyList()
                }
            } catch (e: IOException) {
                clientes = emptyList()
            }
        }
    }

    private fun calcularAnchuras() {
        idWidth = clientes.maxOfOrNull { it.id.toString().length } ?: 0
        usuarioWidth = clientes.maxOfOrNull { it.usuario?.length ?: 0 } ?: 0
        nombreWidth = clientes.maxOfOrNull { it.nombre?.length ?: 0 } ?: 0
        apellidosWidth = clientes.maxOfOrNull { it.apellidos?.length ?: 0 } ?: 0
        emailWidth = clientes.maxOfOrNull { it.email?.length ?: 0 } ?: 0
    }

    fun cambiarDeCharacteresADp(nChars: Int): Dp {
        return maxOf((nChars * 11).dp, 60.dp)
    }

    fun crearCliente(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val clienteNuevo = Cliente (
                    usuario = usuarioCliente.trim(),
                    contrasenha = contrasenhaCliente.trim(),
                    nombre = nombreCliente.trim(),
                    apellidos = apellidoCliente.trim(),
                    email = emailCliente.trim(),
                    rol = rolCliente
                )

                if (usuarioCliente.isEmpty() || nombreCliente.isEmpty() || apellidoCliente.isEmpty() || emailCliente.isEmpty() || contrasenhaCliente.isEmpty()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                if (!isEmailValid(emailCliente.trim())) {
                    errorMessage = "Correo no válido, vuelva a intentarlo"
                    isLoading = false
                    return@launch
                }

                val response = repository.create(clienteNuevo)
                if (response.isSuccessful) {
                    cargarClientes()
                    restaurarCamposAutor(null)

                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error al crear el cliente"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposAutor(null)
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun editarCliente(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val clienteActualizado = Cliente (
                    id = idCliente.toLong(),
                    usuario = usuarioCliente.trim(),
                    contrasenha = contrasenhaCliente.trim(),
                    nombre = nombreCliente.trim(),
                    apellidos = apellidoCliente.trim(),
                    email = emailCliente.trim(),
                    rol = rolCliente
                )

                if (idCliente.isEmpty() || usuarioCliente.isEmpty() || nombreCliente.isEmpty() || apellidoCliente.isEmpty() || emailCliente.isEmpty() || contrasenhaCliente.isEmpty()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                if (!isEmailValid(emailCliente.trim())) {
                    errorMessage = "Correo no válido, vuelva a intentarlo"
                    isLoading = false
                    return@launch
                }

                val response = repository.update(idCliente.toLong(), clienteActualizado)
                if (response.isSuccessful) {
                    cargarClientes()
                    restaurarCamposAutor(null)

                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error al editar el cliente"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposAutor(null)
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

    fun restaurarCamposAutor(cliente: Cliente? = null) {
        if (cliente != null) {
            usuarioCliente = cliente.usuario ?: ""
            contrasenhaCliente = cliente.contrasenha ?: ""
            nombreCliente = cliente.nombre ?: ""
            apellidoCliente = cliente.apellidos ?: ""
            emailCliente = cliente.email ?: ""
            idCliente = cliente.id.toString() ?: ""
            rolCliente = cliente.rol ?: Rol.USER
        } else {
            usuarioCliente = ""
            contrasenhaCliente = ""
            nombreCliente = ""
            apellidoCliente = ""
            emailCliente = ""
            idCliente = ""
            rolCliente = Rol.USER
        }
    }
}