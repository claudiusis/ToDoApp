
import com.example.todoapp.core.Importance
import com.example.todoapp.core.Result
import com.example.todoapp.data.db.ToDoDao
import com.example.todoapp.data.network.NetworkConnection
import com.example.todoapp.data.network.NetworkServiceImpl
import com.example.todoapp.data.repository.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepositoryImpl
import com.example.todoapp.domain.Mapper.toToDoItemEntity
import com.example.todoapp.domain.NetworkService
import com.example.todoapp.domain.Repository
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import java.util.Date

class TodoItemsRepositoryImplTest {

    private lateinit var repository: Repository
    private lateinit var service: NetworkService
    private val db: ToDoDao = mockk()
    private val networkConnection: NetworkConnection = mockk()


    @Before
    fun setup() {
        val mockEngine = MockEngine { request ->
            when (request.url.fullPath) {
                "/list" -> respond(
                    content = """
                {"revision":1, 
                "element": {
            "importance": "basic",
            "created_at": 1721428956678,
            "done": false,
            "changed_at": 1721893905045,
            "id": "3d76e631-1594-4607-9c31-c4747f909a8d",
            "text": "новая заметка"
                }""",
                    status = HttpStatusCode.OK,
                    headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                )
                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }

        service = NetworkServiceImpl(client)
        repository = TodoItemsRepositoryImpl(service, db, networkConnection)
    }


    @Test
    fun `addItem should add item to local database when network is unavailable`() = runBlocking {

        val todoItem = TodoItem(
            id = "1",
            text = "Test item",
            importance = Importance.Normal,
            isCompleted = false,
            creationDate = Date()
        )
        coEvery { networkConnection.isNetworkAvailable() } returns false
        coJustRun { db.upsertItem(todoItem.toToDoItemEntity()) }

        val result = repository.addItem(todoItem)

        coVerify { db.upsertItem(todoItem.toToDoItemEntity()) }
        assertEquals(Result.Error(Exception("Ошибка добавления элемента списка с сервера")), result)
    }

    @Test
    fun `addItem should add item to server and local database when network is available`() = runBlocking {

        val todoItem = TodoItem(
            id = "1",
            text = "Test item",
            importance = Importance.Normal,
            isCompleted = false,
            creationDate = Date()
        )
        coEvery { networkConnection.isNetworkAvailable() } returns true
        coJustRun { db.upsertItem(todoItem.toToDoItemEntity()) }

        val result = repository.addItem(todoItem)

        coVerify { db.upsertItem(todoItem.toToDoItemEntity()) }
        assertEquals(Result.Success(Unit), result)
    }
}
