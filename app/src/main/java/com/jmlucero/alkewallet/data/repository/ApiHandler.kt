package com.jmlucero.alkewallet.data.repository

import com.jmlucero.alkewallet.data.model.entity.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class ApiHandler @Inject constructor() {

     fun <T> safeApiCall(apiCall: suspend ()  -> Response<T>): Flow<UiState<T>> = flow {
        emit(UiState.Loading)
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(UiState.Success(it))
                } ?: emit(UiState.Error("Respuesta vacía"))
            } else {
                val error = response.errorBody()?.string() ?: "Error desconocido"
                emit(UiState.Error("Error ${response.code()}: $error"))
            }
        } catch (e: Exception) {
            when (e) {
                is UnknownHostException ->

                    emit(UiState.Error("Sin conexión a internet"))

                is SocketTimeoutException ->
                    emit(UiState.Error("Tiempo de espera agotado"))
                is IOException ->
                    emit(UiState.Error("Error de red"))
                is RuntimeException -> {
                    if (e.message?.contains("Network error") == true) {
                        emit(UiState.Error("Error de red"))
                    }
                    if (e.message?.contains("Timeout") == true) {
                        emit(UiState.Error("Tiempo de espera agotado"))
                    }
                    if(e.message?.contains("Unable to resolve host") == true) {
                        emit(UiState.Error("Sin conexión a internet"))
                    }
                }
                else -> emit(UiState.Error(e.message.toString()))
            }
        }
    }
}

