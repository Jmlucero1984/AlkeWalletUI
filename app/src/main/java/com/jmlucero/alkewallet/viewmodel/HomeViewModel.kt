package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.jmlucero.alkewallet.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val repository: UserRepository
) : ViewModel() {

//    val usuario: StateFlow<UsuarioEntity?> =
//        repository.getUsuario()
//            .stateIn(
//                viewModelScope,
//                SharingStarted.WhileSubscribed(5000),
//                null
//            )
}