package com.example.todoapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todoitems")
    suspend fun getList(): List<ToDoItemEntity>
    @Query("SELECT * FROM todoitems WHERE id = :id")
    suspend fun getItem(id: String) : ToDoItemEntity
    @Upsert
    suspend fun upsertItem(item : ToDoItemEntity)
    @Upsert
    suspend fun upsertItem(item : List<ToDoItemEntity>)
    @Query("DELETE  FROM todoitems WHERE id = :id")
    suspend fun deleteItem(id : String)
}