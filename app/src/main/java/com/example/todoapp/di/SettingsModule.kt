package com.example.todoapp.di

import com.example.todoapp.core.AppInfoScope
import com.example.todoapp.core.SettingsScope
import com.example.todoapp.navigation.Router
import com.example.todoapp.navigation.RouterImpl
import dagger.Binds
import dagger.Module

@Module
interface SettingsModule {
    @Binds
    @SettingsScope
    fun bindRoute(routerImpl: RouterImpl) : Router
}