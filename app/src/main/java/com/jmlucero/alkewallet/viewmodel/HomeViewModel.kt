package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmlucero.alkewallet.data.model.Balance
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.model.UsuarioConMoneda

import com.jmlucero.alkewallet.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
     fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    val usuario: Flow<Usuario> = repository.getUsuarioLocal()
    val usuarioConMoneda: Flow<UsuarioConMoneda> = repository.getUsuarioConMonedaLocal()
    val balance: Flow<UiState<Balance>> = repository.getBalance()
    val cuenta = repository.getUsuarioLocal().flatMapLatest { usuario ->
                usuario?.let { repository.getCuenta(it.email) } ?: emptyFlow()
    }

}

