package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmlucero.alkewallet.data.model.LoginResponse
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository( )
) : ViewModel() {

    // Login
    private val _loginState = MutableStateFlow<UiState<LoginResponse>>(UiState.Idle)
    val loginState: StateFlow<UiState<LoginResponse>> = _loginState

    // Usuario individual


    fun login(email: String, password: String) {

        viewModelScope.launch {

            repository.login(email, password).collect { state ->

                when (state) {

                    is UiState.Loading -> {
                        _loginState.value = UiState.Loading
                    }

                    is UiState.Error -> {
                        _loginState.value = state
                    }

                    is UiState.Success -> {

                        try {
                            val token = state.data.token

                            // 1️⃣ Guardar token
                            // repository.saveToken(token)

                            // 2️⃣ Obtener perfil y guardarlo en Room
                            //repository.refreshUsuario()

                            // 3️⃣ Emitir success real
                            _loginState.value = UiState.Success(state.data)

                        } catch (e: Exception) {
                            _loginState.value =
                                UiState.Error(e.message ?: "Error obteniendo perfil")
                        }
                    }

                    else -> {}
                }
            }
        }
    }


}
