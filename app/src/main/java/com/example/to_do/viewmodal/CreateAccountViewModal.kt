package com.example.to_do.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.datastore.UserPreferencesManager
import com.example.to_do.model.RegisterState
import com.example.to_do.model.RegisterRequest
import com.example.to_do.network.RetrofitInstance.api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateAccountViewModel(
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = api.registerUser(RegisterRequest(name, email, password))
                if (response.isSuccessful) {
                    response.body()?.let {
                        _registerState.value = RegisterState(isSuccess = true, userId = it.id)
                    }
                } else {
                    _errorMessage.value = "Registration failed: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Registration failed: ${e.message}"
            }
        }
    }
}
