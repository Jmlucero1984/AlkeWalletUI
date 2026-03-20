package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmlucero.alkewallet.data.model.entity.TransaccionSimple
import com.jmlucero.alkewallet.data.model.entity.UiState
import com.jmlucero.alkewallet.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
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
