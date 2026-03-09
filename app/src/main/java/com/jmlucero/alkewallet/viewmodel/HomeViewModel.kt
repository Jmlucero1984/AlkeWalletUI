package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmlucero.alkewallet.data.model.AvatarResponse
import com.jmlucero.alkewallet.data.model.Balance
import com.jmlucero.alkewallet.data.model.Cuenta
import com.jmlucero.alkewallet.data.model.Deposito
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.model.UsuarioConMoneda

import com.jmlucero.alkewallet.data.repository.UserRepository
import com.jmlucero.alkewallet.data.room.UsuarioMonedaDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    val usuario: Flow<Usuario> = repository.getUsuarioLocal()
    val usuarioConMoneda: Flow<UsuarioConMoneda> = repository.getUsuarioConMonedaLocal()
    val balance: Flow<UiState<Balance>> = repository.getBalance()
    val cuenta =
        repository.getUsuarioLocal()
            .flatMapLatest { usuario ->
                usuario?.let { repository.getCuenta(it.email) }
                    ?: emptyFlow()
            }


    private val _uploadAvatarEvent =MutableSharedFlow<UiState<AvatarResponse>>()
    val uploadAvatarEvent= _uploadAvatarEvent.asSharedFlow()

    fun uploadAvatar(avatar: MultipartBody.Part) {
        viewModelScope.launch {
            repository.uploadAvatar(avatar).collect {
                _uploadAvatarEvent.emit(it)
            }
        }
    }

}

