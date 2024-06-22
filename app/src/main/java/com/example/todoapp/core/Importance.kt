package com.example.todoapp.core

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class Importance {
    LOW,
    NORMAL,
    URGENT
}

object ChangeType {
    fun importanceToString(type: Importance) : String  {
        return when(type){
            Importance.LOW -> "Низкий"
            Importance.NORMAL -> "Нет"
            Importance.URGENT -> "!! Высокий"
        }
    }

    fun changeDateFormat(date: Date) : String {
        val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
        return outputFormat.format(date)
    }
}