package com.example.to_do.model

data class RegisterResponse(
    val name: String,
    val email: String,
    val enabled: Boolean,
    val token: String,
    val admin: Boolean,
    val id: Int
)