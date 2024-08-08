package com.example.to_do.ui

import androidx.compose.runtime.*
import com.example.to_do.datastore.UserPreferencesManager
import com.example.to_do.viewmodal.ViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun AppNavigator(
    userPreferencesManager: UserPreferencesManager,
    factory: ViewModelFactory,
    apiKey: String
) {
    val scope = rememberCoroutineScope()
    var userId by remember { mutableStateOf("") }
    var currentScreen by remember { mutableStateOf("login") }

    LaunchedEffect(Unit) {
        userPreferencesManager.userId.collect { id ->
            userId = id ?: ""
            currentScreen = if (userId.isEmpty()) "login" else "main"
        }
    }

    when (currentScreen) {
        "login" -> LoginScreen(
            onLoginSuccess = { id ->
                userId = id
                currentScreen = "main"
            },
            onCreateAccount = {
                currentScreen = "createAccount"
            },
            factory = factory,
            apiKey = apiKey
        )
        "createAccount" -> CreateAccountScreen(
            onAccountCreated = { id ->
                userId = id
                currentScreen = "main"
            },
            onLogin = {
                currentScreen = "login"
            },
            factory = factory,
            apiKey = apiKey
        )
        "main" -> MainScreen(
            userIdKey = userId,
            onLogout = {
                scope.launch {
                    userPreferencesManager.clearUserId()
                    userId = ""
                    currentScreen = "login"
                }
            },
            factory = factory,
            apiKey = apiKey
        )
    }
}
