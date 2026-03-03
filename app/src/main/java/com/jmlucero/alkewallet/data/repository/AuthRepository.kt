package com.jmlucero.alkewallet.data.repository

import com.jmlucero.alkewallet.data.api.RetrofitClient

import com.jmlucero.alkewallet.data.model.LoginRequest
import com.jmlucero.alkewallet.data.model.LoginResponse
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import retrofit2.Response
class AuthRepository() {

    private val apiService = RetrofitClient.apiService

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