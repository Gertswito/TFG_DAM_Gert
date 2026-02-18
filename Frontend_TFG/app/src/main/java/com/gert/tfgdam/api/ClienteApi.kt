package com.gert.tfgdam.api

import com.gert.tfgdam.model.Cliente
import com.gert.tfgdam.model.JwtResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ClienteApi {
    @GET("api/cliente/get")
    suspend fun getAll(): Response<List<Cliente>>

    @GET("api/cliente/get/{id}")
    suspend fun getPorId(@Path("id") id: Long): Response<Cliente>

    @POST("api/cliente/new")
    suspend fun create(@Body cliente: Cliente): Response<Cliente>

    @POST("api/cliente/login")
    suspend fun login(@Body cliente: Cliente): Response<JwtResponse>

    @PUT("api/cliente/update/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body cliente: Cliente
    ): Response<Cliente>

    @DELETE("api/cliente/delete/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Unit>
}