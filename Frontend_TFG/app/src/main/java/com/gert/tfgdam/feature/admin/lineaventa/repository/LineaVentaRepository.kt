package com.gert.tfgdam.feature.admin.lineaventa.repository

import com.gert.tfgdam.core.network.RetrofitClient
import com.gert.tfgdam.feature.admin.lineaventa.api.LineaVentaApi
import com.gert.tfgdam.feature.admin.lineaventa.model.LineaVenta
import retrofit2.Response

class LineaVentaRepository {

    private val api: LineaVentaApi =
        RetrofitClient.instance.create(LineaVentaApi::class.java)

    suspend fun getAll(): Response<List<LineaVenta>> {
        return api.getAll()
    }

    suspend fun getAllPorVenta(ventaId: Long): Response<List<LineaVenta>> {
        return api.getAllPorVenta(ventaId)
    }

    suspend fun getPorId(id: Long): Response<LineaVenta> {
        return api.getPorId(id)
    }

    suspend fun create(lineaVenta: LineaVenta): Response<LineaVenta> {
        return api.create(lineaVenta)
    }

    suspend fun update(id: Long, lineaVenta: LineaVenta): Response<LineaVenta> {
        return api.update(id, lineaVenta)
    }

    suspend fun delete(id: Long): Response<Unit> {
        return api.delete(id)
    }
}