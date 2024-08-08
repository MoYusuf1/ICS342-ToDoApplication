package com.example.to_do.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.model.LoginRequest
import com.example.to_do.network.ApiService
import com.example.to_do.datastore.UserPreferencesManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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
                val response = apiService.loginUser("YOUR_API_KEY", LoginRequest(email, password))
                if (response.isSuccessful) {
                    response.body()?.let {
                        userPreferencesManager.saveUserId(it.id.toString())
                        _loginState.value = LoginState(isSuccess = true, userId = it.id.toString())
                    } ?: run {
                        _errorMessage.value = "Login failed"
                    }
                } else {
                    _errorMessage.value = "Login failed"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error"
            }
        }
    }
}

data class LoginState(
    val isSuccess: Boolean = false,
    val userId: String? = null
)
