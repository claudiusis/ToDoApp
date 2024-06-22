package com.example.todoapp.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.ui.TodoViewModel

@Suppress("UNCHECKED_CAST")
class ToDoViewModelFactory(private val repository: TodoItemsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)){
            return TodoViewModel(repository) as T
        }
        throw Exception("Class cast exception")
    }
}