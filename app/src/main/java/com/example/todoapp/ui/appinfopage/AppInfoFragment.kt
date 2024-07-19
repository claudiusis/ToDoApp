package com.example.todoapp.ui.appinfopage

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapp.ToDoApp
import com.example.todoapp.databinding.FragmentAppInfoBinding
import com.example.todoapp.di.AboutAppComponent
import com.example.todoapp.domain.AppTheme
import com.example.todoapp.navigation.Router
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.expression.variables.DivVariableController
import com.yandex.div.data.Variable
import com.yandex.div.picasso.PicassoDivImageLoader
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [AppInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppInfoFragment : Fragment() {

    private lateinit var binding: FragmentAppInfoBinding
    private lateinit var fragmentComponent : AboutAppComponent
    private lateinit var imageLoader: PicassoDivImageLoader
    private lateinit var assertReader : AssetsReader
    private lateinit var variableController : DivVariableController

    @Inject
    lateinit var router: Router

    override fun onAttach(context: Context) {
        super.onAttach(context)
        imageLoader = PicassoDivImageLoader(requireContext())
        assertReader = AssetsReader(requireContext())
        variableController = DivVariableController()
        fragmentComponent = (requireContext().applicationContext as ToDoApp)
            .appComponent
            .aboutInfoFeature()
            .create(findNavController())
        fragmentComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAppInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        router.setNavController(findNavController())

        val divJson = assertReader.read("infopage.json")
        val templatesJson = divJson.optJSONObject("templates")
        val cardJson = divJson.getJSONObject("card")

        viewLifecycleOwner.lifecycleScope.launch {
            (requireContext().applicationContext as ToDoApp).settings.themeStream.collect{
               val theme =  when(it){
                    AppTheme.ModeSystem -> {
                        if (isSystemInDarkTheme()){
                            Variable.StringVariable("app_theme", "dark")
                        } else {
                            Variable.StringVariable("app_theme", "light")
                        }
                    }
                    AppTheme.ModeDay -> {
                        Variable.StringVariable("app_theme", "light")
                    }
                    AppTheme.ModeNight -> {
                        Variable.StringVariable("app_theme", "dark")
                    }
               }
               variableController.putOrUpdate(theme)
            }
        }

        val divContext = Div2Context(
            baseContext = requireActivity(),
            configuration = createDivConfiguration(),
            lifecycleOwner = this
        )

        val divView = Div2ViewFactory(divContext, templatesJson).createView(cardJson)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        divView.layoutParams = layoutParams
        binding.root.addView(divView)

    }

    private fun createDivConfiguration() : DivConfiguration {
        return DivConfiguration.Builder(imageLoader)
            .divVariableController(variableController)
            .visualErrorsEnabled(true)
            .actionHandler(NavigationDivActionHandler(router))
            .build()
    }

    private fun isSystemInDarkTheme(): Boolean {
        return (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

}