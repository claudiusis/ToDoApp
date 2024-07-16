package com.example.todoapp.core

import com.example.todoapp.data.repository.TodoItem
/*
* Network result class
*/
sealed class Result<out T> {
    class Success<T>(val data: T) : Result<T>()
    class Error(val e: Exception, val data : TodoItem? = null) : Result<Nothing>()
}