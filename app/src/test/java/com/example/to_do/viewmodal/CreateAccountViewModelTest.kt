package com.example.to_do.viewmodal

import com.example.to_do.datastore.UserPreferencesManager
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue

@ExperimentalCoroutinesApi
class CreateAccountViewModelTest {

    private lateinit var createAccountViewModel: CreateAccountViewModel
    private lateinit var userPreferencesManager: UserPreferencesManager

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        userPreferencesManager = mockk(relaxed = true)
        createAccountViewModel = CreateAccountViewModel(mockk(), userPreferencesManager)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `registerState should initially be empty`() = runTest {
        val registerState = createAccountViewModel.registerState.first()

        assertFalse(registerState.isSuccess)
        assertNull(registerState.userId)
    }

    @Test
    fun `errorMessage should initially be empty`() = runTest {
        val errorMessage = createAccountViewModel.errorMessage.first()

        assertTrue(errorMessage.isEmpty())
    }
}
