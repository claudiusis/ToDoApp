package com.example.todoapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.core.Importance
import com.example.todoapp.data.repository.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

class ToDoItemViewModel : ViewModel() {

    private val _toDoItem : MutableLiveData<TodoItem?> = MutableLiveData(null)
    val todoItem : LiveData<TodoItem?> = _toDoItem

    fun setToDoItem(todoItem: TodoItem?){
        _toDoItem.value = todoItem
        importance.value = todoItem?.importance ?: Importance.NORMAL
        deadLine.value = todoItem?.deadLine
    }

    var importance : MutableStateFlow<Importance> = MutableStateFlow(Importance.NORMAL)
        private set

    var deadLine : MutableStateFlow<Date?> = MutableStateFlow(null)
        private set


    fun setDeadLine(date: Date?){
        deadLine.value = date
    }

    fun setImportance(importance: Importance){
        this.importance.value = importance
    }



    fun createToDoItem(id : String = _toDoItem.value?.id?:"-1", text: String) : TodoItem {
        return if (_toDoItem.value==null) {
            TodoItem(
                id,
                text,
                importance.value,
                deadLine.value,
                isCompleted = false,
                creationDate = Date()
            )
        } else {
            TodoItem(
                id,
                text,
                importance.value,
                deadLine.value,
                isCompleted = _toDoItem.value?.isCompleted?:false,
                creationDate = _toDoItem.value!!.creationDate,
                refactorDate = Date()
            )
        }
    }
}