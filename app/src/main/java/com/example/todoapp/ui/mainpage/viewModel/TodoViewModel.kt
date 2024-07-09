package com.example.todoapp.ui.mainpage.viewModel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHost
import com.example.todoapp.R
import com.example.todoapp.core.Result
import com.example.todoapp.data.repository.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepositoryImpl
import com.example.todoapp.ui.mainpage.TodoListEvent
import com.example.todoapp.ui.mainpage.UiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/*
* Main viewModel for list
*/
class TodoViewModel(
    private  val repository: TodoItemsRepositoryImpl,
                    private var navHost: NavHost
) : ViewModel() {

    private val _toDoList : MutableStateFlow<List<TodoItem>> = MutableStateFlow(emptyList())
    val todoList = _toDoList.asStateFlow()

    private val _countOfCompleted : MutableStateFlow<Int> = MutableStateFlow(0)
    val countOfCompleted = _countOfCompleted

    private val _eyeVisible : MutableStateFlow<Boolean> = MutableStateFlow(true)
    val eyeVisible = _eyeVisible.asStateFlow()

    private val _uiState : MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.value = UiState.Error(exception.message.toString())
    }

    init {
        collectToDoList()
        getList()
    }

    fun setNavHost(navHost: NavHost){
        this.navHost = navHost
    }

    private fun getList() {
        viewModelScope.launch(exceptionHandler) {
            repository.refresh()
        }
    }
    private fun collectToDoList() {
        viewModelScope.launch(exceptionHandler) {
            repository.toDoListState.collect { result ->
                when(result){
                    is Result.Success -> {
                        val todoItems = if (_eyeVisible.value) {
                            result.data
                        } else {
                            result.data.filter { !it.isCompleted }
                        }
                        _toDoList.update {
                            todoItems
                        }
                        _countOfCompleted.update {
                            result.data.count { it.isCompleted }
                        }
                        _uiState.update {
                            UiState.Success
                        }
                    }
                    is Result.Error -> {
                        val message = result.e.message?:"Произошла ошибка"
                        _uiState.update {
                            UiState.Error(message)
                        }
                    }
                    else -> {
                        _uiState.update {
                            UiState.Loading
                        }
                    }
                }
            }
        }
    }
    private fun deleteNote(id: String) {
        viewModelScope.launch(exceptionHandler) {
            repository.deleteItem(id)
        }
    }
    private fun upDateNote(item: TodoItem){
        viewModelScope.launch(exceptionHandler) {
            repository.updateItem(item)
        }
    }
    private fun updateToDoList() {
        val currentItems = (repository.toDoListState.value as? Result.Success)?.data ?: emptyList()
        _toDoList.update {
            if (!_eyeVisible.value) {
                currentItems.filter { !it.isCompleted }
            } else {
                currentItems
            }
        }
    }

    fun onEvent(event: TodoListEvent){
        when(event){
            is TodoListEvent.DeleteTodo -> {
                deleteNote(event.todo.id)
            }
            is TodoListEvent.ToggleCompleted -> {
                upDateNote(event.todo.copy(isCompleted = !event.todo.isCompleted))
            }
            is TodoListEvent.OnCreateNewPage -> {
                navHost.navController.navigate(R.id.action_mainPageFragment_to_taskPageFragment)
            }
            is TodoListEvent.OnInfoBtnClicked -> {
                Log.d("QWERTY", navHost.toString())
                val bundle = Bundle().apply { putString("id",  event.todoId) }
                navHost.navController.navigate(R.id.action_mainPageFragment_to_taskPageFragment, bundle)
            }
            is TodoListEvent.OnEyeChange -> {
                _eyeVisible.update {
                    !it
                }
                updateToDoList()
            }
            is TodoListEvent.Reload -> {
                getList()
            }
        }
    }
}