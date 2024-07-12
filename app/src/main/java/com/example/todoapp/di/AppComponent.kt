package com.example.todoapp.di

import android.content.Context
import com.example.todoapp.ToDoApp
import com.example.todoapp.core.AppScope
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class])
@AppScope
interface AppComponent {

    fun listFeature() : ListFeatureComponent.Factory
    fun creationFeature() : CreationFeatureComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context) : AppComponent
    }

    fun inject(app : ToDoApp)

}