package com.example.to_do.viewmodal

import com.example.to_do.model.TodoItem
import com.example.to_do.network.ApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import retrofit2.Response

@ExperimentalCoroutinesApi
class TodoListViewModelTest {

    private lateinit var viewModel: TodoListViewModel
    private lateinit var apiService: ApiService
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        apiService = mockk()
        viewModel = TodoListViewModel(apiService)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadTodos should load todos successfully`() = runTest {
        val mockTodos = listOf(TodoItem(1, "Test Todo", false, null))
        coEvery { apiService.getAllTodos(any()) } returns Response.success(mockTodos)

        viewModel.loadTodos("apiKey")

        advanceUntilIdle()

        assertEquals(mockTodos, viewModel.todoList.value)
    }

    @Test
    fun `createTodo should create todo successfully`() = runTest {
        val newTodo = TodoItem(1, "New Todo", false, null)
        coEvery { apiService.createTodo(any(), any()) } returns Response.success(newTodo)

        viewModel.createTodo("apiKey", "New Todo", false)

        advanceUntilIdle()

        assertTrue(viewModel.todoList.value.contains(newTodo))
    }

    @Test
    fun `updateTodo should update todo successfully`() = runTest {
        val updatedTodo = TodoItem(1, "Updated Todo", true, null)
        coEvery { apiService.updateTodo(any(), any(), any()) } returns Response.success(updatedTodo)

        viewModel.updateTodo("apiKey", 1, "Updated Todo", true)

        advanceUntilIdle()

        assertTrue(viewModel.todoList.value.contains(updatedTodo))
    }

    @Test
    fun `deleteTodo should delete todo successfully`() = runTest {
        val todoId = 1
        coEvery { apiService.deleteTodo(any(), any()) } returns Response.success(null)

        viewModel.deleteTodo("apiKey", todoId)

        advanceUntilIdle()

        assertTrue(viewModel.todoList.value.isEmpty())
    }
}
