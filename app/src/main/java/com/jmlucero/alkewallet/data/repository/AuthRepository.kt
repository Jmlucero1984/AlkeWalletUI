package com.jmlucero.alkewallet.data.repository

import com.jmlucero.alkewallet.data.api.ApiService
import com.jmlucero.alkewallet.data.api.RetrofitClient
import com.jmlucero.alkewallet.data.api.SessionManager
import com.jmlucero.alkewallet.data.mapper.toMoneda
import com.jmlucero.alkewallet.data.mapper.toUsuario
import com.jmlucero.alkewallet.data.model.Cuenta

import com.jmlucero.alkewallet.data.model.LoginRequest
import com.jmlucero.alkewallet.data.model.response.LoginResponse
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.room.dao.CuentaDAO
import com.jmlucero.alkewallet.data.room.dao.MonedaDAO
import com.jmlucero.alkewallet.data.room.dao.UsuarioDAO
import com.jmlucero.alkewallet.data.room.dto.UsuarioMonedaDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val sessionManager: SessionManager,
    private val apiService: ApiService,
    private val usuarioDao: UsuarioDAO,
    private val cuentaDAO: CuentaDAO,
    private val monedaDAO: MonedaDAO){


    suspend fun getCurrentUser(
    ): Flow<UiState<UsuarioMonedaDTO>> =
        safeApiCall {
            apiService.get_profile()
        }.onEach { state ->
            if (state is UiState.Success) {
                usuarioDao.logoutAllUsers()
                val usuario = state.data.toUsuario(true)
                val moneda = state.data.moneda.toMoneda()

                usuarioDao.insertUser(usuario)
                monedaDAO.insertMoneda(moneda)
                cuentaDAO.insertCuenta(Cuenta(usuario.email,usuario.balance, System.currentTimeMillis()))

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
                sessionManager.saveToken(state.data.token)
                sessionManager.saveEmail(email)
            //RetrofitClient.updateToken(state.data.token)
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