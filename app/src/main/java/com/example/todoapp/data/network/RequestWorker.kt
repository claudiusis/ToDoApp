package com.example.todoapp.data.network

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapp.domain.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
* Worker for background tasks
*/
class RequestWorker(
    context : Context,
    workParams : WorkerParameters
) : CoroutineWorker(context, workParams) {

    @Inject
    lateinit var repository: Repository

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO){
            try {
                repository.synchronizeServer()
                Result.success()
            } catch (e: Exception) {
                Result.retry()
            }
        }
    }
}