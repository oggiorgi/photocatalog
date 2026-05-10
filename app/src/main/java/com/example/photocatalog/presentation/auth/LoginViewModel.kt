package com.example.photocatalog.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photocatalog.domain.usecases.LoginUseCase
import com.example.photocatalog.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResult<String>>(NetworkResult.Idle)
    val loginState: StateFlow<NetworkResult<String>> = _loginState.asStateFlow()

    fun login(username: String, password: String) {
        Log.d("LoginViewModel", "Login called with username: $username")
        viewModelScope.launch {
            _loginState.value = NetworkResult.Loading
            try {
                val result = loginUseCase(username, password)
                Log.d("LoginViewModel", "Result isSuccess: ${result.isSuccess}")
                if (result.isSuccess) {
                    val token = result.getOrNull()
                    Log.d("LoginViewModel", "Token received: ${token?.take(20)}...")
                    _loginState.value = NetworkResult.Success(token ?: "")
                } else {
                    val error = result.exceptionOrNull()
                    Log.e("LoginViewModel", "Login failed", error)
                    _loginState.value = NetworkResult.Error(error?.message ?: "Login failed")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception during login", e)
                _loginState.value = NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetState() {
        _loginState.value = NetworkResult.Loading
    }
}