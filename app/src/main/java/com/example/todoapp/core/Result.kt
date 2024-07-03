package com.example.todoapp.core

sealed class Result<out T> {

    class Success<out T>(val data : T) : Result<T>()
    class Error(val e: Exception) : Result<Nothing>()
    data object Loading : Result<Nothing>()

}