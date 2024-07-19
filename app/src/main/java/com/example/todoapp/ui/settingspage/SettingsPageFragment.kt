package com.example.todoapp.ui.settingspage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.ToDoApp
import com.example.todoapp.di.SettingsComponent
import com.example.todoapp.domain.AppTheme
import com.example.todoapp.navigation.Router
import com.example.todoapp.ui.MainActivity
import com.example.todoapp.ui.core.TodoAppTheme
import javax.inject.Inject

class SettingsPageFragment : Fragment() {

    private val fragmentComponent: SettingsComponent by lazy {
        (requireContext().applicationContext as ToDoApp)
            .appComponent
            .settingsFeature()
            .create(findNavController())
    }

    @Inject
    lateinit var router: Router

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val theme = (requireContext().applicationContext as ToDoApp).settings.themeStream.collectAsState()
                val themeStyle = when (theme.value) {
                    AppTheme.ModeSystem -> isSystemInDarkTheme()
                    AppTheme.ModeDay -> false
                    AppTheme.ModeNight -> true
                }
                TodoAppTheme(themeStyle) {
                    SettingPage(
                        router,
                        (requireContext().applicationContext as ToDoApp).settings
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        router.setNavController(findNavController())
    }

}