package com.example.todoapp.domain

import com.example.todoapp.core.Importance
import com.example.todoapp.data.dto.PostItem
import com.example.todoapp.data.dto.TodoItemDto
import com.example.todoapp.data.repository.TodoItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/*
* Data converter
*/
object Mapper {

    fun TodoItemDto.toDomain(): TodoItem {
        return TodoItem(
            id = id,
            text = text,
            importance = Importance.fromString(importance),
            deadLine = deadLine?.let { Date(it) },
            isCompleted = isCompleted,
            creationDate = Date(creationDate),
            refactorDate = refactorDate?.let { Date(it) }
        )
    }

    private fun TodoItem.toDto(): TodoItemDto {
        return TodoItemDto(
            id = id,
            text = text,
            importance = Importance.toString(importance),
            color = "#FFFFFF",
            deadLine = deadLine?.time,
            isCompleted = isCompleted,
            creationDate = creationDate.time,
            refactorDate = refactorDate?.time?:Date().time,
            device = "1"
        )
    }

    fun TodoItem.toPostItem() : PostItem {
        return PostItem(
            element = this.toDto()
        )
    }

    fun changeDateFormat(date: Date) : String {
        val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
        return outputFormat.format(date)
    }

}