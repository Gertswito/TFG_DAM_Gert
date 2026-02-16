package com.gert.tfgdam.repository

import com.gert.tfgdam.api.VentaApi
import com.gert.tfgdam.model.Venta
import com.gert.tfgdam.network.RetrofitClient
import retrofit2.Response

class VentaRepository {

    private val api: VentaApi =
        RetrofitClient.instance.create(VentaApi::class.java)

    suspend fun getAll(): Response<List<Venta>> {
        return api.getAll()
    }

    suspend fun getPorId(id: Long): Response<Venta> {
        return api.getPorId(id)
    }

    suspend fun create(venta: Venta): Response<Venta> {
        return api.create(venta)
    }

    suspend fun update(id: Long, venta: Venta): Response<Venta> {
        return api.update(id, venta)
    }

    suspend fun delete(id: Long): Response<Unit> {
        return api.delete(id)
    }
}