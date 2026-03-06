package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.ViewModel
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    val usuario: Flow<Usuario> = repository.getUsuario()
    // Usuario individual
    private val _usuarioState = MutableStateFlow<UiState<Usuario>>(UiState.Idle)
    val usuarioState: StateFlow<UiState<Usuario>> = _usuarioState
}