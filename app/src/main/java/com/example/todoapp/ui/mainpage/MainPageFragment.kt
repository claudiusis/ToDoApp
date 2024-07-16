package com.example.todoapp.ui.mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.ToDoApp
import com.example.todoapp.core.ToDoViewModelFactory
import com.example.todoapp.di.ListFeatureComponent
import com.example.todoapp.ui.core.TodoAppTheme
import com.example.todoapp.ui.mainpage.composable.MainScreen
import com.example.todoapp.ui.mainpage.viewModel.TodoViewModel
import javax.inject.Inject

class MainPageFragment : Fragment() {

    private val listComponent : ListFeatureComponent by lazy {
        (requireActivity().application as ToDoApp)
            .appComponent
            .listFeature()
            .create(findNavController())
    }

    @Inject
    lateinit var viewModelFactory: ToDoViewModelFactory

    private val viewModel: TodoViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listComponent.inject(this)
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
}