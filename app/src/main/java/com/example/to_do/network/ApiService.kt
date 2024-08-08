package com.example.to_do.network

import com.example.to_do.model.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {
    data class LoginRequest(val email: String, val password: String)
    data class LoginResponse(val userId: String)

    @POST("login")
    suspend fun login(@Body request: LoginRequest): retrofit2.Response<LoginResponse>

    companion object {
        private var retrofitService: ApiService? = null

        fun getInstance(): ApiService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.example.com/") // Replace with your API base URL
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(ApiService::class.java)
            }
            return retrofitService!!
        }
    }

    @POST("/api/users/register")
    suspend fun register(@Body request: RegisterRequest): Response<User>

    @GET("/api/users/{user_id}/todos")
    suspend fun getTodos(@Path("user_id") userId: String): Response<List<TodoItem>>

    @POST("/api/users/{user_id}/todos")
    suspend fun createTodo(@Path("user_id") userId: String, @Body request: TodoRequest): Response<TodoItem>

    @POST("/api/users/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<User>

    @PUT("/api/users/{user_id}/todos/{id}")
    suspend fun updateTodo(@Path("user_id") userId: String, @Path("id") todoId: String, @Body request: TodoRequest): Response<TodoItem>
}

