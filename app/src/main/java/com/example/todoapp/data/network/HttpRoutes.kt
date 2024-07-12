package com.example.todoapp.data.network

/**
* Links and URL
*/
object HttpRoutes {
    private const val BASE_URL = "https://hive.mrdekk.ru/todo"
    val LIST = "$BASE_URL/list"
    val Header = "X-Last-Known-Revision"
}