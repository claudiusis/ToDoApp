package com.example.todoapp.domain

import kotlinx.coroutines.flow.StateFlow

interface UserSettings {
    val themeStream: StateFlow<AppTheme>
    var theme: AppTheme
}