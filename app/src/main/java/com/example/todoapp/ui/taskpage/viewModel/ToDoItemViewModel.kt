package com.example.todoapp.ui.taskpage.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHost
import com.example.todoapp.core.ChangeType
import com.example.todoapp.core.Importance
import com.example.todoapp.core.Result
import com.example.todoapp.data.repository.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.ui.mainpage.UiState
import com.example.todoapp.ui.taskpage.TaskEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class ToDoItemViewModel(
    private val savedStateHandle: SavedStateHandle? = null,
    private val repository: TodoItemsRepository = TodoItemsRepository(),
    private val navHost: NavHost? = null
) : ViewModel() {

    private var _toDoItem by mutableStateOf<TodoItem?>(null)
    var text by mutableStateOf("")
        private set
    var deadline by mutableStateOf<Date?>(null)
        private set
    var _importance by mutableStateOf<Importance>(Importance.Normal)
        private set

    var switchState by mutableStateOf(false)
        private set

    var deleteState by mutableStateOf<Boolean>(false)
        private set

    private val _uiState : MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.value = UiState.Error(exception.message.toString())
    }

    init {
        val id = savedStateHandle?.get<String>("id") ?: "-1"
        if (id != "-1") {
            viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                repository.getItemById(id).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            text = result.data.text
                            deadline = result.data.deadLine
                            _importance = result.data.importance
                            deleteState = true
                            deadline?.let {
                                switchState = true
                            }

                            _uiState.update {
                                UiState.Success
                            }

                            this@ToDoItemViewModel._toDoItem = result.data
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
        } else {
            deleteState = false
            _uiState.update {
                UiState.Success
            }
        }
    }

    fun onEvent(event: TaskEvent){
        when(event){
            is TaskEvent.OnTextChange -> {
                text = event.text
            }
            is TaskEvent.OnBackClicked -> {
                navHost?.navController?.popBackStack()
            }
            is TaskEvent.OnDeleteClickedChange -> {
                if (deleteState) {
                    viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
                        repository.deleteItem(_toDoItem!!)
                    }
                    navHost?.navController?.popBackStack()
                }
            }
            is TaskEvent.OnSaveClickedChange -> {
                viewModelScope.launch(Dispatchers.IO + exceptionHandler) {

                    val item = TodoItem(
                        _toDoItem?.id?:UUID.randomUUID().toString(),
                        text,
                        _importance,
                        deadline,
                        _toDoItem?.isCompleted?:false,
                        _toDoItem?.creationDate?: Date(),
                        _toDoItem?.let { Date() }
                    )
                    if (_toDoItem==null){
                        repository.addItem(item)
                    } else {
                        repository.updateItem(item)
                    }
                }
                navHost?.navController?.popBackStack()
            }
            is TaskEvent.OnImportanceChange -> {
                _importance = event.importance
            }
            is TaskEvent.OnDeadLineChange -> {
                deadline = event.date
                _uiState.update {
                    UiState.Success
                }
            }
            is TaskEvent.OnCancelClicked -> {
                if (deadline==null){
                    switchState = false
                }
                _uiState.update {
                    UiState.Success
                }
            }
            is TaskEvent.OnSwitchChange -> {
                switchState = !switchState

                if (!switchState){
                    deadline = null
                } else if (deadline==null){
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
        }
    }
}
