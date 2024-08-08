package com.example.to_do.viewmodal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.model.TodoItem
import com.example.to_do.model.TodoRequest
import com.example.to_do.network.RetrofitInstance
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TodoListViewModel : ViewModel() {
    private val _todoItems = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoItems: StateFlow<List<TodoItem>> get() = _todoItems

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    fun getTodos(userId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getTodos(userId)
                if (response.isSuccessful) {
                    _todoItems.value = response.body() ?: emptyList()
                } else {
                    _errorMessage.value = "Failed to load todos"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error"
            }
        }
    }

    fun createTodo(userId: String, text: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.createTodo(userId, TodoRequest(text, false))
                if (response.isSuccessful) {
                    _todoItems.value += response.body()!!
                } else {
                    _errorMessage.value = "Failed to create todo"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error"
            }
        }
    }

    fun updateTodo(userId: String, todoId: String, isCompleted: Boolean) {
        viewModelScope.launch {
            try {
                val todo = _todoItems.value.find { it.id == todoId }
                if (todo != null) {
                    val response = RetrofitInstance.api.updateTodo(userId, todoId, TodoRequest(todo.text, isCompleted))
                    if (response.isSuccessful) {
                        _todoItems.value = _todoItems.value.map {
                            if (it.id == todoId) response.body()!! else it
                        }
                    } else {
                        _errorMessage.value = "Failed to update todo"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error"
            }
        }
    }
}
