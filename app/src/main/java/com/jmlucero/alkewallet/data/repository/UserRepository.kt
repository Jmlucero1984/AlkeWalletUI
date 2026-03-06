package com.jmlucero.alkewallet.data.repository

import com.google.gson.Gson
import com.jmlucero.alkewallet.data.api.ApiError
import com.jmlucero.alkewallet.data.api.RetrofitClient
import com.jmlucero.alkewallet.data.model.Balance
import com.jmlucero.alkewallet.data.model.Cuenta
import com.jmlucero.alkewallet.data.model.Deposito
import com.jmlucero.alkewallet.data.model.DepositoResponse
import com.jmlucero.alkewallet.data.model.Retiro
import com.jmlucero.alkewallet.data.model.RetiroResponse

import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.room.CuentaDAO
import com.jmlucero.alkewallet.data.room.UsuarioDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val usuarioDAO: UsuarioDAO,
                                         private val cuentaDAO: CuentaDAO
) {

    private val apiService = RetrofitClient.apiService

    //
    //    fun getUsuario(id: Int) = usuarioDao.getUsuarioById(id)
    //

    suspend fun insertLoggedUsuario(usuario: Usuario) {
        usuarioDAO.insertUser(usuario)
    }


    fun getUsuarioLocal(): Flow<Usuario> {
        return  usuarioDAO.getUsuario()
    }
    fun getCuentaLocal():Flow<Cuenta> {
        return cuentaDAO.getCuenta()
    }

    fun getCuenta(): Flow<Cuenta> {
        return  cuentaDAO.getCuenta()
    }
    //    suspend fun saveUsuario(usuario: UsuarioEntity) {
    //        usuarioDao.insert(usuario)
    //    }

    suspend fun getUsuarioPorId(id: Long): Flow<UiState<Usuario>> =
        safeApiCall {
            apiService.getUsuarioPorId(id)
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
    suspend fun getUsuarios(): Flow<UiState<List<Usuario>>> =
        safeApiCall {
            apiService.getUsuarios()
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