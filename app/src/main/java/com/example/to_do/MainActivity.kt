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
import com.example.to_do.datastore.UserPreferencesManager
import com.example.to_do.ui.CreateAccountScreen
import com.example.to_do.ui.LoginScreen
import com.example.to_do.ui.MainScreen
import com.example.to_do.ui.theme.TodoTheme
import com.example.to_do.viewmodal.ViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var userPreferencesManager: UserPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPreferencesManager = UserPreferencesManager(applicationContext)
        val factory = ViewModelFactory(userPreferencesManager)
        setContent {
            TodoTheme {
                AppNavigator(userPreferencesManager, factory)
            }
        }
    }
}

@Composable
fun AppNavigator(userPreferencesManager: UserPreferencesManager, factory: ViewModelFactory) {
    var userId by remember { mutableStateOf<String?>(null) }
    var currentScreen by remember { mutableStateOf("Login") }

    LaunchedEffect(Unit) {
        userPreferencesManager.userId.collect { id ->
            userId = id
            currentScreen = if (id != null) "main" else "Login"
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
                userPreferencesManager.clearUserId()
                currentScreen = "Login"
            },
            factory = factory
        )
    }
}
