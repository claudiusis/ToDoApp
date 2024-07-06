package com.example.todoapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
/*
* Class for sending element (PUT method)
*/
@Serializable
data class PostItem(
    @SerialName("status")
    val status : String = "ok",
    @SerialName("element")
    val element: TodoItemDto,
)
