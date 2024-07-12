package com.example.todoapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ToDoItemEntity::class],
    version = 1
)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun toDoDao() : ToDoDao
}