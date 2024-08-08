package com.example.to_do.model

data class TodoItem(
    val id: Int,
    val description: String,
    val completed: Boolean,
    val meta: Map<String, String>?
)