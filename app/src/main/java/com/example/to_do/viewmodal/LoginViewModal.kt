package com.example.to_do.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.model.LoginRequest
import com.example.to_do.network.ApiService
import com.example.to_do.datastore.UserPreferencesManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.util.Log
import com.example.to_do.network.apiKey

class LoginViewModel(
    private val apiService: ApiService,
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Attempting to log in user: $email")
                val response = apiService.loginUser(apikey = apiKey, LoginRequest(email, password))
                Log.d("LoginViewModel", "Response received: $response")
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("LoginViewModel", "Login successful: ${it.id}")
                        userPreferencesManager.saveUserId(it.id.toString())
                        _loginState.value = LoginState(isSuccess = true, userId = it.id.toString())
                    } ?: run {
                        _errorMessage.value = "Login failed: Empty response body"
                        Log.e("LoginViewModel", "Login failed: Empty response body")
                    }
                } else {
                    val errorResponse = response.errorBody()?.string() ?: "Unknown error"
                    _errorMessage.value = "Login failed: $errorResponse"
                    Log.e("LoginViewModel", "Login failed: $errorResponse")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
                Log.e("LoginViewModel", "Network error", e)
            }
        }
    }
}

data class LoginState(
    val isSuccess: Boolean = false,
    val userId: String? = null
)
