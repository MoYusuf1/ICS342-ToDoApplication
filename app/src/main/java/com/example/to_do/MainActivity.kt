package com.example.to_do

import UserPreferencesManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.to_do.ui.CreateAccountScreen
import com.example.to_do.ui.LoginScreen
import com.example.to_do.ui.MainScreen
import com.example.to_do.ui.theme.TodoTheme
import com.example.to_do.viewmodal.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var userPreferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPreferencesManager = UserPreferencesManager(applicationContext)
        setContent {
            TodoTheme {
                val factory = ViewModelFactory(userPreferencesManager)
                AppNavigator(userPreferencesManager, factory)
            }
        }
    }
}

@Composable
fun AppNavigator(userPreferencesManager: UserPreferencesManager, factory: ViewModelFactory) {
    var userId by remember { mutableStateOf<String?>(null) }
    var currentScreen by remember { mutableStateOf("Login") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            userPreferencesManager.userId.collect { id ->
                userId = id
                currentScreen = if (id != null) "main" else "Login"
            }
        }
    }

    when (currentScreen) {
        "Login" -> LoginScreen(
            onLoginSuccess = {
                userId = it
                currentScreen = "main"
            },
            onCreateAccount = {
                currentScreen = "createAccount"
            },
            factory = factory
        )
        "createAccount" -> CreateAccountScreen(
            onAccountCreated = { id ->
                userId = id
                currentScreen = "main"
            },
            onLogin = {
                currentScreen = "Login"
            },
            factory = factory
        )
        "main" -> MainScreen(
            userId = userId,
            onLogout = {
                scope.launch {
                    userPreferencesManager.clearUserId()
                    currentScreen = "Login"
                }
            },
            factory = factory
        )
    }
}
