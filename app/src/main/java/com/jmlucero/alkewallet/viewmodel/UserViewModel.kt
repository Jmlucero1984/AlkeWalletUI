package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmlucero.alkewallet.data.model.response.AvatarResponse
import com.jmlucero.alkewallet.data.model.entity.Deposito
import com.jmlucero.alkewallet.data.model.response.DepositoResponse
import com.jmlucero.alkewallet.data.model.entity.Transferencia
import com.jmlucero.alkewallet.data.model.response.TransferenciaResponse
import com.jmlucero.alkewallet.data.model.entity.UiState
import com.jmlucero.alkewallet.data.model.entity.Usuario
import com.jmlucero.alkewallet.data.model.entity.UsuarioConMoneda
import com.jmlucero.alkewallet.data.repository.UserRepository
import com.jmlucero.alkewallet.data.room.dto.UsuarioMonedaDTO
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

    val usuario: Flow<Usuario?> = repository.getUsuarioLocal()
    val usuarioConMoneda: Flow<UsuarioConMoneda?> = repository.getUsuarioConMonedaLocal()
    private val _usuarioState = MutableStateFlow<UiState<UsuarioMonedaDTO>>(UiState.Idle)
    val usuarioState: StateFlow<UiState<UsuarioMonedaDTO>> = _usuarioState

    private val _usuarioDestinoEvent = MutableSharedFlow<UiState<UsuarioMonedaDTO>>()
    val usuarioDestinoEvent = _usuarioDestinoEvent.asSharedFlow()

    suspend fun actualizarBalance(balance: String) {
        repository.getUsuarioLocal().collect { usuario ->
            usuario.let { it?.let { it1 ->

                    repository.updateBalance(balance, it1.email)


            }


            }

        }

    }

    suspend fun getBalance() {
        repository.getUsuarioLocal().collect { usuario ->
            usuario.let { it?.let { it1 -> repository.getBalance() } }
        }

    }


    suspend fun buscarSugerencias(query: String): Flow<List<Usuario>> {

        return repository.getSugerencias(query)
    }



    suspend fun insertarSugerencia(balance: String) {
        repository.getUsuarioLocal().collect { usuario ->
            usuario.let { it?.let { it1 -> repository.updateBalance(balance, it1.email) } }
        }

    }

    private val _usuariosState = MutableStateFlow<UiState<List<Usuario>>>(UiState.Idle)
    val usuariosState: StateFlow<UiState<List<Usuario>>> = _usuariosState

    private val _depositoState = MutableStateFlow<UiState<DepositoResponse>>(UiState.Idle)
    val depositoState: StateFlow<UiState<DepositoResponse>> = _depositoState


    private val _depositoEvent = MutableSharedFlow<UiState<DepositoResponse>>()
    val depositoEvent = _depositoEvent.asSharedFlow()


    private val _transferenciaEvent = MutableSharedFlow<UiState<TransferenciaResponse>>()
    val transferenciaEvent = _transferenciaEvent.asSharedFlow()

    private val _uploadAvatarEvent = MutableSharedFlow<UiState<AvatarResponse>>()
    val uploadAvatarEvent = _uploadAvatarEvent.asSharedFlow()


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
                _depositoEvent.emit(it)

            }
        }
    }

    fun onDepositoSuccess(nuevoSaldo: String) {

        viewModelScope.launch {
            actualizarBalance(nuevoSaldo)
        }
    }

    fun transferir(transferencia: Transferencia) {
        viewModelScope.launch {
            repository.doTransferencia(transferencia).collect {
                _transferenciaEvent.emit(it)
            }
        }
    }

    fun onTransferSuccess(nuevoSaldo: String) {

        viewModelScope.launch {
            actualizarBalance(nuevoSaldo)
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


