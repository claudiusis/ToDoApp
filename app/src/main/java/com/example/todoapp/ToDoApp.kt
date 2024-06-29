package com.example.todoapp

import android.app.Application
import com.example.todoapp.data.repository.TodoItemsRepository

class ToDoApp: Application() {
    val repository : TodoItemsRepository = TodoItemsRepository()
}