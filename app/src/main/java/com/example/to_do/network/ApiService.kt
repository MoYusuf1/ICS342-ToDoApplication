package com.example.to_do.network

import com.example.to_do.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Generic Todos
    @GET("api/todos")
    suspend fun getAllTodos(@Query("apikey") apikey: String): Response<List<TodoItem>>

    @GET("api/todos/{id}")
    suspend fun getTodo(@Path("id") id: Int, @Query("apikey") apikey: String): Response<TodoItem>

    @POST("api/todos")
    suspend fun createTodo(@Query("apikey") apikey: String, @Body todoRequest: TodoRequest): Response<TodoItem>

    @PUT("api/todos/{id}")
    suspend fun updateTodo(@Path("id") id: Int, @Query("apikey") apikey: String, @Body todoRequest: TodoRequest): Response<TodoItem>

    @DELETE("api/todos/{id}")
    suspend fun deleteTodo(@Path("id") id: Int, @Query("apikey") apikey: String): Response<Void>

    @DELETE("api/todos")
    suspend fun deleteAllTodos(@Query("apikey") apikey: String): Response<Void>

    // User Accounts
    @POST("api/users/register")
    suspend fun registerUser(@Query("apikey") apikey: String, @Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("api/users/login")
    suspend fun loginUser(@Query("apikey") apikey: String, @Body loginRequest: LoginRequest): Response<LoginResponse>

    // User Todos
    @GET("api/users/{user_id}/todos")
    suspend fun getUserTodos(@Path("user_id") userId: Int, @Query("apikey") apikey: String, @Header("Authorization") token: String): Response<List<TodoItem>>

    @POST("api/users/{user_id}/todos")
    suspend fun createUserTodo(@Path("user_id") userId: Int, @Query("apikey") apikey: String, @Header("Authorization") token: String, @Body todoRequest: TodoRequest): Response<TodoItem>

    @PUT("api/users/{user_id}/todos/{id}")
    suspend fun updateUserTodo(@Path("user_id") userId: Int, @Path("id") id: Int, @Query("apikey") apikey: String, @Header("Authorization") token: String, @Body todoRequest: TodoRequest): Response<TodoItem>

    @DELETE("api/users/{user_id}/todos/{id}")
    suspend fun deleteUserTodo(@Path("user_id") userId: Int, @Path("id") id: Int, @Query("apikey") apikey: String, @Header("Authorization") token: String): Response<Void>

    @DELETE("api/users/{user_id}/todos")
    suspend fun deleteUserTodos(@Path("user_id") userId: Int, @Query("apikey") apikey: String, @Header("Authorization") token: String): Response<Void>
}
