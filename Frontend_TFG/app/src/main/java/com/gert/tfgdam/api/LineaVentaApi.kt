package com.gert.tfgdam.api

import com.gert.tfgdam.model.LineaVenta
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

    @POST("api/linea-venta/new")
    suspend fun create(@Body lineaVenta: LineaVenta): Response<LineaVenta>

    @PUT("api/linea-venta/update/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body lineaVenta: LineaVenta
    ): Response<LineaVenta>

    @DELETE("api/linea-venta/delete/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Unit>
}