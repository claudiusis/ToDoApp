package com.example.todoapp.core

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class Importance(val value: String) {
    data object Low : Importance("Низкий")
    data object Normal : Importance("Нет")
    data object Urgent : Importance("!! Высокий")
}

object ChangeType {
    fun changeDateFormat(date: Date) : String {
        val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
        return outputFormat.format(date)
    }
}