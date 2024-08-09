package com.example.to_do.viewmodal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.model.TodoItem
import com.example.to_do.model.TodoRequest
import com.example.to_do.network.ApiService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TodoListViewModel(
    private val apiService: ApiService,
) : ViewModel() {

    private val _todoList = MutableStateFlow<List<TodoItem>>(emptyList())
    val todoList: StateFlow<List<TodoItem>> = _todoList

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun loadTodos(apiKey: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getAllTodos(apiKey) // Assuming your API supports this
                if (response.isSuccessful) {
                    response.body()?.let {
                        _todoList.value = it
                    } ?: run {
                        _errorMessage.value = "Failed to load todos"
                    }
                } else {
                    _errorMessage.value = "Failed to load todos"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error"
            }
        }
    }

    fun createTodo(apiKey: String, description: String, completed: Boolean) {
        viewModelScope.launch {
            try {
                val response = apiService.createTodo(
                    apiKey,
                    TodoRequest(description, completed)
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        _todoList.value += it
                    } ?: run {
                        _errorMessage.value = "Failed to create todo"
                    }
                } else {
                    _errorMessage.value = "Failed to create todo"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error"
            }
        }
    }

    fun updateTodo(apiKey: String, id: Int, description: String, completed: Boolean) {
        viewModelScope.launch {
            try {
                val response = apiService.updateTodo(
                    id,
                    apiKey,
                    TodoRequest(description, completed)
                )
                if (response.isSuccessful) {
                    response.body()?.let { updatedTodo ->
                        _todoList.value = _todoList.value.map { if (it.id == id) updatedTodo else it }
                    } ?: run {
                        _errorMessage.value = "Failed to update todo"
                    }
                } else {
                    _errorMessage.value = "Failed to update todo"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error"
            }
        }
    }

    fun deleteTodo(apiKey: String, id: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteTodo(
                    id,
                    apiKey
                )
                if (response.isSuccessful) {
                    _todoList.value = _todoList.value.filter { it.id != id }
                } else {
                    _errorMessage.value = "Failed to delete todo"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error"
            }
        }
    }
}
