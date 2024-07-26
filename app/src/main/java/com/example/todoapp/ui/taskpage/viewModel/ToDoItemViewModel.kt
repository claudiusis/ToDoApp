package com.example.todoapp.ui.taskpage.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.todoapp.R
import com.example.todoapp.core.Importance
import com.example.todoapp.core.Result
import com.example.todoapp.data.repository.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepositoryImpl
import com.example.todoapp.navigation.Router
import com.example.todoapp.ui.mainpage.UiState
import com.example.todoapp.ui.taskpage.TaskEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

/*
* ViewModel for second page
*/
class ToDoItemViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle?,
    private val repository: TodoItemsRepositoryImpl,
    private val router: Router
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle) : ToDoItemViewModel
    }

    var _toDoItem by mutableStateOf<TodoItem?>(null)
        private set
    var text by mutableStateOf("")
        private set
    var deadline by mutableStateOf<Date?>(null)
        private set
    var importance by mutableStateOf<Importance>(Importance.Normal)
        private set

    var switchState by mutableStateOf(false)
        private set

    var deleteState by mutableStateOf<Boolean>(false)
        private set

    var isShowCancelSnackBar by mutableStateOf(false)
        private set

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.value = UiState.Error(exception.message.toString())
    }

    fun setNavController(navController: NavController){
        router.setNavController(navController)
    }

    init {
        val id = savedStateHandle?.get<String>("id") ?: "-1"
        if (id != "-1") {
            viewModelScope.launch(exceptionHandler) {
                when (val result = repository.getItemById(id)) {
                    is Result.Success -> {
                        result.data?.let {
                            text = result.data.text
                            deadline = result.data.deadLine
                            importance = result.data.importance
                            deleteState = true
                            deadline?.let {
                                switchState = true
                            }
                        }
                        _uiState.update {
                            UiState.Success
                        }
                        this@ToDoItemViewModel._toDoItem = result.data
                    }

                    is Result.Error -> {
                        val message = result.e.message ?: "Произошла ошибка"
                        _uiState.update {
                            UiState.Error(message)
                        }
                        result.data?.let {
                            text = result.data.text
                            deadline = result.data.deadLine
                            importance = result.data.importance
                            deleteState = true
                            deadline?.let {
                                switchState = true
                            }
                        }
                        _uiState.update {
                            UiState.Success
                        }
                        this@ToDoItemViewModel._toDoItem = result.data
                    }
                }
            }
        } else {
            deleteState = false
            _uiState.update {
                UiState.Success
            }
        }
    }

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.OnTextChange -> {
                text = event.text
            }

            is TaskEvent.OnBackClicked -> {
                if (deleteState && isShowCancelSnackBar){
                    deleteItem()
                }
                router.navigateBack()
            }

            is TaskEvent.OnDeleteClickedChange -> {
                if (deleteState) {
                    deleteItem()
                }
            }

            is TaskEvent.OnSaveClickedChange -> {
                viewModelScope.launch(exceptionHandler) {
                    val item = TodoItem(
                        _toDoItem?.id ?: UUID.randomUUID().toString(),
                        text,
                        importance,
                        deadline,
                        _toDoItem?.isCompleted ?: false,
                        _toDoItem?.creationDate ?: Date(),
                        _toDoItem?.let { Date() }
                    )
                    val result = if (_toDoItem == null) {
                        repository.addItem(item)
                    } else {
                        repository.updateItem(item)
                    }
                    when (result) {
                        is Result.Success -> {}
                        is Result.Error -> {
                            val message = result.e.message ?: "Произошла ошибка"
                            _uiState.update {
                                UiState.Error(message)
                            }
                        }
                    }
                    router.navigateBack()
                }
            }

            is TaskEvent.OnImportanceChange -> {
                importance = event.importance
            }

            is TaskEvent.OnDeadLineChange -> {
                deadline = event.date
                _uiState.update {
                    UiState.Success
                }
            }

            is TaskEvent.OnCancelClicked -> {
                if (deadline == null) {
                    switchState = false
                }
                _uiState.update {
                    UiState.Success
                }
            }

            is TaskEvent.OnSwitchChange -> {
                switchState = !switchState

                if (!switchState) {
                    deadline = null
                } else if (deadline == null) {
                    _uiState.update {
                        UiState.Dialog
                    }
                }
            }

            is TaskEvent.OnTextDeadlineClicked -> {
                _uiState.update {
                    UiState.Dialog
                }
            }

            is TaskEvent.ChangeSnackBarState -> {
                isShowCancelSnackBar = !isShowCancelSnackBar
            }
        }
    }

    private fun deleteItem(){
        viewModelScope.launch(exceptionHandler) {
            when (val result = repository.deleteItem(_toDoItem!!.id)) {
                is Result.Success -> router.navigateBack()
                is Result.Error -> {
                    val message = result.e.message ?: "Произошла ошибка"
                    _uiState.update {
                        UiState.Error(message)
                    }
                    router.navigateBack()
                }
            }
        }
    }
}
