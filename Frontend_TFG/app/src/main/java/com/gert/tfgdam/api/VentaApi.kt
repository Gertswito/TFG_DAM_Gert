package com.gert.tfgdam.api

import com.gert.tfgdam.model.CarritoItem
import com.gert.tfgdam.model.FinalizarCompra
import com.gert.tfgdam.model.Venta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VentaApi {
    @GET("api/venta/get")
    suspend fun getAll(): Response<List<Venta>>

    @GET("api/venta/get/{id}")
    suspend fun getPorId(@Path("id") id: Long): Response<Venta>

    @POST("api/venta/new")
    suspend fun create(@Body venta: Venta): Response<Venta>

    @PUT("api/venta/update/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body venta: Venta
    ): Response<Venta>

    @DELETE("api/venta/delete/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Unit>
    @POST("api/venta/validar-stock")
    suspend fun validarStock(@Body lineas: List<CarritoItem>): Response<Unit>

    @POST("api/venta/paypal/create-order")
    suspend fun createPaypalOrder(@Body finalizarCompra: FinalizarCompra): Response<Map<String, Any>>

    @POST("api/venta/paypal/capture/{orderId}")
    suspend fun capturePaypalOrder(@Path("orderId") orderId: String): Response<Venta>
}