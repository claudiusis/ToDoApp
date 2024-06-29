package com.example.todoapp.ui.mainpage

sealed interface UiState {
    data object Loading : UiState
    data class Error(val error: String, ) : UiState
    data object Success : UiState
    data object Dialog : UiState
    data object DropDownMenu : UiState
}