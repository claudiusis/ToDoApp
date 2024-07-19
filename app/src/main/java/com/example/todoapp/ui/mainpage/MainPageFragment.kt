package com.example.todoapp.ui.mainpage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.ToDoApp
import com.example.todoapp.core.ToDoViewModelFactory
import com.example.todoapp.di.ListFeatureComponent
import com.example.todoapp.domain.AppTheme
import com.example.todoapp.ui.MainActivity
import com.example.todoapp.ui.core.TodoAppTheme
import com.example.todoapp.ui.mainpage.composable.MainScreen
import com.example.todoapp.ui.mainpage.viewModel.TodoViewModel
import javax.inject.Inject

class MainPageFragment : Fragment() {

    private lateinit var listComponent : ListFeatureComponent

    @Inject
    lateinit var viewModelFactory: ToDoViewModelFactory

    private val viewModel: TodoViewModel by viewModels {
        viewModelFactory
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        Log.d("QWERTY", findNavController().toString())
        listComponent = (requireContext().applicationContext as ToDoApp)
            .appComponent
            .listFeature()
            .create(findNavController())
        listComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(activity as MainActivity).apply {
            setContent {
                val theme = (requireContext().applicationContext as ToDoApp).settings.themeStream.collectAsState()
                val themeStyle = when (theme.value) {
                    AppTheme.ModeSystem -> isSystemInDarkTheme()
                    AppTheme.ModeDay -> false
                    AppTheme.ModeNight -> true
                }
                TodoAppTheme(themeStyle) {
                    MainScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setNavController(findNavController())
    }
}