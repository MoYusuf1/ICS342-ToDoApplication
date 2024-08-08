package com.example.to_do.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.model.RegisterRequest
import com.example.to_do.model.User
import com.example.to_do.network.RetrofitInstance
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreateAccountViewModel : ViewModel() {
    private val _registerState = MutableStateFlow<User?>(null)
    val registerState: StateFlow<User?> get() = _registerState

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.register(RegisterRequest(email, password))
                if (response.isSuccessful) {
                    _registerState.value = response.body()
                } else {
                    _errorMessage.value = "Registration failed"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error"
            }
        }
    }
}
