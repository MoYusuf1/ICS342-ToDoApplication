package com.example.to_do.ui

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.to_do.viewmodal.ViewModelFactory
import com.example.to_do.viewmodel.TodoListViewModel

@Composable
fun MainScreen(
    userIdKey: String?,
    onLogout: () -> Unit,
    factory: ViewModelFactory,
    apiKey: String
) {
    val todoListViewModel: TodoListViewModel = viewModel(factory = factory)
    var newTodoText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = newTodoText,
            onValueChange = { newTodoText = it },
            label = { Text("New Todo") }
        )
        Button(onClick = {
            userIdKey?.let {
                todoListViewModel.createTodo(
                    description = newTodoText,
                    completed = false,
                    apiKey = apiKey,

                )
            }
        }) {
            Text("Add Todo")
        }
        Button(onClick = onLogout) {
            Text("Logout")
        }
    }
}
