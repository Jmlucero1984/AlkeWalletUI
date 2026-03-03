package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmlucero.alkewallet.data.model.LoginResponse
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.repository.AuthRepository
import com.jmlucero.alkewallet.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel (
    private val repository: UserRepository = UserRepository()
    ) : ViewModel() {


        // Usuario individual
        private val _usuarioState = MutableStateFlow<UiState<Usuario>>(UiState.Idle)
        val usuarioState: StateFlow<UiState<Usuario>> = _usuarioState

        // Lista usuarios
        private val _usuariosState = MutableStateFlow<UiState<List<Usuario>>>(UiState.Idle)
        val usuariosState: StateFlow<UiState<List<Usuario>>> = _usuariosState

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
