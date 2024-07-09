package com.example.todoapp.data.network


import android.util.Log
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

/*
* HttpClient for connection to network
*/

object KtorHttpClient {
    val client = HttpClient(Android){
        install(ContentNegotiation){
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }

        install(Auth){
            bearer {
                loadTokens {
                    BearerTokens("Rian", "Rian")
                }
            }
        }

        install(HttpRequestRetry){
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
}