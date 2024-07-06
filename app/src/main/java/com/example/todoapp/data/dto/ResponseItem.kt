package com.example.todoapp.data.dto

import kotlinx.serialization.Serializable
/*
* Class for getting element (GET method)
*/
@Serializable
data class ResponseItem(
    val revision : Int,
    val status : String,
    val element: TodoItemDto,
)