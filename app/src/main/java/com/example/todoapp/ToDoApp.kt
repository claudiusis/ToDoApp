package com.example.todoapp

import android.app.Application
import androidx.fragment.app.Fragment
import com.example.todoapp.di.AppComponent
import com.example.todoapp.di.DaggerAppComponent

/**
* Class of application (create network service and repository
*/
class ToDoApp: Application() {

    val appComponent : AppComponent by lazy {
        DaggerAppComponent
            .factory()
            .create(this)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

}

fun Fragment.getAppComponent(): AppComponent =
    (requireContext() as ToDoApp).appComponent