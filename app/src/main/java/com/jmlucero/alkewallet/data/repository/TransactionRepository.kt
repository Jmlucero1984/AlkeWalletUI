package com.jmlucero.alkewallet.data.repository

import com.jmlucero.alkewallet.data.api.ApiService
import com.jmlucero.alkewallet.data.api.RetrofitClient
import com.jmlucero.alkewallet.data.model.Transaccion
import com.jmlucero.alkewallet.data.model.TransaccionSimple
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject


class TransactionRepository @Inject constructor(
    private val apiService: ApiService,
    private val apiHandler: ApiHandler){


     fun getTransaccionPorId(id: Long): Flow<UiState<Transaccion>> =
        apiHandler.safeApiCall  {
            apiService.getTransaccionPorId(id)
        }

     fun getTransacciones(): Flow<UiState<List<Transaccion>>> =
        apiHandler.safeApiCall  {
            apiService.getTransacciones()
        }

     fun getTransaccionesSimple():  Flow<UiState<List<TransaccionSimple>>> =
        apiHandler.safeApiCall  {
            apiService.getTransaccionesSimple()
        }


}