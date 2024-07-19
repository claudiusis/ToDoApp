package com.example.todoapp

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.todoapp.di.AppComponent
import com.example.todoapp.di.DaggerAppComponent
import com.example.todoapp.domain.UserSettings
import com.example.todoapp.ui.MainActivity
import javax.inject.Inject

/**
* Class of application (create network service and repository
*/
class ToDoApp : Application() {
    val appComponent by lazy {
        DaggerAppComponent
            .factory()
            .create(this)
    }

    var navController: NavController? = null

    @Inject
    lateinit var settings: UserSettings

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

}