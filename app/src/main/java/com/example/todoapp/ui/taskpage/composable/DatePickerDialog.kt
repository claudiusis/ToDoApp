package com.example.todoapp.ui.taskpage.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.todoapp.R
import com.example.todoapp.ui.taskpage.TaskEvent
import java.util.Calendar

@Composable
fun DatePickerFun(onEvent: (TaskEvent) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        R.style.DatePickerTheme,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            onEvent(TaskEvent.OnDeadLineChange(calendar.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        setOnDismissListener {
            onEvent(TaskEvent.OnCancelClicked)
        }
    }
    LaunchedEffect(Unit) {
        datePickerDialog.show()
    }
}