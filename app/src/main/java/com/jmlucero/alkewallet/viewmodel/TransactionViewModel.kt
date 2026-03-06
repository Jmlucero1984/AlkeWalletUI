package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmlucero.alkewallet.data.model.Transaccion
import com.jmlucero.alkewallet.data.model.TransaccionSimple
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.repository.TransactionRepository
import com.jmlucero.alkewallet.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionViewModel (
    private val repository: TransactionRepository = TransactionRepository()
    ) : ViewModel() {



        private val _transaccionState = MutableStateFlow<UiState<TransaccionSimple>>(UiState.Idle)
        val transaccionState: StateFlow<UiState<TransaccionSimple>> = _transaccionState


        private val _transaccionesState = MutableStateFlow<UiState<List<TransaccionSimple>>>(UiState.Idle)
        val transaccionesState: StateFlow<UiState<List<TransaccionSimple>>> = _transaccionesState



        fun getTransaccionesSimple() {
            viewModelScope.launch {
                repository.getTransaccionesSimple().collect {
                    _transaccionesState.value = it
                }
            }
        }
    }
