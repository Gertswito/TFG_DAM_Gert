package com.gert.tfgdam.feature.admin.venta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.core.network.ApiError
import com.gert.tfgdam.feature.admin.cliente.model.Cliente
import com.gert.tfgdam.feature.admin.direccion.model.Direccion
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.admin.lineaventa.model.LineaVenta
import com.gert.tfgdam.feature.admin.venta.model.Venta
import com.gert.tfgdam.feature.admin.cliente.repository.ClienteRepository
import com.gert.tfgdam.feature.admin.direccion.repository.DireccionRepository
import com.gert.tfgdam.feature.admin.libro.repository.LibroRepository
import com.gert.tfgdam.feature.admin.lineaventa.repository.LineaVentaRepository
import com.gert.tfgdam.feature.admin.venta.repository.VentaRepository
import com.google.gson.Gson
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class VentaAdminViewModel : ViewModel() {
    private val ventaRepository = VentaRepository()
    private val lineaVentaRepository = LineaVentaRepository()
    private val clienteRepository = ClienteRepository()
    private val direccionRepository = DireccionRepository()
    private val libroRepository = LibroRepository()

    var idWidth by mutableIntStateOf(0)
        private set
    var usuarioWidth by mutableIntStateOf(0)
        private set
    var direccionWidth by mutableIntStateOf(0)
        private set

    var idVenta by mutableStateOf("")
    var clienteVenta by mutableStateOf<Cliente?>(null)
    var direccionVenta by mutableStateOf<Direccion?>(null)
    var fechaVenta by mutableStateOf("")
    var horaVenta by mutableStateOf("")
    var precioFinalVenta by mutableStateOf("")

    var idLineaVenta by mutableStateOf("")
    var libroLineaVenta by mutableStateOf<Libro?>(null)
    var cantidadLineaVenta by mutableStateOf("")
    var precioParcialLineaVenta by mutableStateOf("")
    var precioTotalLineaVenta by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    var buscador by mutableStateOf("")
        private set
    private val buscadorFlow = MutableStateFlow("")
    var isLoadingBusqueda by mutableStateOf(false)

    var ventas by mutableStateOf<List<Venta>>(emptyList())
        private set
    var ventasFiltradas by mutableStateOf<List<Venta>>(emptyList())
        private set
    var lineasPorVenta by mutableStateOf<Map<Long, List<LineaVenta>>>(emptyMap())
        private set
    var listaClientes by mutableStateOf<List<Cliente>>(emptyList())
        private set
    var listaDirecciones by mutableStateOf<List<Direccion>>(emptyList())
        private set
    var listaLibros by mutableStateOf<List<Libro>>(emptyList())
        private set

    init {
        cargarVentas()
        observarBuscador()
    }

    private fun cargarVentas() {
        viewModelScope.launch {
            try {
                val response = ventaRepository.getAll()

                if (response.isSuccessful) {
                    ventas = (response.body() ?: emptyList()).sortedBy { it.id }
                    calcularAnchuras()
                } else {
                    ventas = emptyList()
                }
            } catch (e: IOException) {
                ventas = emptyList()
            }
        }
    }

    fun onBuscadorChange(texto: String) {
        buscador = texto
        buscadorFlow.value = texto
        if (texto.isNotEmpty()) {
            isLoadingBusqueda = true
        }
    }

    @OptIn(FlowPreview::class)
    private fun observarBuscador() {
        viewModelScope.launch {
            buscadorFlow
                .debounce(500)
                .distinctUntilChanged()
                .collect { texto ->
                    buscarVentas(texto)
                }
        }
    }

    private fun buscarVentas(texto: String) {
        viewModelScope.launch {
            try {
                val response = ventaRepository.getAllPorBusqueda(texto)

                if (response.isSuccessful) {
                    ventasFiltradas = (response.body() ?: emptyList()).sortedBy { it.id }
                } else {
                    ventasFiltradas = emptyList()
                }
            } catch (e: IOException) {
                ventasFiltradas = emptyList()
            } finally {
                isLoadingBusqueda = false
            }
        }
    }

    fun cargarLineasVentasPorVenta(ventaId: Long) {
        if (lineasPorVenta.containsKey(ventaId)) return

        viewModelScope.launch {
            try {
                val response = lineaVentaRepository.getAllPorVenta(ventaId)

                if (response.isSuccessful) {
                    val nuevas = response.body() ?: emptyList()

                    lineasPorVenta = lineasPorVenta.toMutableMap().apply {
                        put(ventaId, nuevas)
                    }
                }
            } catch (_: IOException) {}
        }
    }

    fun limpiarLineasVenta(ventaId: Long) {
        lineasPorVenta = lineasPorVenta.toMutableMap().apply {
            remove(ventaId)
        }
    }

    private fun cargarClientes() {
        viewModelScope.launch {
            try {
                val response = clienteRepository.getAll()

                if (response.isSuccessful) {
                    listaClientes = (response.body() ?: emptyList()).sortedBy { it.id }
                } else {
                    listaClientes = emptyList()
                }
            } catch (e: IOException) {
                listaClientes = emptyList()
            }
        }
    }

    fun cargarListasEditarYCrearVenta() {
        cargarClientes()
    }

    fun cargarListaDireccionesPorCliente(clienteId: Long) {
        viewModelScope.launch {
            try {
                val response = direccionRepository.getAllPorClienteId(clienteId)

                if (response.isSuccessful) {
                    listaDirecciones = (response.body() ?: emptyList()).sortedBy { it.id }
                } else {
                    listaDirecciones = emptyList()
                }
            } catch (_: IOException) {
                listaDirecciones = emptyList()
            }
        }
    }

    private fun cargarLibros() {
        viewModelScope.launch {
            try {
                val response = libroRepository.getAll()

                if (response.isSuccessful) {
                    listaLibros = (response.body() ?: emptyList()).sortedBy { it.id }
                } else {
                    listaLibros = emptyList()
                }
            } catch (e: IOException) {
                listaLibros = emptyList()
            }
        }
    }

    fun cargarListasEditarYCrearLineaVenta() {
        cargarLibros()
    }

    fun crearVenta(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val ventaNueva = Venta(
                    cliente = clienteVenta,
                    direccion = direccionVenta,
                    fecha = fechaVenta,
                    hora = horaVenta,
                    precioFinal = precioFinalVenta.toDoubleOrNull()
                )

                if (clienteVenta == null || direccionVenta == null || fechaVenta.isEmpty() || horaVenta.isEmpty()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                val response = ventaRepository.create(ventaNueva)
                if (response.isSuccessful) {
                    cargarVentas()
                    restaurarCamposVenta()

                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("message")
                    } catch (e: Exception) {
                        "Error al crear la venta"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposVenta()
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun restaurarCamposVenta() {
        idVenta = ""
        clienteVenta = null
        direccionVenta = null
        fechaVenta = ""
        horaVenta = ""
        precioFinalVenta = ""
    }

    fun crearLineaVenta(venta: Venta, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val lineaVentaNueva = LineaVenta(
                    venta = venta,
                    libro = libroLineaVenta,
                    cantidad = cantidadLineaVenta.toIntOrNull(),
                    precioParcial = precioParcialLineaVenta.toDoubleOrNull(),
                    precioTotal = precioTotalLineaVenta.toDoubleOrNull()
                )

                if (libroLineaVenta == null || cantidadLineaVenta.isEmpty() || precioParcialLineaVenta.isEmpty() || precioTotalLineaVenta.isEmpty()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                val response = lineaVentaRepository.create(lineaVentaNueva)
                if (response.isSuccessful) {
                    cargarVentas()
                    limpiarLineasVenta(venta.id ?: 0)
                    restaurarCamposLineaVenta()

                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("message")
                    } catch (e: Exception) {
                        "Error al crear la línea de venta"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposVenta()
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun restaurarCamposLineaVenta() {
        idLineaVenta = ""
        libroLineaVenta = null
        cantidadLineaVenta = ""
        precioParcialLineaVenta = ""
        precioTotalLineaVenta = ""
    }

    fun calcularPreciosLineaVenta(libro: Libro?) {
        val cantidad = cantidadLineaVenta.toIntOrNull()

        if (cantidad != null && libro != null) {
            precioParcialLineaVenta = libro.precio.toString()
            precioTotalLineaVenta = (libro.precio?.times(cantidad) ?: 0.0).toString()
        }
    }

    private fun calcularAnchuras() {
        idWidth = ventas.maxOfOrNull { it.id.toString().length } ?: 0
        usuarioWidth = ventas.maxOfOrNull { it.cliente?.usuario?.length ?: 0 } ?: 0
        direccionWidth = ventas.maxOfOrNull { (it.direccion?.calle?.length ?: 0) + (it.direccion?.numero.toString().length ?: 0) + (it.direccion?.piso.toString().length ?: 0) } ?: 0
    }

    fun cambiarDeCharacteresADp(nChars: Int): Dp {
        return kotlin.comparisons.maxOf((nChars * 11).dp, 60.dp)
    }
}