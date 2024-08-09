import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.to_do.datastore.UserPreferencesManager
import com.example.to_do.ui.CreateAccountScreen
import com.example.to_do.ui.LoginScreen
import com.example.to_do.ui.MainScreen
import com.example.to_do.viewmodal.ViewModelFactory
import com.example.to_do.viewmodel.TodoListViewModel
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
        "main" -> {
            // Create an instance of TodoListViewModel
            val todoListViewModel: TodoListViewModel = viewModel(factory = factory)

            MainScreen(
                userIdKey = userId,
                onLogout = {
                    scope.launch {
                        userPreferencesManager.clearUserId()
                        userId = ""
                        currentScreen = "login"
                    }
                },
                apiKey = apiKey,
                todoListViewModel = todoListViewModel
            )
        }
    }
}
