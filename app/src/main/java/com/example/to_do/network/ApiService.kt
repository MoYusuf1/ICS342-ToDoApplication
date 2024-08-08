package com.example.to_do.network

import com.example.to_do.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("/api/users/login")
    suspend fun login(@Body request: LoginRequest): Response<User>

    @POST("/api/users/register")
    suspend fun register(@Body request: RegisterRequest): Response<User>

    @GET("/api/users/{user_id}/todos")
    suspend fun getTodos(@Path("user_id") userId: String): Response<List<TodoItem>>

    @POST("/api/users/{user_id}/todos")
    suspend fun createTodo(@Path("user_id") userId: String, @Body request: TodoRequest): Response<TodoItem>

    @PUT("/api/users/{user_id}/todos/{id}")
    suspend fun updateTodo(@Path("user_id") userId: String, @Path("id") todoId: String, @Body request: TodoRequest): Response<TodoItem>
}
