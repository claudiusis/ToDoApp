package com.example.todoapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostList(
    @SerialName("status")
    val status : String = "ok",
    @SerialName("list")
    val list: List<TodoItemDto>,
)