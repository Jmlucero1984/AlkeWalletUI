package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmlucero.alkewallet.data.model.entity.Balance
import com.jmlucero.alkewallet.data.model.entity.UiState
import com.jmlucero.alkewallet.data.model.entity.Usuario
import com.jmlucero.alkewallet.data.model.entity.UsuarioConMoneda

import com.jmlucero.alkewallet.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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

    val usuario: Flow<Usuario?> = repository.getUsuarioLocal()

    val usuarioConMoneda: Flow<UsuarioConMoneda?> = repository.getUsuarioConMonedaLocal()

    val balance: Flow<UiState<Balance>> = repository.getBalance()
   // val cuenta: Flow<Cuenta?> =  repository.getCuentaUsuarioLogueado()

    val cuenta = usuarioConMoneda.flatMapLatest { user ->
        if (user == null) flowOf(null)
        else  repository.getCuentaUsuarioLogueado()
    }

    }



