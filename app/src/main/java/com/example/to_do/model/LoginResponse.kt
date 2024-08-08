package com.example.to_do.model

data class LoginResponse(
    val token: String,
    val id: Int,
    val name: String,
    val email: String,
    val enabled: Boolean,
    val admin: Boolean
)