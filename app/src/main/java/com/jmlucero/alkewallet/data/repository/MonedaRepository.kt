package com.jmlucero.alkewallet.data.repository

import com.jmlucero.alkewallet.data.api.ApiService
import com.jmlucero.alkewallet.data.api.RetrofitClient
import com.jmlucero.alkewallet.data.model.Moneda
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.room.dao.MonedaDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject


class MonedaRepository @Inject constructor(
    private val apiService: ApiService,
    private val monedaDAO: MonedaDAO,
    private val apiHandler: ApiHandler){




     fun getCurrencies():  Flow<UiState<List<Moneda>>> =
        apiHandler.safeApiCall  {
            apiService.getCurrencies()
        }

}