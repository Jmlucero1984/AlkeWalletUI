package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmlucero.alkewallet.data.model.LoginResponse
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    // Login
    private val _loginState = MutableStateFlow<UiState<LoginResponse>>(UiState.Idle)
    val loginState: StateFlow<UiState<LoginResponse>> = _loginState

    // Usuario individual
    private val _usuarioState = MutableStateFlow<UiState<Usuario>>(UiState.Idle)
    val usuarioState: StateFlow<UiState<Usuario>> = _usuarioState

    // Lista usuarios
    private val _usuariosState = MutableStateFlow<UiState<List<Usuario>>>(UiState.Idle)
    val usuariosState: StateFlow<UiState<List<Usuario>>> = _usuariosState


    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password).collect {
                _loginState.value = it
            }
        }
    }

    fun cargarUsuario(id: Long) {
        viewModelScope.launch {
            repository.getUsuarioPorId(id).collect {
                _usuarioState.value = it
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
