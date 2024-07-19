package com.example.todoapp.di

import androidx.navigation.NavController
import com.example.todoapp.core.AppInfoScope
import com.example.todoapp.ui.appinfopage.AppInfoFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [AboutAppModule::class])
@AppInfoScope
interface AboutAppComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance navController: NavController) : AboutAppComponent
    }

    fun inject(fragment: AppInfoFragment)

}