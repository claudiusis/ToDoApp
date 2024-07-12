package com.example.todoapp.data.repository

import com.example.todoapp.core.AppScope
import com.example.todoapp.core.Result
import com.example.todoapp.data.db.ToDoDao
import com.example.todoapp.data.dto.Response
import com.example.todoapp.data.network.NetworkConnection
import com.example.todoapp.domain.Mapper.toDomain
import com.example.todoapp.domain.Mapper.toPostItem
import com.example.todoapp.domain.Mapper.toToDoItemEntity
import com.example.todoapp.domain.NetworkService
import com.example.todoapp.domain.Repository
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
* Repository class connect to network service to reformat data and receive it to viewModel and presentation layer
*/
@AppScope
class TodoItemsRepositoryImpl @Inject constructor(
    private val service: NetworkService,
    private val db: ToDoDao,
    private val networkConnection: NetworkConnection
) : Repository {

    private val _toDoListState = MutableStateFlow<Result<List<TodoItem>>?>(null)
    override val toDoListState: StateFlow<Result<List<TodoItem>>?> = _toDoListState.asStateFlow()

    private var revision: Int = 0
    private var errorMessage = ""

    init {
        CoroutineScope(Dispatchers.IO).launch {
            networkConnection.observeNetworkState().collect { networkConnection ->
                if (networkConnection){
                    refresh()
                }
            }
        }
    }


    override suspend fun refresh() {
        errorMessage = "Ошибка получения списка с сервера"
        withContext(Dispatchers.IO) {
            try {

                if (!networkConnection.isNetworkAvailable()) {
                    throw Exception("Нет интернета")
                }

                val result: Response = service.getList()
                revision = result.revision
                val todoItems = result.list.map { it.toDomain() }

                db.upsertItem(todoItems.map { it.toToDoItemEntity() })

                _toDoListState.update {
                    Result.Success(todoItems)
                }
            } catch (e: ResponseException) {

                val localList = db.getList()

                _toDoListState.update {
                    Result.Error(Exception("$errorMessage (${e.response.status})"))
                }

                _toDoListState.update {
                    Result.Success(localList.map { it.toDomain() })
                }

            } catch (e: Exception) {

                val localList = db.getList()

                _toDoListState.update {
                    Result.Error(Exception(errorMessage))
                }

                _toDoListState.update {
                    Result.Success(localList.map { it.toDomain() })
                }
            }
        }
    }

    override suspend fun getItemById(id: String): Result<TodoItem?> {
        return try {
            errorMessage = "Ошибка получения элемента списка с сервера"

            if (!networkConnection.isNetworkAvailable()) {
                throw Exception("Нет интернета")
            }

            val result = service.getItem(id)
            val todoItem = result.element.toDomain()
            Result.Success(todoItem)
        } catch (e: ResponseException) {

            val localElement = db.getItem(id).toDomain()

            Result.Error(Exception("$errorMessage (${e.response.status})"), localElement)
        } catch (e: Exception) {

            val localElement = db.getItem(id).toDomain()

            Result.Error(Exception(errorMessage), localElement)
        }
    }

    override suspend fun addItem(item: TodoItem): Result<Unit> {
        return try {
            errorMessage = "Ошибка добавления элемента списка с сервера"

            if (!networkConnection.isNetworkAvailable()) {
                throw Exception("Нет интернета")
            }

            val revisionResponse: Response = service.getList()
            revision = revisionResponse.revision
            val postItem = item.toPostItem()
            val result = service.postItem(postItem, revision)
            val toDoItem = result.element.toDomain()

            db.upsertItem(toDoItem.toToDoItemEntity())

            _toDoListState.update {
                val list = (it as Result.Success).data + toDoItem
                Result.Success(list)
            }
            Result.Success(Unit)
        } catch (e: ResponseException) {

            db.upsertItem(item.toToDoItemEntity())

            _toDoListState.update {
                val list = (it as Result.Success).data + item
                Result.Success(list)
            }

            Result.Error(Exception("$errorMessage (${e.response.status})"))
        } catch (e: Exception) {

            db.upsertItem(item.toToDoItemEntity())

            _toDoListState.update {
                val list = (it as Result.Success).data + item
                Result.Success(list)
            }

            Result.Error(Exception(errorMessage))
        }
    }

    override suspend fun deleteItem(id: String): Result<Unit> {
        return try {
            errorMessage = "Ошибка удаления элемента списка с сервера"

            if (!networkConnection.isNetworkAvailable()) {
                throw Exception("Нет интернета")
            }

            val revisionResponse: Response = service.getList()
            revision = revisionResponse.revision
            service.deleteItem(id, revision)
            db.deleteItem(id)
            Result.Success(Unit)
        } catch (e: ResponseException) {
            db.deleteItem(id)
            Result.Error(Exception("$errorMessage (${e.response.status})"))
        } catch (e: Exception) {
            db.deleteItem(id)
            Result.Error(Exception(errorMessage))
        } finally {
            refresh()
        }
    }

    override suspend fun updateItem(item: TodoItem): Result<Unit> {
        return try {
            errorMessage = "Ошибка обновления элемента списка на сервере"

            if (!networkConnection.isNetworkAvailable()) {
                throw Exception("Нет интернета")
            }

            val revisionResponse: Response = service.getList()
            revision = revisionResponse.revision
            service.putItem(item.toPostItem(), revision)
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
        } finally {
            db.upsertItem(item.toToDoItemEntity())
            refresh()
        }
    }
}