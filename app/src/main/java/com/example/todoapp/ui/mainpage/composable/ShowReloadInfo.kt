package com.example.todoapp.ui.mainpage.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.todoapp.ui.core.Typography
import com.example.todoapp.ui.core.blue
import com.example.todoapp.ui.core.red
import com.example.todoapp.ui.core.white
import com.example.todoapp.ui.mainpage.TodoListEvent

@Composable
fun ReloadInfo(
    message : String,
    onEvent : (TodoListEvent) -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.red,
            style = Typography.bodyMedium,
        )
        TextButton(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.blue)
                .shadow(
                    elevation = 0.dp,
                    shape = RoundedCornerShape(12.dp)
                ),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                onEvent(TodoListEvent.Reload)
            }
        ) {
            Text(
                text = "Повторить",
                style = Typography.bodyLarge,
                color = MaterialTheme.colorScheme.white
            )
        }
    }
}