package com.jmlucero.alkewallet.data.api


import com.jmlucero.alkewallet.data.model.Balance
import com.jmlucero.alkewallet.data.model.Deposito
import com.jmlucero.alkewallet.data.model.DepositoResponse
import com.jmlucero.alkewallet.data.model.LoginRequest
import com.jmlucero.alkewallet.data.model.LoginResponse
import com.jmlucero.alkewallet.data.model.Moneda
import com.jmlucero.alkewallet.data.model.Retiro
import com.jmlucero.alkewallet.data.model.RetiroResponse
import com.jmlucero.alkewallet.data.model.Transaccion
import com.jmlucero.alkewallet.data.model.TransaccionSimple
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.model.UsuarioConMoneda
import com.jmlucero.alkewallet.data.room.MonedaDTO
import com.jmlucero.alkewallet.data.room.UsuarioMonedaDTO
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/usuarios")
    suspend fun getUsuarios(@Header("Authorization") token: String): Response<List<Usuario>>

    // Si no quieres pasar el token manualmente
    @GET("api/usuarios")
    suspend fun getUsuarios(): Response<List<Usuario>>

    @GET("api/usuarios/{email}")
    suspend fun getUsuarioPorEmail(
        @Path("email") email: String
    ): Response<UsuarioMonedaDTO>

    @GET("api/usuarios/me")
    suspend fun get_profile(): Response<UsuarioMonedaDTO>


    @GET("api/transacciones/owner")
    suspend fun getTransacciones(): Response<List<Transaccion>>

    @GET("api/transacciones/owner_simple")
    suspend fun getTransaccionesSimple(): Response<List<TransaccionSimple>>


    @GET("api/usuarios/me/balance")
    suspend fun getBalance(): Response<Balance>


    @GET("api/transacciones/{id}")
    suspend fun getTransaccionPorId(
        @Path("id") id: Long
    ): Response<Transaccion>


    @POST("api/retiro")
    suspend fun retiro(@Body request: Retiro): Response<RetiroResponse>

    @POST("api/deposito")
    suspend fun deposito(@Body request: Deposito): Response<DepositoResponse>



}