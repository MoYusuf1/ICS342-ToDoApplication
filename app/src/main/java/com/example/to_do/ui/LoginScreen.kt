package com.example.to_do.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.to_do.viewmodal.LoginViewModel
import com.example.to_do.viewmodal.ViewModelFactory

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onCreateAccount: () -> Unit,
    factory: ViewModelFactory
) {
    val loginViewModel: LoginViewModel = viewModel(factory = factory)
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val loginState by loginViewModel.loginState.collectAsState()
    val errorMessage by loginViewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
            loginViewModel.login(email.value.text, password.value.text)
        }) {
            Text("Login")
        }
        OutlinedButton(onClick = onCreateAccount) {
            Text("Create Account")
        }
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    LaunchedEffect(loginState) {
        if (loginState.isSuccess) {
            loginState.userId?.let { userId ->
                onLoginSuccess(userId)
            }
        }
    }
}

