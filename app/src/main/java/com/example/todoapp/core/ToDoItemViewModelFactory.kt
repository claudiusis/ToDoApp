package com.example.todoapp.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHost
import androidx.savedstate.SavedStateRegistryOwner
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.ui.taskpage.viewModel.ToDoItemViewModel

class ToDoItemViewModelFactory(
    private val repository: TodoItemsRepository,
    private val owner: SavedStateRegistryOwner,
    private val defaultArgs: Bundle? = null,
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
        throw Exception("Class cast exception")
    }
}
