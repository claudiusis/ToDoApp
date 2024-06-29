package com.example.todoapp.ui.taskpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHost
import com.example.todoapp.R
import com.example.todoapp.ToDoApp
import com.example.todoapp.core.ToDoItemViewModelFactory
import com.example.todoapp.ui.core.TodoAppTheme
import com.example.todoapp.ui.taskpage.composable.TaskPage
import com.example.todoapp.ui.taskpage.viewModel.ToDoItemViewModel

class TaskPageFragment : Fragment() {

    private lateinit var toDoPage : ToDoItemViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val app = requireActivity().application as ToDoApp
        val repository = app.repository

        toDoPage = ViewModelProvider(
            this,
            ToDoItemViewModelFactory(repository, this, arguments,
                requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHost)
        )[ToDoItemViewModel::class.java]

        return ComposeView(requireContext()).apply { 
            setContent {
                TodoAppTheme {
                    TaskPage(
                        toDoPage
                    )
                }
            }
        }
    }

}