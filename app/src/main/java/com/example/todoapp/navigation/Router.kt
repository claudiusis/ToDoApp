package com.example.todoapp.navigation

import android.os.Bundle

interface Router {
    fun navigateTo(route : Int, id : Bundle)
    fun navigate(route: Int)
    fun navigateBack()
}