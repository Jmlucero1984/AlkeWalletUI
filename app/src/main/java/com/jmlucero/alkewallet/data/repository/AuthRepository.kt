package com.jmlucero.alkewallet.data.repository

import com.jmlucero.alkewallet.data.api.RetrofitClient
import com.jmlucero.alkewallet.data.model.Cuenta
import com.jmlucero.alkewallet.data.model.DateTime

import com.jmlucero.alkewallet.data.model.LoginRequest
import com.jmlucero.alkewallet.data.model.LoginResponse
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.room.CuentaDAO
import com.jmlucero.alkewallet.data.room.UsuarioDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import retrofit2.Response
import java.sql.Date
import java.sql.Time
import java.time.ZonedDateTime
import javax.inject.Inject

class AuthRepository @Inject constructor( private val usuarioDao: UsuarioDAO,
    private val cuentaDAO: CuentaDAO){

    private val apiService = RetrofitClient.apiService
    suspend fun getCurrentUser(
    ): Flow<UiState<Usuario>> =
        safeApiCall {
            apiService.get_profile()
        }.onEach { state ->
            if (state is UiState.Success) {
                state.data.isLoggedUser=true
                usuarioDao.insertUser(state.data)
                cuentaDAO.insertCuenta(Cuenta(state.data.usuario_id,state.data.balance, System.currentTimeMillis()))

            }
        }


    suspend fun login(
        email: String,
        password: String
    ): Flow<UiState<LoginResponse>> =
        safeApiCall {
            apiService.login(LoginRequest(email, password))
        }.onEach { state ->
            if (state is UiState.Success) {
                RetrofitClient.updateToken(state.data.token)
            }
        }

    private fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>
    ): Flow<UiState<T>> = flow {
        emit(UiState.Loading)

        val response = apiCall()

        if (response.isSuccessful) {
            response.body()?.let {
                emit(UiState.Success(it))
            } ?: emit(UiState.Error("Respuesta vacía"))
        } else {
            val error = response.errorBody()?.string() ?: "Error desconocido"
            emit(UiState.Error("Error ${response.code()}: $error"))
        }
    }.catch {
        emit(UiState.Error(it.message ?: "Error inesperado"))
    }
}