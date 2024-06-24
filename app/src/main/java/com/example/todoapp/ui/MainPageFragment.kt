package com.example.todoapp.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.core.Importance
import com.example.todoapp.core.ToDoViewModelFactory
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.databinding.FragmentMainPageBinding
import kotlinx.coroutines.launch

class MainPageFragment : Fragment() {

    private lateinit var binding : FragmentMainPageBinding

    private val toDoViewModel : TodoViewModel by activityViewModels {
        ToDoViewModelFactory(TodoItemsRepository())
    }

    private val toDoPage : ToDoItemViewModel by activityViewModels()

    private var scrollPosition = 20
    private var originalMargin = 20

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainPageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.setActionBar(binding.toolBar)

        recyclerCreation()
        observe()

        binding.FAB.setOnClickListener {
            findNavController().navigate(R.id.action_mainPageFragment_to_taskPageFragment)
        }

        binding.eyeImg.setOnClickListener {
            toDoViewModel.changeCompletedState()
        }

        binding.nestedScroll.setOnScrollChangeListener { _, _, scrollY, _, _ ->

            if (isScrollAtTop(binding.nestedScroll)) {
                updateImageViewMargin(binding.eyeImg, resources.getDimensionPixelSize(R.dimen.marginOriginal))
            } else {
                updateImageViewMargin(binding.eyeImg, originalMargin + resources.getDimensionPixelSize(R.dimen.margin))
            }

            scrollPosition = scrollY

        }
    }

    private fun isScrollAtTop(scrollView: NestedScrollView): Boolean {
        return scrollView.scrollY <= 25
    }

    private fun updateImageViewMargin(imageView: ImageView, bottom: Int) {
        val layoutParams = imageView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.bottomMargin = bottom
        imageView.layoutParams = layoutParams
    }


    private fun observe(){

        viewLifecycleOwner.lifecycleScope.launch {
            toDoViewModel.todoList.collect {
                (binding.mainRv.adapter as TodoListAdapter).submitList(it)
                val subtitle = "Выполнено - ${toDoViewModel.getCompletedCount()}"
                binding.completed.text = subtitle
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            toDoViewModel.stateCompleted.collect{
                when(it){
                    true -> {
                        binding.eyeImg.setImageResource(R.drawable.visibility_off)
                        toDoViewModel.getUnCompleted()
                    }
                    else -> {
                        binding.eyeImg.setImageResource(R.drawable.visibility)
                        toDoViewModel.getToDoList()
                    }
                }
            }
        }
    }

    private fun recyclerCreation(){
        binding.mainRv.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRv.adapter = TodoListAdapter({
            toDoPage.setToDoItem(it)
            findNavController().navigate(R.id.action_mainPageFragment_to_taskPageFragment)
        }, { item ->
            toDoViewModel.upDateNote(item.copy(isCompleted = !item.isCompleted))
        }, { isCompleted, importance, title, checkBox ->
            val spannableString = SpannableString(title.text)
            if (isCompleted){
                checkBox.setButtonDrawable(R.drawable.checked_checkbox)
                title.setTextColor(resources.getColor(R.color.tertiaryLabel, resources.newTheme()))
                spannableString.setSpan(StrikethroughSpan(), 0, title.text.length, Spanned.SPAN_INTERMEDIATE)
            } else {
                if (importance==Importance.URGENT) {
                    checkBox.setButtonDrawable(R.drawable.warning)
                } else {
                    checkBox.setButtonDrawable(R.drawable.simple_checkbox)
                }
                title.setTextColor(resources.getColor(R.color.primaryLabel))
            }
            title.text = spannableString
        }, {
            findNavController().navigate(R.id.action_mainPageFragment_to_taskPageFragment)
        })
    }

}