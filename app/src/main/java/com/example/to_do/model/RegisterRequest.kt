package com.example.to_do.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)