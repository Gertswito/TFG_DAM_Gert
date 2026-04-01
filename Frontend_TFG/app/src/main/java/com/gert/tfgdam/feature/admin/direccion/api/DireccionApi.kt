package com.gert.tfgdam.feature.admin.direccion.api

import com.gert.tfgdam.feature.admin.direccion.model.Direccion
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DireccionApi {
    @GET("api/direccion/get")
    suspend fun getAll(): Response<List<Direccion>>

    @GET("api/direccion/get/cliente/{clienteId}")
    suspend fun getAllPorClienteId(@Path("clienteId") clienteId: Long): Response<List<Direccion>>

    @GET("api/direccion/get/{id}")
    suspend fun getPorId(@Path("id") id: Long): Response<Direccion>

    @POST("api/direccion/new")
    suspend fun create(@Body direccion: Direccion): Response<Direccion>

    @PUT("api/direccion/update/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body direccion: Direccion
    ): Response<Direccion>

    @DELETE("api/direccion/delete/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Unit>
}