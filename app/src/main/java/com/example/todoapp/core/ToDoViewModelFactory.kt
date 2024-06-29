package com.example.todoapp.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHost
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.ui.mainpage.viewModel.TodoViewModel

@Suppress("UNCHECKED_CAST")
class ToDoViewModelFactory(
    private val repository: TodoItemsRepository,
    private val navHost: NavHost) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)){
            return TodoViewModel(repository, navHost) as T
        }
        throw Exception("Class cast exception")
    }
}