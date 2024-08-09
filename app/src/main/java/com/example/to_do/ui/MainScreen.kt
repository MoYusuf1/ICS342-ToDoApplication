package com.example.to_do.ui

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.to_do.viewmodel.TodoListViewModel

@Composable
fun MainScreen(
    userIdKey: String?,
    onLogout: () -> Unit,
    apiKey: String,
    todoListViewModel: TodoListViewModel
) {
    var newTodoText by remember { mutableStateOf("") }
    val todoList by todoListViewModel.todoList.collectAsState()

    LaunchedEffect(Unit) {
        todoListViewModel.loadTodos(apiKey)
    }

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
                    apiKey = apiKey
                )
            }
        }) {
            Text("Add Todo")
        }

        LazyColumn {
            items(todoList) { todo ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = todo.description)
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        todoListViewModel.deleteTodo(
                            apiKey = apiKey,
                            id = todo.id
                        )
                    }) {
                        Text("Delete")
                    }
                    Button(onClick = {
                        todoListViewModel.updateTodo(
                            apiKey = apiKey,
                            id = todo.id,
                            description = todo.description,
                            completed = !todo.completed
                        )
                    }) {
                        Text(if (todo.completed) "Mark Incomplete" else "Mark Complete")
                    }
                }
            }
        }

        Button(onClick = onLogout) {
            Text("Logout")
        }
    }
}

