package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.model.CarritoItem
import com.gert.tfgdam.model.Cliente
import com.gert.tfgdam.model.Direccion
import com.gert.tfgdam.model.FinalizarCompra
import com.gert.tfgdam.model.LineaVenta
import com.gert.tfgdam.model.Venta
import com.gert.tfgdam.repository.VentaRepository
import kotlinx.coroutines.launch

class PagoViewModel() : ViewModel() {
    private val repository = VentaRepository()

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var successMessage by mutableStateOf("")
    var approvalUrl by mutableStateOf<String?>(null)

    fun iniciarProcesoPago(carrito: List<CarritoItem>, direccionSeleccionada: Direccion, usuario: Cliente) {
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

                val response = repository.createPaypalOrder(finalizarCompra)
                if (response.isSuccessful) {
                    val body = response.body()
                    val links =
                        body?.get("links") as List<Map<String, Any>>

                    val approveUrl = links.first {
                        it["rel"] == "approve"
                    }["href"] as String
                    approvalUrl = approveUrl
                } else {
                    errorMessage = "Error creando pedido en PayPal"
                }
            } catch (e: Exception) {
                errorMessage = "Error creando pedido en PayPal"
            } finally {
                isLoading = false;
            }
        }
    }

    fun capturarPago(orderId: String, carritoViewModel: CarritoViewModel) {
        viewModelScope.launch {
            carritoViewModel.vaciarCarrito()
            isLoading = true
            errorMessage = ""

            try {
                val response = repository.capturePaypalOrder(orderId)
                if (response.isSuccessful) {
                    successMessage = "Compra realizada con éxito, muchas gracias !!!"
                } else {
                    errorMessage = "Error guardando la venta en la base de datos"
                }
            } catch (e: Exception) {
                errorMessage = "Error guardando la venta en la base de datos"
            } finally {
                isLoading = false
            }
        }
    }

    suspend fun validarStock(carrito: List<CarritoItem>): Boolean {
        return try {
            val response = repository.validarStock(carrito)
            if (response.isSuccessful) {
                true
            } else {
                errorMessage = response.errorBody()?.string() ?: "Stock insuficiente"
                false
            }
        } catch (e: Exception) {
            errorMessage = "Error validando stock"
            false
        }
    }
}