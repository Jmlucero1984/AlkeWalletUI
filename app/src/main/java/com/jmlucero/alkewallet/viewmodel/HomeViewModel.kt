package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.ViewModel
import com.jmlucero.alkewallet.data.model.Balance
import com.jmlucero.alkewallet.data.model.Cuenta
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario

import com.jmlucero.alkewallet.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    val usuario: Flow<Usuario> = repository.getUsuarioLocal()

    val balance: Flow<UiState<Balance>> = repository.getBalance()
    val cuenta: Flow<Cuenta> = repository.getCuenta()

}

