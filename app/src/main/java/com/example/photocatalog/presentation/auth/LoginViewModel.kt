package com.example.photocatalog.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photocatalog.domain.usecases.LoginUseCase
import com.example.photocatalog.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    
    private val _loginState = MutableStateFlow<NetworkResult<String>>(NetworkResult.Loading)
    val loginState: StateFlow<NetworkResult<String>> = _loginState.asStateFlow()
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = NetworkResult.Loading
            val result = loginUseCase(username, password)
            _loginState.value = if (result.isSuccess) {
                NetworkResult.Success(result.getOrNull() ?: "")
            } else {
                NetworkResult.Error(result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }
    
    fun resetState() {
        _loginState.value = NetworkResult.Loading
    }
}