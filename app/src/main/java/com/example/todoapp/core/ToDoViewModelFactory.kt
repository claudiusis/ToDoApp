package com.example.todoapp.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.domain.Repository
import com.example.todoapp.navigation.Router
import com.example.todoapp.ui.mainpage.viewModel.TodoViewModel
import javax.inject.Inject

/**
Factory for ToDoViewModel
*/
class ToDoViewModelFactory @Inject constructor(
    private val repository: Repository,
    private val router: Router) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)){
            return TodoViewModel(repository, router) as T
        }
        throw RuntimeException("Incorrect class")
    }
}
