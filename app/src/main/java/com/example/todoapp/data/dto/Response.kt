package com.example.todoapp.data.dto

import kotlinx.serialization.Serializable
/*
* Class for getting list (GET method)
*/
@Serializable
data class Response(
    val revision : Int,
    val status : String,
    val list: List<TodoItemDto>,
)