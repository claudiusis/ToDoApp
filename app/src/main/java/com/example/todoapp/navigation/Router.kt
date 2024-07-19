package com.example.todoapp.navigation

import android.os.Bundle
import androidx.navigation.NavController

interface Router {
    fun setNavController(navController: NavController)
    fun navigateTo(route : Int, id : Bundle)
    fun navigate(route: Int)
    fun navigateBack()
}