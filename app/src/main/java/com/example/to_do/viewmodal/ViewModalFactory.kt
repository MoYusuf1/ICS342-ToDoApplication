package com.example.to_do.viewmodal

import LoginViewModel
import UserPreferencesManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.to_do.viewmodel.CreateAccountViewModel


@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val userPreferencesManager: UserPreferencesManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userPreferencesManager) as T
            }
            modelClass.isAssignableFrom(CreateAccountViewModel::class.java) -> {
                CreateAccountViewModel(userPreferencesManager) as T
            }
            modelClass.isAssignableFrom(TodoListViewModel::class.java) -> {
                TodoListViewModel(userPreferencesManager) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
