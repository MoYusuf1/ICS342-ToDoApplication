package com.example.to_do.ui

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.to_do.network.apiKey
import com.example.to_do.viewmodal.TodoListViewModel

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
        if (userIdKey != null) {
            todoListViewModel.loadTodos(apiKey = apiKey, userId = userIdKey)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Todo List",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp),
            fontWeight = FontWeight.Bold
        )

        // Todo Input Field and Add Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newTodoText,
                onValueChange = { newTodoText = it },
                label = { Text("New Todo") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                userIdKey?.let {
                    todoListViewModel.createTodo(
                        description = newTodoText,
                        completed = false,
                        apiKey = apiKey
                    )
                }
                newTodoText = ""
            }) {
                Text("Add")
            }
        }

        // Todo List
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(todoList) { todo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = todo.description,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
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
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        todoListViewModel.deleteTodo(
                            apiKey = apiKey,
                            id = todo.id
                        )
                    }) {
                        Text("Delete")
                    }
                }
            }
        }
        // Ensure the onLogout function is used here
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Logout")
        }

    }
}



