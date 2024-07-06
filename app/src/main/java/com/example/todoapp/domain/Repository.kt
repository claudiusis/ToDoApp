package com.example.todoapp.domain

import com.example.todoapp.core.Result
import com.example.todoapp.data.repository.TodoItem

interface Repository {
    suspend fun refresh()
    suspend fun getItemById(id: String) : Result<TodoItem?>
    suspend fun addItem(item: TodoItem) : Result<Unit>
    suspend fun deleteItem(id: String) : Result<Unit>
    suspend fun updateItem(item: TodoItem) : Result<Unit>
}