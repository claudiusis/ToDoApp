package com.example.todoapp.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.core.ChangeType
import com.example.todoapp.core.Importance
import com.example.todoapp.databinding.FragmentTaskPageBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class TaskPageFragment : Fragment() {

    private lateinit var binding : FragmentTaskPageBinding
    private val todoItemsViewModel: TodoViewModel by activityViewModels()
    private val toDoPage : ToDoItemViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskPageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUI()

        observe()

    }

    override fun onDestroy() {
        super.onDestroy()
        toDoPage.setToDoItem(null)
        toDoPage.setDeadLine(null)
    }

    private fun setUpUI(){

        binding.switchPicker.isUseMaterialThemeColors = false
        binding.switchPicker.thumbTintList = ContextCompat.getColorStateList(requireContext(),
            R.color.thumb_selector
        )
        binding.switchPicker.trackTintList = ContextCompat.getColorStateList(requireContext(),
            R.color.track_selector
        )

        binding.cross.setOnClickListener {
            findNavController().popBackStack()
        }

        val importance = listOf(binding.importanceText, binding.importanceTitle)
        importance.forEach { item ->
            item.setOnClickListener {
                showImportance(it)
            }
        }

        binding.switchPicker.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked){
                if (toDoPage.todoItem.value?.deadLine==null) {
                    showDialog()
                }
                val list = listOf(binding.deadlineTitle, binding.deadlineText)
                list.forEach {
                    it.setOnClickListener {
                        showDialog()
                    }
                }
            } else {
                toDoPage.setDeadLine(null)
            }
        }

        binding.save.setOnClickListener {
            if (binding.todoText.text.isNotEmpty()) {
                if (toDoPage.todoItem.value == null) {
                    todoItemsViewModel.addNote(
                        toDoPage.createToDoItem(todoItemsViewModel.todoList.value.size.toString(), binding.todoText.text.toString())
                    )
                } else {
                    todoItemsViewModel.upDateNote(
                        toDoPage.createToDoItem(text = binding.todoText.text.toString())
                    )
                }
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Вы должны ввести текст!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun observe() {
        toDoPage.todoItem.observe(viewLifecycleOwner) {
            if (it!=null) {
                binding.todoText.setText(it.text)

                binding.deleteText.setTextColor(resources.getColor(R.color.red))
                binding.deleteImg.setColorFilter(resources.getColor(R.color.red))
                binding.deleteBlock.setOnClickListener {
                    todoItemsViewModel.deleteNote(toDoPage.todoItem.value?.id ?: "-1")
                    findNavController().popBackStack()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            toDoPage.deadLine.collect {
                if (it!=null) {
                    binding.switchPicker.isChecked = true
                    binding.deadlineText.visibility = View.VISIBLE
                    binding.deadlineText.text = ChangeType.changeDateFormat(it)
                } else {
                    binding.deadlineText.visibility = View.GONE
                }
            }
        }




        viewLifecycleOwner.lifecycleScope.launch {
            toDoPage.importance .collect {
                binding.importanceText.text = ChangeType.importanceToString(it)
            }
        }
    }


    private fun showImportance(view: View){
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.importance)
        popup.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.low -> {
                    toDoPage.setImportance(Importance.LOW)
                    binding.importanceText.setTextColor(resources.getColor(R.color.tertiaryLabel))
                }
                R.id.no -> {
                    toDoPage.setImportance(Importance.NORMAL)
                    binding.importanceText.setTextColor(resources.getColor(R.color.tertiaryLabel))
                }
                R.id.high -> {
                    toDoPage.setImportance(Importance.URGENT)
                    binding.importanceText.setTextColor(resources.getColor(R.color.red))
                }
            }
            false
        }
        popup.show()
    }

    private fun showDialog(){
        val datePicker = DatePickerDialog(requireContext(), R.style.DialogTheme)
        datePicker.show()
        datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).text = "ГОТОВО"
        datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.blue))
        datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.blue))
        datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.set(datePicker.datePicker.year, datePicker.datePicker.month, datePicker.datePicker.dayOfMonth)
            toDoPage.setDeadLine(calendar.time)
            datePicker.dismiss()
        }
        datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setOnClickListener {
            if (toDoPage.deadLine.value==null){
                binding.switchPicker.isChecked = false
            }
            datePicker.dismiss()
        }

    }

}