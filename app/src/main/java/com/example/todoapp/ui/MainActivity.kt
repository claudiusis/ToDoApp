package com.example.todoapp.ui

import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.R
import com.example.todoapp.data.network.WorkCreator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WorkCreator.createWork(this)
        setContentView(R.layout.activity_main)

    }
}