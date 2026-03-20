package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmlucero.alkewallet.data.model.entity.Moneda
import com.jmlucero.alkewallet.data.model.entity.SignUpNuevoUsuario
import com.jmlucero.alkewallet.data.model.response.SignUpResponse
import com.jmlucero.alkewallet.data.model.entity.UiState
import com.jmlucero.alkewallet.data.repository.MonedaRepository
import com.jmlucero.alkewallet.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: UserRepository,
    private val monedaRepository: MonedaRepository
) : ViewModel() {




    private val _signUpEvent =MutableSharedFlow<UiState<SignUpResponse>>()
    val signUpEvent= _signUpEvent.asSharedFlow()

/*
    private val _getCurrenciesEvent =MutableSharedFlow<UiState<List<Moneda>>>()
    val getCurrenciesEvent= _getCurrenciesEvent.asSharedFlow()


    fun getCurrencies() {
        viewModelScope.launch {
            monedaRepository.getCurrencies().collect {
                _getCurrenciesEvent.emit(it)
            }
        }
    }
    */

    private val _currenciesState = MutableStateFlow<UiState<List<Moneda>>>(UiState.Idle)
    val currenciesState: StateFlow<UiState<List<Moneda>>> = _currenciesState

    fun getCurrencies() {
        viewModelScope.launch {
            monedaRepository.getCurrencies().collect {
                _currenciesState.value = it
            }
        }
    }
            fun signUpUsuario(signUpNuevoUsuario: SignUpNuevoUsuario) {
        viewModelScope.launch {
            repository.signUpUsuario(signUpNuevoUsuario).collect {
                _signUpEvent.emit(it)
            }
        }
    }


}