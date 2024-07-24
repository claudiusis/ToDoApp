package com.example.todoapp.di

import androidx.navigation.NavController
import com.example.todoapp.core.CreationFeatureScope
import com.example.todoapp.navigation.Router
import com.example.todoapp.navigation.RouterImpl
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Provides

@Module
interface CreationFeatureModule {

    @Binds
    @CreationFeatureScope
    fun bindRoute(routerImpl: RouterImpl) : Router

}