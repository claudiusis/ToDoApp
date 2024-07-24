package com.example.todoapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.todoapp.core.CreationFeatureScope
import com.example.todoapp.core.ListFeatureScope
import com.example.todoapp.core.ToDoViewModelFactory
import com.example.todoapp.core.ViewModelKey
import com.example.todoapp.navigation.Router
import com.example.todoapp.navigation.RouterImpl
import com.example.todoapp.ui.mainpage.viewModel.TodoViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface ListFeatureModule {

    @ListFeatureScope
    @Binds
    @IntoMap
    @ViewModelKey(TodoViewModel::class)
    fun bindTodoViewModel(todoViewModel: TodoViewModel): ViewModel

    @ListFeatureScope
    @Binds
    fun bindViewModelFactory(factory: ToDoViewModelFactory): ViewModelProvider.Factory

    @ListFeatureScope
    @Binds
    fun bindRoute(routerImpl: RouterImpl) : Router

}