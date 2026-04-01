package com.gert.tfgdam.feature.user.usersettings.direcciones.viewmodel

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.core.network.ApiError
import com.gert.tfgdam.feature.admin.cliente.model.Cliente
import com.gert.tfgdam.feature.admin.direccion.model.Direccion
import com.gert.tfgdam.feature.admin.direccion.repository.DireccionRepository
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okio.IOException
import org.json.JSONObject
import kotlin.collections.plus

class UserDireccionesViewModel : ViewModel() {
    private val repository = DireccionRepository()

    var direcciones by mutableStateOf<List<Direccion>>(emptyList())
        private set
    var usuario: Cliente? = null

    var idDireccionEditar by mutableStateOf("")
    var calleEditar by mutableStateOf("")
    var numeroEditar by mutableStateOf("")
    var pisoEditar by mutableStateOf("")
    var ciudadEditar by mutableStateOf("")
    var provinciaEditar by mutableStateOf("")
    var codigoPostalEditar by mutableStateOf("")

    var abrirModalEditarDireccion by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var successMessage by mutableStateOf("")

    var direccionSeleccionada by mutableStateOf<Direccion?>(null)
        private set

    fun setDireccionesIniciales(usuarioScreenAnterior: Cliente, listaDirecciones: List<Direccion>) {
        direcciones = listaDirecciones
        usuario = usuarioScreenAnterior
    }

    fun agregarDireccion(direccion: Direccion) {
        direcciones = direcciones + direccion
    }

    fun actualizarDireccion(direccionActualizada: Direccion) {
        direcciones = direcciones.map {
            if (it.id == direccionActualizada.id) direccionActualizada else it
        }
    }

    fun crearDireccion (usuario: Cliente, onSuccess: (Direccion) -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            successMessage = ""

            try {
                val usuarioSinDireccion = usuario.copy(direcciones = emptyList())
                val direccionNueva = Direccion(
                    calle = calleEditar.trim(),
                    numero = numeroEditar.trim().toIntOrNull(),
                    piso = pisoEditar.trim(),
                    ciudad = ciudadEditar.trim(),
                    provincia = provinciaEditar.trim(),
                    codigoPostal = codigoPostalEditar.trim(),
                    cliente = usuarioSinDireccion
                )

                if (direccionNueva.calle.isNullOrBlank() || direccionNueva.piso.isNullOrBlank() || direccionNueva.ciudad.isNullOrBlank() || direccionNueva.provincia.isNullOrBlank() || direccionNueva.codigoPostal.isNullOrBlank()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                if (direccionNueva.numero == null) {
                    errorMessage = "Por favor, introduzca un número válido"
                    isLoading = false
                    return@launch
                }

                if(usuarioSinDireccion == null && usuarioSinDireccion.id == null) {
                    errorMessage = "No se ha cargado el usuario"
                    isLoading = false
                    return@launch
                }

                val response = repository.create(direccionNueva)
                if(response.isSuccessful) {
                    successMessage = "Dirección creada exitosamente"

                    restaurarCamposDireccion(null)

                    delay(500)
                    onSuccess(response.body()!!)
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error al crear la dirección"
                    }
                }
            } catch (e: IOException) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposDireccion(null)
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun editarDireccion (usuario: Cliente, onSuccess: (Direccion) -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            successMessage = ""

            try {
                val usuarioSinDireccion = usuario.copy(direcciones = emptyList())
                val direccionId = idDireccionEditar.toLongOrNull()
                val direccionNueva = Direccion(
                    id = idDireccionEditar.toLongOrNull(),
                    calle = calleEditar.trim(),
                    numero = numeroEditar.trim().toIntOrNull(),
                    piso = pisoEditar.trim(),
                    ciudad = ciudadEditar.trim(),
                    provincia = provinciaEditar.trim(),
                    codigoPostal = codigoPostalEditar.trim(),
                    cliente = usuarioSinDireccion
                )

                if (direccionNueva.calle.isNullOrBlank() || direccionNueva.piso.isNullOrBlank() || direccionNueva.ciudad.isNullOrBlank() || direccionNueva.provincia.isNullOrBlank() || direccionNueva.codigoPostal.isNullOrBlank()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                if (direccionNueva.numero == null) {
                    errorMessage = "Por favor, introduzca un número válido"
                    isLoading = false
                    return@launch
                }

                if (direccionId == null) {
                    errorMessage = "No se ha encontrado dirección que editar"
                    isLoading = false
                    return@launch
                }

                if(usuarioSinDireccion == null && usuarioSinDireccion.id == null) {
                    errorMessage = "No se ha cargado el usuario"
                    isLoading = false
                    return@launch
                }

                val response = repository.update(direccionId, direccionNueva)
                if(response.isSuccessful) {
                    successMessage = "Dirección editada exitosamente"

                    restaurarCamposDireccion(null)

                    delay(500)
                    onSuccess(response.body()!!)
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error al editar la dirección"
                    }
                }
            } catch (e: IOException) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposDireccion(null)
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun seleccionarDireccion(direccion: Direccion) {
        direccionSeleccionada = direccion
    }

    fun deseleccionarDireccion() {
        direccionSeleccionada = null
    }

    fun restaurarCamposDireccion(direccion: Direccion? = null) {
        if (direccion != null) {
            calleEditar = direccion.calle ?: ""
            numeroEditar = direccion.numero.toString() ?: ""
            pisoEditar = direccion.piso ?: ""
            ciudadEditar = direccion.ciudad ?: ""
            provinciaEditar = direccion.provincia ?: ""
            codigoPostalEditar = direccion.codigoPostal ?: ""
            idDireccionEditar = direccion.id.toString() ?: ""
        } else {
            calleEditar = ""
            numeroEditar = ""
            pisoEditar = ""
            ciudadEditar = ""
            provinciaEditar = ""
            codigoPostalEditar = ""
            idDireccionEditar = ""
        }
    }

    fun clickAbrirModalOCerrar(){
        abrirModalEditarDireccion = !abrirModalEditarDireccion
    }

    fun vibrar(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}