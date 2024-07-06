package com.example.todoapp.data.network

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/*
* Creator for worker
*/
object WorkCreator {
    fun createWork(
        context: Context,
    ) {
        val workRequest = PeriodicWorkRequestBuilder<RequestWorker>(8, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "DataResend",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
}