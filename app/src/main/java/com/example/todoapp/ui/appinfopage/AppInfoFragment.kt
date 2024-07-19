package com.example.todoapp.ui.appinfopage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.ToDoApp
import com.example.todoapp.databinding.FragmentAppInfoBinding
import com.example.todoapp.di.AboutAppComponent
import com.example.todoapp.navigation.Router
import com.example.todoapp.ui.MainActivity
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [AppInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppInfoFragment : Fragment() {

    private lateinit var binding: FragmentAppInfoBinding
    private lateinit var fragmentComponent : AboutAppComponent

    @Inject
    lateinit var router: Router

    override fun onAttach(context: Context) {
        super.onAttach(context)
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

        val divJson = (activity as MainActivity).assetReader.read("infopage.json")
        val templatesJson = divJson.optJSONObject("templates")
        val cardJson = divJson.getJSONObject("card")

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
        return DivConfiguration.Builder((activity as MainActivity).imageLoader!!)
            .visualErrorsEnabled(true)
            .actionHandler(NavigationDivActionHandler(router))
            .build()
    }

}