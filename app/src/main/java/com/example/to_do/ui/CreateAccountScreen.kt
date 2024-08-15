package com.example.to_do.ui

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.to_do.viewmodal.ViewModelFactory
import com.example.to_do.viewmodal.CreateAccountViewModel

@Composable
fun CreateAccountScreen(
    onAccountCreated: (String) -> Unit,
    onLogin: () -> Unit,
    factory: ViewModelFactory,
    apiKey: String // Ensure this is the correct API key
) {
    val createAccountViewModel: CreateAccountViewModel = viewModel(factory = factory)
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val registerState by createAccountViewModel.registerState.collectAsState()
    val errorMessage by createAccountViewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Name") }
        )
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") }
        )
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") }
        )
        Button(onClick = {
            createAccountViewModel.register(name.value.text, email.value.text, password.value.text, apiKey)
        }) {
            Text("Create Account")
        }
        OutlinedButton(onClick = onLogin) { // Ensure this button is present to use onLogin
            Text("Login")
        }
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }

    LaunchedEffect(registerState) {
        if (registerState.isSuccess) {
            registerState.userId?.let { userId ->
                onAccountCreated(userId)
            }
        }
    }
}
