package com.example.todoapp.core

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHost
import com.example.todoapp.data.repository.TodoItemsRepositoryImpl
import com.example.todoapp.ui.taskpage.viewModel.ToDoItemViewModel

/*
* Factory for TodoItemViewModel
*/
class ToDoItemViewModelFactory(
    private val repository: TodoItemsRepositoryImpl,
    private val navHost: NavHost
    ):
    AbstractSavedStateViewModelFactory() {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(ToDoItemViewModel::class.java)){
            return ToDoItemViewModel(handle, repository, navHost) as T
        }
        throw RuntimeException("Incorrect class")
    }
}
