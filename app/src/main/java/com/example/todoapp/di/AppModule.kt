package com.example.todoapp.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.todoapp.core.AppScope
import com.example.todoapp.data.db.ToDoDao
import com.example.todoapp.data.db.ToDoDatabase
import com.example.todoapp.data.network.NetworkConnection
import com.example.todoapp.data.network.NetworkServiceImpl
import com.example.todoapp.data.repository.TodoItemsRepositoryImpl
import com.example.todoapp.domain.NetworkService
import com.example.todoapp.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@Module
interface AppModule {

    @Binds
    @AppScope
    fun bindRepository(repositoryImpl: TodoItemsRepositoryImpl): Repository

    @Binds
    @AppScope
    fun bindNetwork(networkService: NetworkServiceImpl): NetworkService

    companion object {

        @Provides
        @AppScope
        fun provideNetworkConnection(context: Context) : NetworkConnection {
            return NetworkConnection(context)
        }

        @Provides
        @AppScope
        fun provideHttpClient(): HttpClient = HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens("Rian", "Rian")
                    }
                }
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(1)
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("KTOR_REQUEST", message)
                    }
                }
                level = LogLevel.ALL
            }
        }

        @AppScope
        @Provides
        fun provideDataBase(context: Context): ToDoDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ToDoDatabase::class.java,
                "todo.db"
            )
                .build()
        }

        @Provides
        fun provideToDoDao(db: ToDoDatabase): ToDoDao {
            return db.toDoDao()
        }
    }
}