package com.gert.tfgdam.api

import com.gert.tfgdam.model.Cliente
import com.gert.tfgdam.model.Libro
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LibroApi {
    @GET("api/libro/get")
    suspend fun getAll(): Response<List<Libro>>

    @GET("api/libro/get/stock")
    suspend fun getAllLimitadoPorStock(): Response<List<Libro>>

    @GET("api/libro/get/view")
    suspend fun getAllLimitadoParaView(): Response<List<Libro>>

    @GET("api/libro/get/tipo/{tipo-libro}")
    suspend fun getAllPorTipo(@Path("tipo-libro") tipoLibro: String): Response<List<Libro>>

    @GET("api/libro/get/tipo/{tipo-libro}/genero/{genero}")
    suspend fun getAllPorTipoGenero(@Path("tipo-libro") tipoLibro: String, @Path("genero") genero: String): Response<List<Libro>>

    @GET("api/libro/get/{id}")
    suspend fun getPorId(@Path("id") id: Long): Response<Libro>

    @GET("api/libro/get/lista-deseados/{usuario}")
    suspend fun getAllListaDeseados(@Path("usuario") usuario: String): Response<List<Libro>>

    @GET("api/libro/get/lista-deseados/{id}/{usuario}")
    suspend fun getLibroEnListaDeseados(@Path("id") id: Long, @Path("usuario") usuario: String): Response<Libro>

    @POST("api/libro/new")
    suspend fun create(@Body libro: Libro): Response<Libro>

    @PUT("api/libro/update/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body libro: Libro
    ): Response<Libro>

    @DELETE("api/libro/delete/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Unit>

    @POST("api/libro/lista-deseados/add/{id}")
    suspend fun addLibroListaDeseados(@Path("id") id: Long, @Body cliente: Cliente): Response<Unit>

    @DELETE("api/libro/lista-deseados/delete/{id}/{usuario}")
    suspend fun deleteLibroListaDeseados(@Path("id") id: Long, @Path("usuario") usuario: String): Response<Unit>

    @PUT("api/libro/update/stock/{id}")
    suspend fun actualizarStock(@Path("id") id: Long, @Body stock: Int): Response<Libro>
}