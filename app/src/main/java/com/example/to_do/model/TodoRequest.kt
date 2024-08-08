package com.example.to_do.model

data class TodoRequest(
    val description: String,
    val completed: Boolean? = false,
    val meta: Map<String, String>? = null
)