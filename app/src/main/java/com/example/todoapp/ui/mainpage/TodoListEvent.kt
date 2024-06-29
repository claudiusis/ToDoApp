package com.example.todoapp.ui.mainpage

import com.example.todoapp.data.repository.TodoItem

sealed interface TodoListEvent {
    data class DeleteTodo(val todo: TodoItem) : TodoListEvent
    data class ToggleCompleted(val todo: TodoItem) : TodoListEvent
    data object OnCreateNewPage : TodoListEvent
    data class OnInfoBtnClicked(val todoId : String) : TodoListEvent
    data object OnEyeChange : TodoListEvent
}