package com.example.todoapp.data.network

import com.example.todoapp.data.dto.PostItem
import com.example.todoapp.data.dto.Response
import com.example.todoapp.data.dto.ResponseItem
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType

/*
* Service for connection to network
*/

class NetworkService(private val client : KtorHttpClient) {

    suspend fun getList(): Response {
        val result = client.client.get { url(HttpRoutes.LIST) }
        return result.body<Response>()
    }

    suspend fun getItem(id: String) : ResponseItem {
        val result = client.client.get {
            url("${HttpRoutes.LIST}/$id")
        }
        return result.body<ResponseItem>()
    }

    suspend fun postItem(postItem: PostItem, revision : Int) : PostItem {
        val result = client.client.post(HttpRoutes.LIST) {
            header(HttpRoutes.Header, revision)
            contentType(ContentType.Application.Json)
            setBody(postItem)
        }
        return result.body()
    }

    suspend fun deleteItem(id : String, revision: Int) : PostItem {
        val response = client.client.delete(HttpRoutes.LIST){
            url {
                appendPathSegments(id)
            }
            header(HttpRoutes.Header, revision)
        }
        return response.body()
    }

    suspend fun putItem(postItem: PostItem, revision: Int) : PostItem {
        val response = client.client.put(HttpRoutes.LIST){
            header(HttpRoutes.Header, revision)
            url{
                appendPathSegments(postItem.element.id)
            }
            contentType(ContentType.Application.Json)
            setBody(postItem)
        }
        return response.body()
    }
}