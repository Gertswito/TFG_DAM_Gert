package com.gert.tfgdam.repository

import com.gert.tfgdam.api.ClienteApi
import com.gert.tfgdam.model.Cliente
import com.gert.tfgdam.network.RetrofitClient
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

    suspend fun create(cliente: Cliente): Response<Cliente> {
        return api.create(cliente)
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

    suspend fun update(id: Long, cliente: Cliente): Response<Cliente> {
        return api.update(id, cliente)
    }

    suspend fun delete(id: Long): Response<Unit> {
        return api.delete(id)
    }
}