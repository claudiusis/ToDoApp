package com.example.todoapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
/*
* Common DTO class item
*/
@Serializable
data class TodoItemDto(
    @SerialName("id")
    val id : String,
    @SerialName("text")
    var text : String,
    @SerialName("importance")
    val importance : String,
    @SerialName("color")
    val color : String? = "#FFFFFF",
    @SerialName("deadline")
    val deadLine : Long? = null,
    @SerialName("done")
    val isCompleted : Boolean,
    @SerialName("created_at")
    val creationDate : Long,
    @SerialName("changed_at")
    val refactorDate : Long? = null,
    @SerialName("last_updated_by")
    val device : String
)
