package com.example.todoapp.domain

import com.example.todoapp.data.dto.PostItem
import com.example.todoapp.data.dto.PostList
import com.example.todoapp.data.dto.Response
import com.example.todoapp.data.dto.ResponseItem

interface NetworkService {
    suspend fun getList(): Response
    suspend fun getItem(id: String) : ResponseItem
    suspend fun postItem(postItem: PostItem, revision : Int) : PostItem
    suspend fun deleteItem(id : String, revision: Int) : PostItem
    suspend fun putItem(postItem: PostItem, revision: Int) : PostItem
    suspend fun patchList(postItem: PostList, revision: Int) : Response
}