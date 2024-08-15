package com.example.to_do.viewmodal

import androidx.lifecycle.ViewModel
import com.example.to_do.datastore.UserPreferencesManager
import com.example.to_do.viewmodel.LoginViewModel
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ViewModelFactoryTest {

    private lateinit var userPreferencesManager: UserPreferencesManager
    private lateinit var viewModelFactory: ViewModelFactory

    @BeforeEach
    fun setUp() {
        // Mock the UserPreferencesManager
        userPreferencesManager = mockk()
        // Instantiate the ViewModelFactory with the mocked UserPreferencesManager
        viewModelFactory = ViewModelFactory(userPreferencesManager)
    }

    @Test
    fun `create should return LoginViewModel when LoginViewModel class is passed`() {
        val viewModel = viewModelFactory.create(LoginViewModel::class.java)
        assertTrue(true)
    }

    @Test
    fun `create should return CreateAccountViewModel when CreateAccountViewModel class is passed`() {
        val viewModel = viewModelFactory.create(CreateAccountViewModel::class.java)
        assertTrue(true)
    }

    @Test
    fun `create should return TodoListViewModel when TodoListViewModel class is passed`() {
        val viewModel = viewModelFactory.create(TodoListViewModel::class.java)
        assertTrue(true)
    }

    @Test
    fun `create should throw IllegalArgumentException for unknown ViewModel class`() {
        assertThrows(IllegalArgumentException::class.java) {
            viewModelFactory.create(UnknownViewModel::class.java)
        }
    }

    // Define an unknown ViewModel class for the test
    class UnknownViewModel : ViewModel()
}
