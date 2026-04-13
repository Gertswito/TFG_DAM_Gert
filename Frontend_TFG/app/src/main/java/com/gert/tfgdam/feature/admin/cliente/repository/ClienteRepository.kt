package com.gert.tfgdam.feature.admin.cliente.repository

import com.gert.tfgdam.core.network.RetrofitClient
import com.gert.tfgdam.feature.admin.cliente.api.ClienteApi
import com.gert.tfgdam.feature.admin.cliente.model.Cliente
import retrofit2.Response

class ClienteRepository {

    private val api: ClienteApi =
        RetrofitClient.instance.create(ClienteApi::class.java)

    suspend fun getAll(): Response<List<Cliente>> {
        return api.getAll()
    }

    suspend fun getPorId(id: Long): Response<Cliente> {
        return api.getPorId(id)
    }

    suspend fun getPorUsuario(usuario: String): Response<Cliente> {
        return api.getPorUsuario(usuario)
    }

    suspend fun getAllPorBusqueda(texto: String): Response<List<Cliente>> {
        return api.getAllPorBusqueda(texto)
    }

    suspend fun login(cliente: Cliente): String {
        val response = api.login(cliente)
        if (response.isSuccessful) {
            return response.body()?.token ?: throw Exception("Token no recibido")
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception(errorBody ?: "Error desconocido del servidor")
        }
    }

    suspend fun cambiarContrasenha(id: Long, contrasenha: String): Response<Cliente> {
        return api.cambiarContrasenha(id, contrasenha)
    }

    suspend fun cambiarContrasenhaSinSesion(usuario: String, email: String, contrasenha: String): Response<Cliente> {
        return api.cambiarContrasenhaSinSesion(usuario, email, contrasenha)
    }

    suspend fun create(cliente: Cliente): Response<Cliente> {
        return api.create(cliente)
    }

    suspend fun update(id: Long, cliente: Cliente): Response<Cliente> {
        return api.update(id, cliente)
    }

    suspend fun updateUsuario(id: Long, cliente: Cliente): Response<Cliente> {
        return api.updateUsuario(id, cliente)
    }

    suspend fun delete(id: Long): Response<Unit> {
        return api.delete(id)
    }
}