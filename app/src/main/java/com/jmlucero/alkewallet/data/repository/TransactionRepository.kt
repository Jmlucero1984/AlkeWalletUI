package com.jmlucero.alkewallet.data.repository

import com.jmlucero.alkewallet.data.api.ApiService
import com.jmlucero.alkewallet.data.model.entity.Transaccion
import com.jmlucero.alkewallet.data.model.entity.TransaccionSimple
import com.jmlucero.alkewallet.data.model.entity.UiState
import kotlinx.coroutines.flow.Flow
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