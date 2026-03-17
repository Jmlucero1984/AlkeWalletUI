package com.jmlucero.alkewallet.data.repository

import com.jmlucero.alkewallet.data.api.RetrofitClient
import com.jmlucero.alkewallet.data.model.Moneda
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.room.dao.MonedaDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject


class MonedaRepository @Inject constructor(private val monedaDAO: MonedaDAO
) {

    private val apiService = RetrofitClient.apiService

    suspend fun getCurrencies():  Flow<UiState<List<Moneda>>> =
        safeApiCall {
            apiService.getCurrencies()
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