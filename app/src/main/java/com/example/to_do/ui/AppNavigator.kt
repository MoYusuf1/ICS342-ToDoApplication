import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.to_do.datastore.UserPreferencesManager
import com.example.to_do.ui.CreateAccountScreen
import com.example.to_do.ui.LoginScreen
import com.example.to_do.ui.MainScreen
import com.example.to_do.viewmodal.ViewModelFactory
import com.example.to_do.viewmodal.TodoListViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavigator(
    userPreferencesManager: UserPreferencesManager,
    factory: ViewModelFactory,
    apiKey: String
) {
    val scope = rememberCoroutineScope()
    var userId by rememberSaveable { mutableStateOf("") }
    var currentScreen by rememberSaveable { mutableStateOf("login") }

    LaunchedEffect(Unit) {
        userPreferencesManager.userId.collect { id ->
            userId = id ?: ""
            if (userId.isNotEmpty()) {
                currentScreen = "main"
            }
        }
    }

    when (currentScreen) {
        "login" -> LoginScreen(
            onLoginSuccess = { id ->
                scope.launch {
                    userPreferencesManager.saveUserId(id)
                    userId = id
                    currentScreen = "main"
                }
            },
            onCreateAccount = {
                currentScreen = "createAccount"
            },
            factory = factory,
            apiKey = apiKey
        )
        "createAccount" -> CreateAccountScreen(
            onAccountCreated = { id ->
                scope.launch {
                    userPreferencesManager.saveUserId(id)
                    userId = id
                    currentScreen = "main"
                }
            },
            onLogin = {
                currentScreen = "login"
            },
            factory = factory,
            apiKey = apiKey
        )
        "main" -> {
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
