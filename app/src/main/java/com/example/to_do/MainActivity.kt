package com.example.to_do

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.to_do.ui.theme.TodoTheme
import com.example.to_do.viewmodal.TodoListViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val todoListViewModel: TodoListViewModel = viewModel()
    val todoItems by todoListViewModel.todoItems.collectAsState()
    val errorMessage by todoListViewModel.errorMessage.collectAsState()
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Todo App") })
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
                                todoListViewModel.updateTodo(
                                    userId = "user_id",
                                    todoId = item.id,
                                    isCompleted = it
                                )
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
                        todoListViewModel.createTodo(
                            userId = "user_id",
                            text = text
                        )
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

@Composable
fun TodoDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var text by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "New Todo",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { text = TextFieldValue("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (text.text.isNotEmpty()) {
                            onSave(text.text)
                        }
                    }
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(onClick = onDismiss) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (text.text.isNotEmpty()) {
                        onSave(text.text)
                    }
                }) {
                    Text("Save")
                }
            }
        }
    }
}
