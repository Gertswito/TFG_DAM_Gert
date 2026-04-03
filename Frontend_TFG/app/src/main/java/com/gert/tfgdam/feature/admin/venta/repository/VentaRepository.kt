package com.gert.tfgdam.feature.admin.venta.repository

import com.gert.tfgdam.core.network.RetrofitClient
import com.gert.tfgdam.feature.admin.venta.api.VentaApi
import com.gert.tfgdam.feature.admin.venta.model.Venta
import com.gert.tfgdam.feature.user.carrito.model.CarritoItem
import com.gert.tfgdam.feature.user.pago.model.FinalizarCompra
import retrofit2.Response

class VentaRepository {

    private val api: VentaApi =
        RetrofitClient.instance.create(VentaApi::class.java)

    suspend fun getAll(): Response<List<Venta>> {
        return api.getAll()
    }

    suspend fun getAllPorUsuario(usuario: String): Response<List<Venta>> {
        return api.getAllPorUsuario(usuario)
    }

    suspend fun getPorId(id: Long): Response<Venta> {
        return api.getPorId(id)
    }

    suspend fun getAllPorBusqueda(texto: String): Response<List<Venta>> {
        return api.getAllPorBusqueda(texto)
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

    suspend fun validarStock(carrito: List<CarritoItem>): Response<Unit> {
        return api.validarStock(carrito)
    }

    suspend fun createPaypalOrder(finalizarCompra: FinalizarCompra): Response<Map<String, Any>> {
        return api.createPaypalOrder(finalizarCompra)
    }

    suspend fun capturePaypalOrder(orderId: String): Response<Venta> {
        return api.capturePaypalOrder(orderId)
    }
}