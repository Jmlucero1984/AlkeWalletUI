package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmlucero.alkewallet.data.model.response.LoginResponse
import com.jmlucero.alkewallet.data.model.entity.UiState
import com.jmlucero.alkewallet.data.repository.AuthRepository
import com.jmlucero.alkewallet.data.room.dto.UsuarioMonedaDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    // Login
    private val _loginState = MutableStateFlow<UiState<LoginResponse>>(UiState.Idle)
    val loginState: StateFlow<UiState<LoginResponse>> = _loginState
    private val _loggedUser = MutableStateFlow<UiState<UsuarioMonedaDTO>>(UiState.Idle)
    val loggedUser: StateFlow<UiState<UsuarioMonedaDTO>> = _loggedUser

    // Usuario individual

    fun saveCurrentLoggedUser(){
        viewModelScope.launch {

            repository.getCurrentUser().collect { state ->

                when (state) {

                    is UiState.Loading -> {
                        _loggedUser.value = UiState.Loading
                    }

                    is UiState.Error -> {
                        _loggedUser.value = state
                    }

                    is UiState.Success -> {

                        try {


                            _loggedUser.value = UiState.Success(state.data)


                        } catch (e: Exception) {
                            _loggedUser.value =
                                UiState.Error(e.message ?: "Error obteniendo perfil")
                        }
                    }

                    else -> {}
                }
            }
        }

    }


    fun resetLoginState() {
        _loginState.value = UiState.Idle
    }

    fun resetLoggedUserState() {
        _loggedUser.value = UiState.Idle
    }
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
