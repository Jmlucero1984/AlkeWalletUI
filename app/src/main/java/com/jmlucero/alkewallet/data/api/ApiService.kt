package com.jmlucero.alkewallet.data.api


import com.jmlucero.alkewallet.data.model.response.AvatarResponse
import com.jmlucero.alkewallet.data.model.Balance
import com.jmlucero.alkewallet.data.model.Deposito
import com.jmlucero.alkewallet.data.model.response.DepositoResponse
import com.jmlucero.alkewallet.data.model.LoginRequest
import com.jmlucero.alkewallet.data.model.response.LoginResponse
import com.jmlucero.alkewallet.data.model.Moneda
import com.jmlucero.alkewallet.data.model.Retiro
import com.jmlucero.alkewallet.data.model.response.RetiroResponse
import com.jmlucero.alkewallet.data.model.SignUpNuevoUsuario
import com.jmlucero.alkewallet.data.model.response.SignUpResponse
import com.jmlucero.alkewallet.data.model.Transaccion
import com.jmlucero.alkewallet.data.model.TransaccionSimple
import com.jmlucero.alkewallet.data.model.Transferencia
import com.jmlucero.alkewallet.data.model.response.TransferenciaResponse
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.room.dto.UsuarioMonedaDTO
import okhttp3.MultipartBody
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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


    @GET("api/monedas")
    suspend fun  getCurrencies(): Response<List<Moneda>>

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

    @POST("api/transferencia")
    suspend fun transferencia(@Body request: Transferencia): Response<TransferenciaResponse>
    @Multipart
    @POST("api/avatar")
    suspend fun uploadAvatar(
        @Part avatar: MultipartBody.Part
    ): Response<AvatarResponse>

    @POST("api/signup")
    suspend fun signUpUsuario(@Body signUpNuevoUsuario: SignUpNuevoUsuario): Response<SignUpResponse>


}