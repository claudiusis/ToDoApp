package com.example.todoapp.data.repository

import com.example.todoapp.core.Importance
import java.util.Date

data class TodoItem(
    val id : String,
    var text : String,
    var importance : Importance,
    var deadLine : Date? = null,
    var isCompleted : Boolean,
    var creationDate : Date,
    var refactorDate : Date? = null
)
