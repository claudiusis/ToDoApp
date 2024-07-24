package com.example.todoapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.todoapp.R
import com.example.todoapp.ToDoApp
import com.example.todoapp.data.network.WorkCreator
import com.example.todoapp.domain.UserSettings
import com.example.todoapp.ui.appinfopage.AssetsReader
import com.example.todoapp.ui.mainpage.viewModel.NavControllerViewModel
import com.yandex.div.glide.GlideDivImageLoader
import com.yandex.div.picasso.PicassoDivImageLoader
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WorkCreator.createWork(this)
        setContentView(R.layout.activity_main)
    }

}