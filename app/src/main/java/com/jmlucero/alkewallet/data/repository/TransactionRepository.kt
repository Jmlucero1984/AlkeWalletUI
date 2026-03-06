package com.jmlucero.alkewallet.data.repository

import com.jmlucero.alkewallet.data.api.RetrofitClient
import com.jmlucero.alkewallet.data.model.Transaccion
import com.jmlucero.alkewallet.data.model.TransaccionSimple
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response


class TransactionRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getTransaccionPorId(id: Long): Flow<UiState<Transaccion>> =
        safeApiCall {
            apiService.getTransaccionPorId(id)
        }

    suspend fun getTransacciones(): Flow<UiState<List<Transaccion>>> =
        safeApiCall {
            apiService.getTransacciones()
        }

    suspend fun getTransaccionesSimple():  Flow<UiState<List<TransaccionSimple>>> =
        safeApiCall {
            apiService.getTransaccionesSimple()
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