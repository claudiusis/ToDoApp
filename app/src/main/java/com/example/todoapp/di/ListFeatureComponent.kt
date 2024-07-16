package com.example.todoapp.di

import androidx.navigation.NavController
import com.example.todoapp.core.ListFeatureScope
import com.example.todoapp.ui.mainpage.MainPageFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ListFeatureModule::class])
@ListFeatureScope
interface ListFeatureComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance navController: NavController) : ListFeatureComponent
    }

    fun inject(fragment: MainPageFragment)
}