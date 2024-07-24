package com.example.todoapp.di

import android.content.Context
import androidx.navigation.NavController
import com.example.todoapp.ToDoApp
import com.example.todoapp.core.AppScope
import com.example.todoapp.ui.MainActivity
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class])
@AppScope
interface AppComponent {

    fun listFeature() : ListFeatureComponent.Factory
    fun creationFeature() : CreationFeatureComponent.Factory
    fun aboutInfoFeature() : AboutAppComponent.Factory
    fun settingsFeature() : SettingsComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ) : AppComponent
    }

    fun inject(toDoApp : ToDoApp)

}