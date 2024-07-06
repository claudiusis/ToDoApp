package com.example.todoapp.core

/*
Class shows importance of toDoItem
*/
sealed class Importance(val value: String) {
    data object Low : Importance("Низкий")
    data object Normal : Importance("Нет")
    data object Urgent : Importance("!! Высокий")
    companion object {
        fun fromString(value: String): Importance {
            return when (value) {
                "important" -> Urgent
                "low" -> Low
                else -> Normal
            }
        }
        fun toString(importance: Importance): String {
            return when (importance) {
                is Urgent -> "important"
                is Low -> "low"
                else -> "basic"
            }
        }
    }
}