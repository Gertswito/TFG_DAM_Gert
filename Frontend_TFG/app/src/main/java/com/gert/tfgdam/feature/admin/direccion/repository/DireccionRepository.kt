package com.gert.tfgdam.feature.admin.direccion.repository

import com.gert.tfgdam.core.network.RetrofitClient
import com.gert.tfgdam.feature.admin.direccion.api.DireccionApi
import com.gert.tfgdam.feature.admin.direccion.model.Direccion
import retrofit2.Response

class DireccionRepository {

    private val api: DireccionApi =
        RetrofitClient.instance.create(DireccionApi::class.java)

    suspend fun getAll(): Response<List<Direccion>> {
        return api.getAll()
    }

    suspend fun getAllPorClienteId(clienteId: Long): Response<List<Direccion>> {
        return api.getAllPorClienteId(clienteId)
    }

    suspend fun getPorId(id: Long): Response<Direccion> {
        return api.getPorId(id)
    }

    suspend fun create(direccion: Direccion): Response<Direccion> {
        return api.create(direccion)
    }

    suspend fun update(id: Long, direccion: Direccion): Response<Direccion> {
        return api.update(id, direccion)
    }

    suspend fun delete(id: Long): Response<Unit> {
        return api.delete(id)
    }
}