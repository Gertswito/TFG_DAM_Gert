package com.gert.tfgdam.feature.user.carrito.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.core.util.Jwt.JwtManager
import com.gert.tfgdam.data.dataStore
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.user.carrito.model.CarritoItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CarritoViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val userFlow = JwtManager.getUserInfoFlow(context)
        .map { it?.sub ?: "guest" }

    val carritoItems = userFlow.flatMapLatest { usuario ->
        val carritoKey = stringPreferencesKey("carrito_items_$usuario")

        context.dataStore.data.map { preferences ->
            val json = preferences[carritoKey] ?: "[]"
            Gson().fromJson<List<CarritoItem>>(
                json,
                object : com.google.gson.reflect.TypeToken<List<CarritoItem>>() {}.type
            )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        emptyList()
    )

    private suspend fun getUsuarioActual(): String {
        return JwtManager.getUserInfoFlow(context).firstOrNull()?.sub ?: "INVITADO"
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

    fun addAlCarrito(libro: Libro) {
        viewModelScope.launch {
            val usuario = getUsuarioActual()
            val carritoKey = stringPreferencesKey("carrito_items_$usuario")
            context.dataStore.edit { preferences ->

                val json = preferences[carritoKey] ?: "[]"
                val type = object : com.google.gson.reflect.TypeToken<MutableList<CarritoItem>>() {}.type
                val carrito: MutableList<CarritoItem> = Gson().fromJson(json, type)

                val libroExistenteEnCarrito = carrito.find { it.libro.id == libro.id }
                if (libroExistenteEnCarrito != null) {
                    val index = carrito.indexOf(libroExistenteEnCarrito)
                    carrito[index] = libroExistenteEnCarrito.copy(cantidad = libroExistenteEnCarrito.cantidad + 1)
                } else {
                    carrito.add(CarritoItem(libro = libro, cantidad = 1))
                }
                preferences[carritoKey] = Gson().toJson(carrito)
            }
        }
    }

    fun quitarUnoDelCarrito(libroId: Long) {
        viewModelScope.launch {
            val usuario = getUsuarioActual()
            val carritoKey = stringPreferencesKey("carrito_items_$usuario")
            context.dataStore.edit { preferences ->
                val json = preferences[carritoKey] ?: "[]"
                val type = object : com.google.gson.reflect.TypeToken<MutableList<CarritoItem>>() {}.type
                val carrito: MutableList<CarritoItem> = Gson().fromJson(json, type)

                val index = carrito.indexOfFirst { it.libro.id == libroId }
                if (index != -1) {
                    val item = carrito[index]
                    if (item.cantidad > 1) {
                        carrito[index] = item.copy(cantidad = item.cantidad - 1)
                    } else {
                        carrito.removeAt(index)
                    }
                }
                preferences[carritoKey] = Gson().toJson(carrito)
            }
        }
    }

    fun deleteDelCarrito(libroId: Long) {
        viewModelScope.launch {
            val usuario = getUsuarioActual()
            val carritoKey = stringPreferencesKey("carrito_items_$usuario")
            context.dataStore.edit { preferences ->
                val json = preferences[carritoKey] ?: "[]"
                val type = object : com.google.gson.reflect.TypeToken<MutableList<CarritoItem>>() {}.type
                val carrito: MutableList<CarritoItem> = Gson().fromJson(json, type)

                carrito.removeAll { it.libro.id == libroId }
                preferences[carritoKey] = Gson().toJson(carrito)
            }
        }
    }

    fun vaciarCarrito() {
        viewModelScope.launch {
            val usuario = getUsuarioActual()
            val carritoKey = stringPreferencesKey("carrito_items_$usuario")
            context.dataStore.edit { preferences ->
                preferences[carritoKey] = "[]"
            }
        }
    }

    fun calcularTotal(): Double {
        return carritoItems.value.sumOf { (it.libro.precio ?: 0.0) * it.cantidad }
    }
}