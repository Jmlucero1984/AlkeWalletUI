package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmlucero.alkewallet.data.model.Deposito
import com.jmlucero.alkewallet.data.model.DepositoResponse
import com.jmlucero.alkewallet.data.model.LoginResponse
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.repository.AuthRepository
import com.jmlucero.alkewallet.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

       val usuario: Flow<Usuario> = repository.getUsuario()
        // Usuario individual
        private val _usuarioState = MutableStateFlow<UiState<Usuario>>(UiState.Idle)
        val usuarioState: StateFlow<UiState<Usuario>> = _usuarioState

        // Lista usuarios
        private val _usuariosState = MutableStateFlow<UiState<List<Usuario>>>(UiState.Idle)
        val usuariosState: StateFlow<UiState<List<Usuario>>> = _usuariosState

    private val _depositoState = MutableStateFlow<UiState<DepositoResponse>>(UiState.Idle)
    val depositoState: StateFlow<UiState<DepositoResponse>> = _depositoState



        fun cargarUsuario(id: Long) {
            viewModelScope.launch {
                repository.getUsuarioPorId(id).collect {
                    _usuarioState.value = it
                }
            }
        }

    fun depositar(deposito: Deposito) {
        viewModelScope.launch {
            repository.doDeposito(deposito).collect {
                _depositoState.value = it
            }
        }
    }


        fun cargarUsuarios() {
            viewModelScope.launch {
                repository.getUsuarios().collect {
                    _usuariosState.value = it
                }
            }
        }
    }
