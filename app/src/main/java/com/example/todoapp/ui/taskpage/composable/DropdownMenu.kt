package com.example.todoapp.ui.taskpage.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.core.Importance
import com.example.todoapp.ui.core.Typography
import com.example.todoapp.ui.core.backSecondary
import com.example.todoapp.ui.core.labelPrimary
import com.example.todoapp.ui.core.red
import com.example.todoapp.ui.core.tertiaryLabel
import com.example.todoapp.ui.taskpage.TaskEvent

@Composable
fun Dropdown(importance: Importance, onEvent: (TaskEvent) -> Unit) {
    var expanded: Boolean by remember { mutableStateOf(false) }
    Box {
        TextButton(
            onClick = { expanded = true }) {
            Text(
                text = importance.value,
                color = if (importance == Importance.Urgent) MaterialTheme.colorScheme.red else MaterialTheme.colorScheme.tertiaryLabel,
                style = Typography.titleSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.backSecondary)
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Нет",
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.labelPrimary,
                    )
                },
                onClick = {
                    onEvent(TaskEvent.OnImportanceChange(Importance.Normal))
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Низкий",
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.labelPrimary,
                    )
                },
                onClick = {
                    onEvent(TaskEvent.OnImportanceChange(Importance.Low))
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "!! Высокий",
                        color = MaterialTheme.colorScheme.red,
                        style = Typography.bodyMedium,
                    )
                },
                onClick = {
                    onEvent(TaskEvent.OnImportanceChange(Importance.Urgent))
                    expanded = false
                }
            )
        }
    }
}