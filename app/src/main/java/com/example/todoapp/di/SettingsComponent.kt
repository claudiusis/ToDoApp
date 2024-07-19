package com.example.todoapp.di

import androidx.navigation.NavController
import com.example.todoapp.core.SettingsScope
import com.example.todoapp.ui.settingspage.SettingsPageFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [SettingsModule::class])
@SettingsScope
interface SettingsComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance navController: NavController) : SettingsComponent
    }

    fun inject(fragment: SettingsPageFragment)
}