package com.jmlucero.alkewallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repository = UserRepository()

    // LiveData para un solo usuario
    private val _user = MutableLiveData<Usuario?>()
    val user: LiveData<Usuario?> = _user

    // LiveData para lista de usuarios
    private val _users = MutableLiveData<List<Usuario>>()
    val users: LiveData<List<Usuario>> = _users

    // LiveData para estado de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData para errores
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getUserById(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.getUserById(userId)

            result.onSuccess { user ->
                _user.value = user
            }.onFailure { exception ->
                _error.value = exception.message
            }

            _isLoading.value = false
        }
    }

    fun getAllUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.getAllUsers()

            result.onSuccess { users ->
                _users.value = users
            }.onFailure { exception ->
                _error.value = exception.message
            }

            _isLoading.value = false
        }
    }

    fun createUser(user: Usuario) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.createUser(user)

            result.onSuccess { newUser ->
                // Actualizar la lista o mostrar éxito
                _user.value = newUser
            }.onFailure { exception ->
                _error.value = exception.message
            }

            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}