package com.example.todoapp.data.network

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
* Worker for background tasks
*/
class RequestWorker(
    context : Context,
    workParams : WorkerParameters
) : CoroutineWorker(context, workParams) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO){
            try {

                Result.success()
            } catch (e: Exception) {
                Result.retry()
            }
        }
    }
}