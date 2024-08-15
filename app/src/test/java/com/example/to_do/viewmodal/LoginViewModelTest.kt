package com.example.to_do.viewmodal

import com.example.to_do.network.ApiService
import com.example.to_do.datastore.UserPreferencesManager
import com.example.to_do.model.LoginResponse
import com.example.to_do.viewmodel.LoginViewModel
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.*
import retrofit2.Response
import org.junit.jupiter.api.Assertions.*

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var apiService: ApiService
    private lateinit var userPreferencesManager: UserPreferencesManager

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        apiService = mockk()
        userPreferencesManager = mockk()
        loginViewModel = LoginViewModel(apiService, userPreferencesManager)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login should succeed with valid credentials`() = runTest {
        val mockResponse = Response.success(LoginResponse(id = 1, name = "test", admin = true, email = "test@gmail.com", token = "1111", enabled = true))
        coEvery { apiService.loginUser(any(), any()) } returns mockResponse
        coEvery { userPreferencesManager.saveUserId(any()) } just Runs

        loginViewModel.login("john@example.com", "password123")

        advanceUntilIdle()

        assertTrue(loginViewModel.loginState.value.isSuccess)
        assertEquals("1", loginViewModel.loginState.value.userId)
        coVerify { userPreferencesManager.saveUserId("1") }
    }

    @Test
    fun `login should fail with invalid credentials`() = runTest {
        val mockResponse = Response.error<LoginResponse>(401, mockk(relaxed = true))
        coEvery { apiService.loginUser(any(), any()) } returns mockResponse

        loginViewModel.login("john@example.com", "wrongpassword")

        advanceUntilIdle()

        assertFalse(loginViewModel.loginState.value.isSuccess)
        assertNotNull(loginViewModel.errorMessage.value)
    }
}
