package com.gert.tfgdam.feature.user.listadeseados.viewmodel

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
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.admin.libro.repository.LibroRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class ListaDeseadosViewModel : ViewModel() {
    private val repository = LibroRepository()

    var libros by mutableStateOf<List<Libro>>(emptyList())
        private set
    var isLibroYaDeseado by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun cargarLibrosListaDeseados(usuario: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllListaDeseados(usuario)

                if (response.isSuccessful) {
                    libros = response.body() ?: emptyList()
                } else {
                    libros = emptyList()
                }
            } catch (e: IOException) {
                libros = emptyList()
            }
        }
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

    fun buscarLibroEnListaDeseados(libroId: Long, usuario: String) {
        viewModelScope.launch {
            try {
                val response = repository.getLibroEnListaDeseados(libroId, usuario)
                if (response.isSuccessful) {
                    isLibroYaDeseado = true
                } else {
                    isLibroYaDeseado = false
                }
            } catch (e: okio.IOException) {
                isLibroYaDeseado = false
            }
        }
    }

    fun addLibroListaDeseados(libroSeleccionado: Libro, usuario: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val libroId = libroSeleccionado.id
                val cliente = Cliente(
                    usuario = usuario.trim()
                )

                if(libroId == null) {
                    errorMessage = "No se ha cargado el libro"
                    isLoading = false
                    return@launch
                }

                if(cliente.usuario.isNullOrBlank()) {
                    errorMessage = "No se ha cargado el usuario"
                    isLoading = false
                    return@launch
                }

                val response = repository.addLibroListaDeseados(libroId, cliente)
                if(response.isSuccessful) {
                    isLibroYaDeseado = true
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error en al añadir a la lista"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteLibroListaDeseados(libroSeleccionado: Libro, usuario: String, isLibroEditar: Boolean, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val libroId = libroSeleccionado.id
                val usuarioLogueado = usuario.trim()

                if(libroId == null) {
                    errorMessage = "No se ha cargado el libro"
                    isLoading = false
                    return@launch
                }

                if(usuarioLogueado.isNullOrBlank()) {
                    errorMessage = "No se ha cargado el usuario"
                    isLoading = false
                    return@launch
                }

                val response = repository.deleteLibroListaDeseados(libroId, usuarioLogueado)
                if(response.isSuccessful) {
                    onSuccess()
                    if(isLibroEditar) {
                        isLibroYaDeseado = false
                    } else {
                        cargarLibrosListaDeseados(usuario)
                    }
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error en al eliminar de la lista"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }
}