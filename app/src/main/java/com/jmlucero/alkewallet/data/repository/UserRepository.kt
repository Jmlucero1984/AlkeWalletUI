package com.jmlucero.alkewallet.data.repository

import com.google.gson.Gson
import com.jmlucero.alkewallet.data.api.ApiError
import com.jmlucero.alkewallet.data.api.ApiService
import com.jmlucero.alkewallet.data.api.RetrofitClient
import com.jmlucero.alkewallet.data.api.SessionManager
import com.jmlucero.alkewallet.data.mapper.toMoneda
import com.jmlucero.alkewallet.data.mapper.toUsuario
import com.jmlucero.alkewallet.data.model.response.AvatarResponse
import com.jmlucero.alkewallet.data.model.Balance
import com.jmlucero.alkewallet.data.model.Cuenta
import com.jmlucero.alkewallet.data.model.Deposito
import com.jmlucero.alkewallet.data.model.response.DepositoResponse
import com.jmlucero.alkewallet.data.model.Retiro
import com.jmlucero.alkewallet.data.model.response.RetiroResponse
import com.jmlucero.alkewallet.data.model.SignUpNuevoUsuario
import com.jmlucero.alkewallet.data.model.response.SignUpResponse
import com.jmlucero.alkewallet.data.model.Transferencia
import com.jmlucero.alkewallet.data.model.response.TransferenciaResponse

import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.model.UsuarioConMoneda
import com.jmlucero.alkewallet.data.room.dao.CuentaDAO
import com.jmlucero.alkewallet.data.room.dao.MonedaDAO
import com.jmlucero.alkewallet.data.room.dao.UsuarioDAO
import com.jmlucero.alkewallet.data.room.dto.UsuarioMonedaDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
    private val usuarioDAO: UsuarioDAO,
    private val cuentaDAO: CuentaDAO,
    private val monedaDAO: MonedaDAO
) {



    suspend fun insertLoggedUsuario(usuario: Usuario) {
        usuarioDAO.insertUser(usuario)
    }

    suspend fun updateBalance(balance: String, usuarioEmail:String) {
        cuentaDAO.updateBalance(balance,usuarioEmail)
    }

    suspend fun updateUsuarioLocalAvatar(url:String) {
        usuarioDAO.updateAvatar(url)
    }
    fun getUsuarioLocal(): Flow<Usuario?> {
        return  usuarioDAO.getUsuario()
    }
    fun getUsuarioConMonedaLocal(): Flow<UsuarioConMoneda?> {
        return  usuarioDAO.getUsuarioConMoneda()
    }
    fun getCuentaLocal(usuario_email:String):Flow<Cuenta?> {
        return cuentaDAO.getCuenta(usuario_email)
    }

    fun getCuenta(usuario_email:String): Flow<Cuenta?> {
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

    suspend fun logout() {
        // 1. Limpiar usuario logueado
        usuarioDAO.logoutAllUsers()

        // 2. Borrar token (MUY importante)
        sessionManager.clearToken()

        // 3. (Opcional pero recomendado) limpiar otras tablas cacheadas
        //transactionDao.clearAll()
        //contactsDao.clearAll()
    }
}