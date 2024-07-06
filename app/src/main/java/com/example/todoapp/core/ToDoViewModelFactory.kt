package com.example.todoapp.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHost
import com.example.todoapp.data.repository.TodoItemsRepositoryImpl
import com.example.todoapp.ui.mainpage.viewModel.TodoViewModel

/*
Factory for ToDoViewModel
*/
@Suppress("UNCHECKED_CAST")
class ToDoViewModelFactory(
    private val repository: TodoItemsRepositoryImpl,
    private val navHost: NavHost) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)){
            return TodoViewModel(repository, navHost) as T
        }
        throw RuntimeException("Incorrect class")
    }
}