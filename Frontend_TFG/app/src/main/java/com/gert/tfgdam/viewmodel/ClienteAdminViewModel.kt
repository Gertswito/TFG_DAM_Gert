package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.model.Cliente
import com.gert.tfgdam.repository.ClienteRepository
import kotlinx.coroutines.launch
import java.io.IOException

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

    var clientes by mutableStateOf<List<Cliente>>(emptyList())
        private set

    init {
        cargarClientes()
    }

    private fun cargarClientes() {
        viewModelScope.launch {
            try {
                val response = repository.getAll()

                if (response.isSuccessful) {
                    clientes = response.body() ?: emptyList()
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
}