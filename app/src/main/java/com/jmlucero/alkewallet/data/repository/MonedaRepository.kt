package com.jmlucero.alkewallet.data.repository

import com.jmlucero.alkewallet.data.api.ApiService
import com.jmlucero.alkewallet.data.model.entity.Moneda
import com.jmlucero.alkewallet.data.model.entity.UiState
import com.jmlucero.alkewallet.data.room.dao.MonedaDAO
import kotlinx.coroutines.flow.Flow
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