package com.example.todoapp.data.repository

import com.example.todoapp.core.Importance
import com.example.todoapp.core.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.util.Date

class TodoItemsRepository {

    private val _toDoList: MutableList<TodoItem> = mutableListOf(
        TodoItem(
            "0",
            "Первая строка",
            Importance.Urgent,
            deadLine = Date(),
            isCompleted = false,
            creationDate = Date()
        ),
        TodoItem(
            "1",
            "Много много много текстаМного много много текстаМного много много текстаМного много много текстаМного много много текста",
            Importance.Normal,
            isCompleted = true,
            creationDate = Date()
        ),
        TodoItem("2", "Text", Importance.Low, isCompleted = true, creationDate = Date()),
        TodoItem(
            "3",
            "Text textText textText textText textText textText textText text",
            Importance.Low,
            isCompleted = false,
            creationDate = Date()
        ),
        TodoItem("4", "Text", Importance.Low, isCompleted = false, creationDate = Date()),
        TodoItem(
            "5",
            "1\n2\n3\n4\n5\n",
            Importance.Urgent,
            isCompleted = false,
            creationDate = Date()
        ),
        TodoItem(
            "6",
            "Купить что-то\nКупить что-то",
            Importance.Low,
            isCompleted = false,
            creationDate = Date()
        ),
        TodoItem("7", "Text", Importance.Normal, isCompleted = false, creationDate = Date()),
        TodoItem(
            "8",
            "Середина",
            Importance.Low,
            isCompleted = true,
            deadLine = Date(),
            creationDate = Date()
        ),
        TodoItem("9", "Text", Importance.Normal, isCompleted = false, creationDate = Date()),
        TodoItem("10", "Новая идея", Importance.Low, isCompleted = false, creationDate = Date()),
        TodoItem(
            "11", "Посмотреть лекции", Importance.Urgent, isCompleted = true,
            deadLine = Date(), creationDate = Date()
        ),
        TodoItem("12", "Text", Importance.Normal, isCompleted = false, creationDate = Date()),
        TodoItem("13", "Сходить в зал", Importance.Low, isCompleted = false, creationDate = Date()),
        TodoItem(
            "14",
            "Integer felis diam, rhoncus pulvinar maximus ac, volutpat eu erat. Vestibulum gravida ac est eu commodo. Suspendisse nulla augue, aliquet non augue quis, mollis finibus massa. Nulla condimentum neque lorem, sed porta dui euismod tempus. Vestibulum tincidunt ligula nunc, lobortis tincidunt turpis sollicitudin nec. Suspendisse consectetur ullamcorper ex non tempor. Sed accumsan hendrerit est eu lobortis. Mauris fringilla, lorem et sollicitudin dapibus, tellus nisi consectetur neque, id porttitor neque eros at velit. Maecenas felis lectus, rhoncus auctor pretium pellentesque, hendrerit in eros. Cras quis ante ligula. Etiam fringilla a ante eu lacinia. Nam at vehicula purus. Curabitur id vulputate lorem. Nulla imperdiet dapibus sapien vitae eleifend. Vestibulum turpis tellus, consectetur ac ex vitae, sollicitudin maximus dolor. Pellentesque vitae",
            Importance.Low,
            isCompleted = false,
            creationDate = Date()
        ),
        TodoItem("15", "Text", Importance.Normal, isCompleted = false, creationDate = Date()),
        TodoItem("16", "Text", Importance.Low, isCompleted = false, creationDate = Date()),
        TodoItem("17", "Text", Importance.Normal, isCompleted = false, creationDate = Date()),
        TodoItem(
            "18",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit.  ullamcorper lorem.",
            Importance.Urgent,
            isCompleted = false,
            creationDate = Date()
        ),
        TodoItem(
            "19",
            "Последняя строка",
            Importance.Normal,
            isCompleted = false,
            creationDate = Date()
        )
    )

    private val _toDoListState: MutableStateFlow<Result<List<TodoItem>>> =
        MutableStateFlow(Result.Loading)

    suspend fun getAllItems(): StateFlow<Result<List<TodoItem>>> = withContext(Dispatchers.IO) {
        try {
            _toDoListState.update {
                Result.Success(_toDoList)
            }
            _toDoListState.asStateFlow()
        } catch (e: Exception) {
            _toDoListState.update {
                Result.Error(e)
            }
            _toDoListState.asStateFlow()
        }
    }

    fun getItemById(id: String): Flow<com.example.todoapp.core.Result<TodoItem>> = flow {
        try {
            val item = _toDoList.find { it.id == id }
            item?.let {
                emit(com.example.todoapp.core.Result.Success(item))
            } ?: throw Exception("Exception of getting item")
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun addItem(item: TodoItem) = withContext(Dispatchers.IO) {
        _toDoList.add(item)
        try {
            _toDoListState.update {
                Result.Success(_toDoList)
            }
        } catch (e: Exception) {
            _toDoListState.update {
                Result.Error(e)
            }
        }

    }

    suspend fun deleteItem(item: TodoItem) = withContext(Dispatchers.IO) {
        _toDoList.removeIf { it.id == item.id }
        try {
            Result.Success(_toDoList)
        } catch (e: Exception) {
            _toDoListState.update {
                Result.Error(e)
            }
        }
    }

    suspend fun updateItem(item: TodoItem) = withContext(Dispatchers.IO) {
        _toDoList.forEachIndexed { index, todoItem ->
            _toDoList[index] = if (todoItem.id == item.id) item else todoItem
        }
        try {
            Result.Success(_toDoList)
        } catch (e: Exception) {
            _toDoListState.update {
                Result.Error(e)
            }
        }
    }
}