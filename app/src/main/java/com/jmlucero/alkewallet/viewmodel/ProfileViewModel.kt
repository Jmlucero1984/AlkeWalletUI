package com.jmlucero.alkewallet.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmlucero.alkewallet.data.model.response.AvatarResponse
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    val usuario: Flow<Usuario> = repository.getUsuarioLocal()
    // Usuario individual
    private val _usuarioState = MutableStateFlow<UiState<Usuario>>(UiState.Idle)
    val usuarioState: StateFlow<UiState<Usuario>> = _usuarioState

    private val _uploadAvatarEvent =MutableSharedFlow<UiState<AvatarResponse>>()
    val uploadAvatarEvent= _uploadAvatarEvent.asSharedFlow()

    fun uploadAvatar(avatar: MultipartBody.Part) {
        viewModelScope.launch {
            repository.uploadAvatar(avatar).collect {
                _uploadAvatarEvent.emit(it)
            }
        }
    }



    fun subirAvatar(file: File) {
        Log.i("SUBIR AVATAR", "Subiendo Avatar")
        val requestFile =
            file.asRequestBody("image/png".toMediaTypeOrNull())

        val body =
            MultipartBody.Part.createFormData("avatar", file.name, requestFile)

        uploadAvatar(body)
    }


}