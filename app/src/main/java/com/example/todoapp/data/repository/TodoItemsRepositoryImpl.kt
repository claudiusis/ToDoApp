package com.example.todoapp.data.repository

import com.example.todoapp.core.Result
import com.example.todoapp.data.dto.Response
import com.example.todoapp.data.network.NetworkService
import com.example.todoapp.domain.Mapper.toDomain
import com.example.todoapp.domain.Mapper.toPostItem
import com.example.todoapp.domain.Repository
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

/*
* Repository class connect to network service to reformat data and receive it to viewModel and presentation layer
*/
class TodoItemsRepositoryImpl(
    private val service: NetworkService
) : Repository {

    private val _toDoListState = MutableStateFlow<Result<List<TodoItem>>?>(null)
    val toDoListState: StateFlow<Result<List<TodoItem>>?> = _toDoListState.asStateFlow()

    private var revision: Int = 0
    private var errorMessage = ""

    override suspend fun refresh() {
        errorMessage = "Ошибка получения списка"
        withContext(Dispatchers.IO) {
            try {
                val result : Response = service.getList()
                revision = result.revision
                val todoItems = result.list.map { it.toDomain() }
                _toDoListState.update {
                    Result.Success(todoItems)
                }
            } catch (e: ResponseException) {
                _toDoListState.update {
                    Result.Error(Exception("$errorMessage (${e.response.status})"))
                }
            } catch (e: Exception) {
                _toDoListState.update {
                    Result.Error(Exception(errorMessage))
                }
            }
        }
    }

    override suspend fun getItemById(id: String) : Result<TodoItem?> {
        return try {
            errorMessage = "Ошибка получения элемента списка"
            val result = service.getItem(id)
            val todoItem = result.element.toDomain()
            Result.Success(todoItem)
        } catch (e: ResponseException) {
            Result.Error(Exception("$errorMessage (${e.response.status})"))
        } catch (e: Exception) {
            Result.Error(Exception(errorMessage))
        }
    }

    override suspend fun addItem(item: TodoItem) : Result<Unit> {
        return try {
            errorMessage = "Ошибка добавления элемента списка"
            val revisionResponse : Response = service.getList()
            revision = revisionResponse.revision
            val postItem = item.toPostItem()
            val result = service.postItem(postItem, revision)
            val toDoItem = result.element.toDomain()
            _toDoListState.update {
                val list = (it as Result.Success).data + toDoItem
                Result.Success(list)
            }
            Result.Success(Unit)
        } catch (e: ResponseException) {
            Result.Error(Exception("$errorMessage (${e.response.status})"))
        } catch (e: Exception) {
            Result.Error(Exception(errorMessage))
        }
    }

    override suspend fun deleteItem(id: String) : Result<Unit> {
        return try {
            errorMessage = "Ошибка удаления элемента списка"
            val revisionResponse : Response = service.getList()
            revision = revisionResponse.revision
            service.deleteItem(id, revision)
            refresh()
            Result.Success(Unit)
        } catch (e: ResponseException) {
                Result.Error(Exception("$errorMessage (${e.response.status})"))
        } catch (e: Exception) {
                Result.Error(Exception(errorMessage))
        }
    }

    override suspend fun updateItem(item: TodoItem) : Result<Unit> {
        return try {
            errorMessage = "Ошибка обновления элемента списка"
            val revisionResponse : Response = service.getList()
            revision = revisionResponse.revision
            service.putItem(item.toPostItem(), revision)
            refresh()
            Result.Success(Unit)
        } catch (e: ResponseException) {
            _toDoListState.update {
                Result.Error(Exception("$errorMessage (${e.response.status})"))
            }
            Result.Error(Exception("$errorMessage (${e.response.status})"))
        } catch (e: Exception) {
            _toDoListState.update {
                Result.Error(Exception(errorMessage))
            }
            Result.Error(Exception(errorMessage))
        }
    }

}