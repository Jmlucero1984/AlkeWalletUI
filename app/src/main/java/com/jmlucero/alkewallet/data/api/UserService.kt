package com.jmlucero.alkewallet.data.api


import com.jmlucero.alkewallet.data.model.Usuario
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    // Obtener usuario por ID
    @GET("usuarios/{id}")
    suspend fun getUser(@Path("id") userId: Int): Response<Usuario>

    // Obtener todos los usuarios
    @GET("usuarios")
    suspend fun getAllUsers(): Response<List<Usuario>>

    // Crear nuevo usuario
    @POST("usuarios")
    suspend fun createUser(@Body user: Usuario): Response<Usuario>

    // Actualizar usuario
    @PUT("usuarios/{id}")
    suspend fun updateUser(@Path("id") userId: Int, @Body user: Usuario): Response<Usuario>

    // Eliminar usuario
    @DELETE("usuarios/{id}")
    suspend fun deleteUser(@Path("id") userId: Int): Response<Unit>
}