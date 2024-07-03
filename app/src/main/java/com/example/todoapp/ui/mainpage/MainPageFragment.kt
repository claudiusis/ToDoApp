package com.example.todoapp.ui.mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavHost
import com.example.todoapp.R
import com.example.todoapp.ToDoApp
import com.example.todoapp.core.ToDoViewModelFactory
import com.example.todoapp.ui.core.TodoAppTheme
import com.example.todoapp.ui.mainpage.composable.MainScreen
import com.example.todoapp.ui.mainpage.viewModel.TodoViewModel

class MainPageFragment : Fragment() {

    private val viewModel: TodoViewModel by viewModels {
        ToDoViewModelFactory((requireActivity().application as ToDoApp).repository, requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHost)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return ComposeView(requireContext()).apply {
            setContent {
                TodoAppTheme {
                    MainScreen(
                        viewModel
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHost = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHost
        viewModel.setNavHost(navHost)
        viewModel.getToDoList()
    }

}