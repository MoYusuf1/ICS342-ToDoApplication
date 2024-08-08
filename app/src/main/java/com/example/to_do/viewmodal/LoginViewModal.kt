import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.model.LoginRequest
import com.example.to_do.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val userPreferencesManager: UserPreferencesManager) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> get() = _loginState

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = ApiService.getInstance().login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        userPreferencesManager.saveUserId(loginResponse.userId)
                        _loginState.value = LoginState(isSuccess = true, userId = loginResponse.userId)
                    } ?: run {
                        _loginState.value = LoginState(errorMessage = "Login failed")
                    }
                } else {
                    _loginState.value = LoginState(errorMessage = "Login failed")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error"
            }
        }
    }
}

data class LoginState(val isSuccess: Boolean = false, val userId: String? = null, val errorMessage: String = "")
