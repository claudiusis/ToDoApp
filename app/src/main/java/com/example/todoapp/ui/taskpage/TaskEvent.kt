package com.example.todoapp.ui.taskpage

import com.example.todoapp.core.Importance
import java.util.Date
/*
Interface of events on Main Page, that trigger ui and change data
 */
sealed interface TaskEvent {
    data class OnTextChange(val text: String): TaskEvent
    data class OnDeadLineChange(val date: Date) : TaskEvent
    data class OnImportanceChange(val importance: Importance) : TaskEvent
    data object OnSaveClickedChange : TaskEvent
    data object OnDeleteClickedChange : TaskEvent
    data object OnBackClicked : TaskEvent
    data object OnSwitchChange : TaskEvent
    data object OnCancelClicked : TaskEvent
    data object OnTextDeadlineClicked : TaskEvent
}