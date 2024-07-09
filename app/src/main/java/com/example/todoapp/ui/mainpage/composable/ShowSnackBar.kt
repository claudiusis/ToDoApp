package com.example.todoapp.ui.mainpage.composable

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ShowSnackBar(message: String, scaffoldState: SnackbarHostState, scope: CoroutineScope) {
    LaunchedEffect(key1 = Unit) {
        scope.launch {
            scaffoldState.showSnackbar(
                message=message
            )
        }
    }
}