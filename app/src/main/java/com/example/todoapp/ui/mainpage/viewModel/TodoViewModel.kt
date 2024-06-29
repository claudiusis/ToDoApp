package com.example.todoapp.ui.mainpage.viewModel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHost
import com.example.todoapp.R
import com.example.todoapp.core.Result
import com.example.todoapp.data.repository.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.ui.mainpage.TodoListEvent
import com.example.todoapp.ui.mainpage.UiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoViewModel(private  val repository: TodoItemsRepository = TodoItemsRepository(),
                    private var navHost: NavHost) : ViewModel() {

    private val _todoList : MutableStateFlow<List<TodoItem>> = MutableStateFlow(listOf())
    val todoList = _todoList.asStateFlow()

    private val _countOfCompleted : MutableStateFlow<Int> = MutableStateFlow(_todoList.value.count { it.isCompleted })
    val countOfCompleted = _countOfCompleted

    private val _eyeVisible : MutableStateFlow<Boolean> = MutableStateFlow(true)
    val eyeVisible = _eyeVisible.asStateFlow()


    private val _uiState : MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.value = UiState.Error(exception.message.toString())
    }

    init {
        getToDoList()
    }

    fun setNavHost(navHost: NavHost){
        this.navHost = navHost
    }
    fun getToDoList() {
            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                repository.getAllItems().collect { result ->
                    when(result){
                        is Result.Success -> {
                            _uiState.update {
                                UiState.Success
                            }
                            if (_eyeVisible.value) {
                                _todoList.update {
                                    result.data
                                }
                            } else {
                                _todoList.update {
                                    result.data.filter { !it.isCompleted }
                                }
                            }
                            _countOfCompleted.update {
                                result.data.count{ it.isCompleted }
                            }

                        }
                        is Result.Loading -> {
                            _uiState.update {
                                UiState.Loading
                            }
                        }
                        is Result.Error -> {
                            _uiState.update {
                                UiState.Error(result.e.message.toString())
                            }
                        }
                    }
                }
            }
    }

    private fun deleteNote(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            repository.deleteItem(item)
        }
    }

    private fun upDateNote(item: TodoItem){
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            repository.updateItem(item)
        }
    }

    fun onEvent(event: TodoListEvent){
        when(event){
            is TodoListEvent.DeleteTodo -> {
                deleteNote(event.todo)
                getToDoList()
            }
            is TodoListEvent.ToggleCompleted -> {
                upDateNote(event.todo.copy(isCompleted = !event.todo.isCompleted))
                getToDoList()
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
                getToDoList()
            }

        }
    }
}