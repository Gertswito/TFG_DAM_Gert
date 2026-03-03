package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.api.ApiError
import com.gert.tfgdam.model.CarritoItem
import com.gert.tfgdam.model.Cliente
import com.gert.tfgdam.model.Direccion
import com.gert.tfgdam.model.FinalizarCompra
import com.gert.tfgdam.model.LineaVenta
import com.gert.tfgdam.model.Venta
import com.gert.tfgdam.repository.VentaRepository
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okio.IOException
import org.json.JSONObject

class PagoViewModel() : ViewModel() {
    private val repository = VentaRepository()

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var successMessage by mutableStateOf("")

    fun finalizarCompra(carrito: List<CarritoItem>, direccionSeleccionada: Direccion, usuario: Cliente, carritoViewModel: CarritoViewModel, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            successMessage = ""

            try {
                val nuevaVenta = Venta(
                    cliente = usuario,
                    direccion = direccionSeleccionada,
                    precioFinal = carrito.sumOf { (it.libro.precio ?: 0.0) * it.cantidad }
                )

                val nuevasLineasVenta = carrito.map { item ->
                    val precioUnitario = item.libro.precio ?: 0.0
                    val cantidad = item.cantidad
                    LineaVenta(
                        venta = nuevaVenta,
                        libro = item.libro,
                        cantidad = cantidad,
                        precioParcial = precioUnitario,
                        precioTotal = precioUnitario * item.cantidad
                    )
                }

                val finalizarCompra = FinalizarCompra(nuevaVenta, nuevasLineasVenta)
                val response = repository.finalizarCompra(finalizarCompra)
                if (response.isSuccessful) {
                    successMessage = "Compra realizada con éxito, muchas gracias por su compra !!!"
                    carritoViewModel.vaciarCarrito()
                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("error")
                    } catch (e: Exception) {
                        "Error al finalizar la compra"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false;
            }
        }
    }
}