package com.gert.tfgdam.feature.admin.lineaventa.api

import com.gert.tfgdam.feature.admin.lineaventa.model.LineaVenta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LineaVentaApi {
    @GET("api/linea-venta/get")
    suspend fun getAll(): Response<List<LineaVenta>>

    @GET("api/linea-venta/get/{id}")
    suspend fun getPorId(@Path("id") id: Long): Response<LineaVenta>

    @GET("api/linea-venta/get/venta/{ventaId}")
    suspend fun getAllPorVenta(@Path("ventaId") ventaId: Long): Response<List<LineaVenta>>

    @POST("api/linea-venta/new")
    suspend fun create(@Body lineaVenta: LineaVenta): Response<LineaVenta>
}