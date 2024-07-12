package com.example.todoapp.navigation

import android.os.Bundle
import androidx.navigation.NavController
import javax.inject.Inject

class RouterImpl @Inject constructor(
    private val navController: NavController
) : Router {

    override fun navigateTo(route: Int, id: Bundle) {
        navController.navigate(route, id)
    }

    override fun navigate(route: Int) {
        navController.navigate(route)
    }

    override fun navigateBack() {
        navController.popBackStack()
    }
}