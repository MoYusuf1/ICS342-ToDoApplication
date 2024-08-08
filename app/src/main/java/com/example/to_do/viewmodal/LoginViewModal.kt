package com.example.to_do.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.model.LoginRequest
import com.example.to_do.model.User
import com.example.to_do.network.RetrofitInstance
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<User?>(null)
    val loginState: StateFlow<User?> get() = _loginState

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    _loginState.value = response.body()
                } else {
                    _errorMessage.value = "Login failed"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error"
            }
        }
    }
}
