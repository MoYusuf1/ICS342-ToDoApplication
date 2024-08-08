package com.example.to_do.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.to_do.TodoDialog
import com.example.to_do.viewmodal.TodoListViewModel
import com.example.to_do.viewmodal.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    userId: String?,
    onLogout: () -> Unit,
    factory: ViewModelFactory
) {
    val todoListViewModel: TodoListViewModel = viewModel(factory = factory)
    val todoItems by todoListViewModel.todoItems.collectAsState()
    val errorMessage by todoListViewModel.errorMessage.collectAsState()
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        if (userId != null) {
            todoListViewModel.getTodos(userId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo App") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { setShowDialog(true) }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            todoItems.forEach { item ->
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item.text, fontSize = 20.sp)
                    Checkbox(
                        checked = item.isCompleted,
                        onCheckedChange = {
                            scope.launch {
                                if (userId != null) {
                                    todoListViewModel.updateTodo(
                                        userId = userId,
                                        todoId = item.id,
                                        isCompleted = it
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }

        if (showDialog) {
            TodoDialog(
                onDismiss = { setShowDialog(false) },
                onSave = { text ->
                    scope.launch {
                        if (userId != null) {
                            todoListViewModel.createTodo(
                                userId = userId,
                                text = text
                            )
                        }
                        setShowDialog(false)
                    }
                }
            )
        }

        if (errorMessage.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = errorMessage, color = Color.Red, fontSize = 16.sp)
            }
        }
    }
}
