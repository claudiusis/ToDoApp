package com.example.todoapp.di

import androidx.navigation.NavController
import com.example.todoapp.core.CreationFeatureScope
import com.example.todoapp.ui.taskpage.TaskPageFragment
import com.example.todoapp.ui.taskpage.viewModel.ToDoItemViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [CreationFeatureModule::class])
@CreationFeatureScope
interface CreationFeatureComponent {

    fun viewModelFactory() : ToDoItemViewModel.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance navController: NavController) :  CreationFeatureComponent
    }

    fun inject(fragment: TaskPageFragment)

}