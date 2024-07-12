package com.example.todoapp.ui.taskpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.todoapp.ToDoApp
import com.example.todoapp.core.ToDoItemViewModelFactory
import com.example.todoapp.di.CreationFeatureComponent
import com.example.todoapp.ui.core.TodoAppTheme
import com.example.todoapp.ui.taskpage.composable.TaskPage
import com.example.todoapp.ui.taskpage.viewModel.ToDoItemViewModel

class TaskPageFragment : Fragment() {

    private val creationComponent : CreationFeatureComponent by lazy {
        (requireActivity().application as ToDoApp)
            .appComponent
            .creationFeature()
            .create(findNavController())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        creationComponent.inject(this)
    }

    private val viewModel : ToDoItemViewModel by lazyViewModel {
        val savedStateHandle = SavedStateHandle()
        savedStateHandle["id"] = arguments?.getString("id")
        creationComponent.viewModelFactory().create(savedStateHandle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply { 
            setContent {
                TodoAppTheme {
                    TaskPage(
                        viewModel
                    )
                }
            }
        }
    }

    inline fun <reified T : ViewModel> Fragment.lazyViewModel(
        noinline create: (stateHandle: SavedStateHandle) -> T
    ) = viewModels<T> {
        ToDoItemViewModelFactory(this, create)
    }

}