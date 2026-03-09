package com.jmlucero.alkewallet.data.repository

import com.google.gson.Gson
import com.jmlucero.alkewallet.data.api.ApiError
import com.jmlucero.alkewallet.data.api.RetrofitClient
import com.jmlucero.alkewallet.data.mapper.toMoneda
import com.jmlucero.alkewallet.data.mapper.toUsuario
import com.jmlucero.alkewallet.data.model.AvatarResponse
import com.jmlucero.alkewallet.data.model.Balance
import com.jmlucero.alkewallet.data.model.Cuenta
import com.jmlucero.alkewallet.data.model.Deposito
import com.jmlucero.alkewallet.data.model.DepositoResponse
import com.jmlucero.alkewallet.data.model.Retiro
import com.jmlucero.alkewallet.data.model.RetiroResponse
import com.jmlucero.alkewallet.data.model.SignUpNuevoUsuario
import com.jmlucero.alkewallet.data.model.SignUpResponse
import com.jmlucero.alkewallet.data.model.Transferencia
import com.jmlucero.alkewallet.data.model.TransferenciaResponse

import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.model.UsuarioConMoneda
import com.jmlucero.alkewallet.data.room.CuentaDAO
import com.jmlucero.alkewallet.data.room.MonedaDAO
import com.jmlucero.alkewallet.data.room.UsuarioDAO
import com.jmlucero.alkewallet.data.room.UsuarioMonedaDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val usuarioDAO: UsuarioDAO,
                                         private val cuentaDAO: CuentaDAO,
    private val monedaDAO: MonedaDAO
) {

    private val apiService = RetrofitClient.apiService

    suspend fun insertLoggedUsuario(usuario: Usuario) {
        usuarioDAO.insertUser(usuario)
    }

    fun getUsuarioLocal(): Flow<Usuario> {
        return  usuarioDAO.getUsuario()
    }
    fun getUsuarioConMonedaLocal(): Flow<UsuarioConMoneda> {
        return  usuarioDAO.getUsuarioConMoneda()
    }
    fun getCuentaLocal(usuario_email:String):Flow<Cuenta> {
        return cuentaDAO.getCuenta(usuario_email)
    }

    fun getCuenta(usuario_email:String): Flow<Cuenta> {
        return  cuentaDAO.getCuenta(usuario_email)
    }



    suspend fun getUsuarioPorEmail(email: String): Flow<UiState<UsuarioMonedaDTO>> =
        safeApiCall {
            apiService.getUsuarioPorEmail(email)
        }.onEach { state ->
            if (state is UiState.Success) {
                val usuario = state.data.toUsuario(false)
                val moneda = state.data.moneda.toMoneda()
                usuarioDAO.insertUser(usuario)
                monedaDAO.insertMoneda(moneda)
            }
        }


    fun getBalance(): Flow<UiState<Balance>> =
        safeApiCall {
            apiService.getBalance()
        }

    fun doRetiro(retiro: Retiro): Flow<UiState<RetiroResponse>> =
        safeApiCall {
            apiService.retiro(retiro)
        }
    fun doDeposito(deposito: Deposito): Flow<UiState<DepositoResponse>> =
        safeApiCall {
            apiService.deposito(deposito)
        }

    suspend fun uploadAvatar(avatar: MultipartBody.Part):Flow<UiState<AvatarResponse>> =
        safeApiCall {
            apiService.uploadAvatar(avatar)
        }

    fun doTransferencia(transferencia: Transferencia): Flow<UiState<TransferenciaResponse>> =
        safeApiCall {
            apiService.transferencia(transferencia)
        }
    suspend fun getUsuarios(): Flow<UiState<List<Usuario>>> =
        safeApiCall {
            apiService.getUsuarios()
        }
    suspend fun signUpUsuario(signUpNuevoUsuario: SignUpNuevoUsuario): Flow<UiState<SignUpResponse>> =
        safeApiCall {
            apiService.signUpUsuario(signUpNuevoUsuario)
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
            //emit(UiState.Error("Error ${response.code()}: $error"))
            val apiError = Gson().fromJson(error, ApiError::class.java)
            emit(UiState.Error(apiError.mensaje))
        }
    }.catch {
        emit(UiState.Error(it.message ?: "Error inesperado"))
    }
}