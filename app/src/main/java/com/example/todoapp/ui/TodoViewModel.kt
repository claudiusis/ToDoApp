package com.example.todoapp.ui

import androidx.lifecycle.ViewModel
import com.example.todoapp.data.repository.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TodoViewModel(private  val repository: TodoItemsRepository) : ViewModel() {

    private val _todoList : MutableStateFlow<List<TodoItem>> = MutableStateFlow(listOf())
    val todoList = _todoList.asStateFlow()

    val stateCompleted : MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        getToDoList()
    }

    fun changeCompletedState(){
        stateCompleted.value = !stateCompleted.value
    }

    fun getToDoList() {
        _todoList.value = repository.toDoList.toList()
    }

    fun deleteNote(item: String) {
        repository.deleteItem(item)
        getToDoList()
    }

    fun addNote(item: TodoItem) {
        repository.addTodoItem(item)
        getToDoList()
    }

    fun upDateNote(item: TodoItem){
        repository.updateItem(item)
        getToDoList()
    }

    fun getUnCompleted(){
        _todoList.value = repository.getUnCompleted().toList()
    }

    fun getCompletedCount() = repository.getCompletedAmount()

}