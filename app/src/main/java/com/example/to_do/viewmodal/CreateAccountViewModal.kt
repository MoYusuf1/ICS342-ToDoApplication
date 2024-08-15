package com.example.to_do.viewmodal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.model.RegisterRequest
import com.example.to_do.network.ApiService
import com.example.to_do.datastore.UserPreferencesManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.util.Log

class CreateAccountViewModel(
    private val apiService: ApiService,
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun register(name: String, email: String, password: String, apiKey: String) {
        viewModelScope.launch {
            try {
                Log.d("CreateAccountViewModel", "Attempting to register user: $email")
                val response = apiService.registerUser(apiKey, RegisterRequest(name, email, password))
                Log.d("CreateAccountViewModel", "Response received: $response")
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("CreateAccountViewModel", "Registration successful: ${it.id}")
                        userPreferencesManager.saveUserId(it.id.toString())
                        _registerState.value = RegisterState(isSuccess = true, userId = it.id.toString())
                    } ?: run {
                        _errorMessage.value = "Registration failed: Empty response body"
                        Log.e("CreateAccountViewModel", "Registration failed: Empty response body")
                    }
                } else {
                    val errorResponse = response.errorBody()?.string() ?: "Unknown error"
                    _errorMessage.value = "Registration failed: $errorResponse"
                    Log.e("CreateAccountViewModel", "Registration failed: $errorResponse")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
                Log.e("CreateAccountViewModel", "Network error", e)
            }
        }
    }
}

data class RegisterState(
    val isSuccess: Boolean = false,
    val userId: String? = null
)
