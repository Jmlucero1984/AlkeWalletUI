package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmlucero.alkewallet.data.model.AvatarResponse
import com.jmlucero.alkewallet.data.model.Deposito
import com.jmlucero.alkewallet.data.model.DepositoResponse
import com.jmlucero.alkewallet.data.model.Transferencia
import com.jmlucero.alkewallet.data.model.TransferenciaResponse
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.model.UsuarioConMoneda
import com.jmlucero.alkewallet.data.repository.UserRepository
import com.jmlucero.alkewallet.data.room.UsuarioMonedaDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

       val usuario: Flow<Usuario> = repository.getUsuarioLocal()
        // Usuario individual
        val usuarioConMoneda: Flow<UsuarioConMoneda> = repository.getUsuarioConMonedaLocal()
        private val _usuarioState = MutableStateFlow<UiState<UsuarioMonedaDTO>>(UiState.Idle)
        val usuarioState: StateFlow<UiState<UsuarioMonedaDTO>> = _usuarioState

        private val _usuarioDestinoEvent =MutableSharedFlow<UiState<UsuarioMonedaDTO>>()
        val usuarioDestinoEvent= _usuarioDestinoEvent.asSharedFlow()



        // Lista usuarios
        private val _usuariosState = MutableStateFlow<UiState<List<Usuario>>>(UiState.Idle)
        val usuariosState: StateFlow<UiState<List<Usuario>>> = _usuariosState

        private val _depositoState = MutableStateFlow<UiState<DepositoResponse>>(UiState.Idle)
        val depositoState: StateFlow<UiState<DepositoResponse>> = _depositoState


        private val _transferenciaEvent =MutableSharedFlow<UiState<TransferenciaResponse>>()
        val transferenciaEvent= _transferenciaEvent.asSharedFlow()

        private val _uploadAvatarEvent =MutableSharedFlow<UiState<AvatarResponse>>()
        val uploadAvatarEvent= _uploadAvatarEvent.asSharedFlow()



    fun cargarUsuario(email: String) {
            viewModelScope.launch {
                repository.getUsuarioPorEmail(email).collect {
                    _usuarioDestinoEvent.emit(it)
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

    fun transferir(transferencia: Transferencia) {
        viewModelScope.launch {
            repository.doTransferencia(transferencia).collect {
                _transferenciaEvent.emit(  it)
            }
        }
    }
    fun uploadAvatar(avatar: MultipartBody.Part) {
        viewModelScope.launch {
            repository.uploadAvatar(avatar).collect {
                _uploadAvatarEvent.emit(it)
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
