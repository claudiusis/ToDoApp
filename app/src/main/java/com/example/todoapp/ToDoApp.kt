package com.example.todoapp

import android.app.Application
import com.example.todoapp.data.network.KtorHttpClient
import com.example.todoapp.data.network.NetworkService
import com.example.todoapp.data.repository.RepositoryProvider
import com.example.todoapp.data.repository.TodoItemsRepositoryImpl
/*
* Class of application (create network service and repository
*/
class ToDoApp: Application() {
    private val network = NetworkService(KtorHttpClient)
    val repository : TodoItemsRepositoryImpl = TodoItemsRepositoryImpl(network)
    init {
        RepositoryProvider.repository = repository
    }
}