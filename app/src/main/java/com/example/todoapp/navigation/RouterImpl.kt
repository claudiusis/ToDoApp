package com.example.todoapp.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.example.todoapp.R
import javax.inject.Inject

class RouterImpl @Inject constructor(
    private var navController: NavController
) : Router {

    private val navOptions = navOptions {
        anim {
            enter = R.anim.slide_in
            exit = R.anim.fade_out
            popEnter = R.anim.fade_in
            popExit = R.anim.slide_out
        }
    }

    override fun setNavController(navController: NavController) {
        this.navController = navController
    }

    override fun navigateTo(route: Int, id: Bundle) {
        navController.navigate(route, id, navOptions)
    }

    override fun navigate(route: Int) {
        navController.navigate(route, null, navOptions)
    }

    override fun navigateBack() {
        navController.popBackStack()
    }
}