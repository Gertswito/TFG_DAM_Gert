package com.gert.tfgdam.feature.admin.cliente.api

import com.gert.tfgdam.feature.admin.cliente.model.Cliente
import com.gert.tfgdam.core.util.Jwt.JwtResponse
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

    @GET("api/cliente/get/usuario/{usuario}")
    suspend fun getPorUsuario(@Path("usuario") usuario: String): Response<Cliente>

    @GET("api/cliente/get/busqueda/{texto}")
    suspend fun getAllPorBusqueda(@Path("texto") texto: String): Response<List<Cliente>>

    @POST("api/cliente/login")
    suspend fun login(@Body cliente: Cliente): Response<JwtResponse>

    @PUT("api/cliente/cambiarContrasenha/{id}")
    suspend fun cambiarContrasenha(@Path("id") id: Long, @Body contrasenha: String): Response<Cliente>

    @PUT("api/cliente/cambiarContrasenha/{usuario}/{email}")
    suspend fun cambiarContrasenhaSinSesion(@Path("usuario") usuario: String, @Path("email") email: String, @Body contrasenha: String): Response<Cliente>

    @POST("api/cliente/new")
    suspend fun create(@Body cliente: Cliente): Response<Cliente>

    @PUT("api/cliente/update/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body cliente: Cliente
    ): Response<Cliente>

    @PUT("api/cliente/update/usuario/{id}")
    suspend fun updateUsuario(
        @Path("id") id: Long,
        @Body cliente: Cliente
    ): Response<Cliente>

    @DELETE("api/cliente/delete/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Unit>
}