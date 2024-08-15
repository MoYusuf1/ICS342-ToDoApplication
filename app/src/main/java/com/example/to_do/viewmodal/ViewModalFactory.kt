package com.example.to_do.viewmodal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.to_do.datastore.UserPreferencesManager
import com.example.to_do.network.RetrofitInstance
import com.example.to_do.viewmodel.LoginViewModel

class ViewModelFactory(
    private val userPreferencesManager: UserPreferencesManager
) : ViewModelProvider.Factory {

    private val apiService = RetrofitInstance.apiService

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            LoginViewModel::class.java -> LoginViewModel(apiService, userPreferencesManager)
            CreateAccountViewModel::class.java -> CreateAccountViewModel(apiService, userPreferencesManager)
            TodoListViewModel::class.java -> TodoListViewModel(apiService)
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    }
}
