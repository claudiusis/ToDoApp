package com.example.todoapp.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ToDoItems")
data class ToDoItemEntity(
    @PrimaryKey
    val id : String,
    @ColumnInfo(name = "text")
    var text : String,
    @ColumnInfo(name = "importance")
    val importance : String,
    @ColumnInfo(name = "color")
    val color : String? = "#FFFFFF",
    @ColumnInfo(name = "deadline")
    val deadLine : Long? = null,
    @ColumnInfo(name = "done")
    val isCompleted : Boolean,
    @ColumnInfo(name = "created_at")
    val creationDate : Long,
    @ColumnInfo(name = "changed_at")
    val refactorDate : Long? = null,
    @ColumnInfo(name = "last_updated_by")
    val device : String
)