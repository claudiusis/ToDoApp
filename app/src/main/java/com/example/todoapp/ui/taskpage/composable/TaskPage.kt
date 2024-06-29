package com.example.todoapp.ui.taskpage.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.core.ChangeType
import com.example.todoapp.core.Importance
import com.example.todoapp.ui.core.TodoAppTheme
import com.example.todoapp.ui.core.Typography
import com.example.todoapp.ui.core.backSecondary
import com.example.todoapp.ui.core.blue
import com.example.todoapp.ui.core.blueLight
import com.example.todoapp.ui.core.disable
import com.example.todoapp.ui.core.labelPrimary
import com.example.todoapp.ui.core.overlay
import com.example.todoapp.ui.core.red
import com.example.todoapp.ui.core.separator
import com.example.todoapp.ui.core.tertiaryLabel
import com.example.todoapp.ui.core.white
import com.example.todoapp.ui.mainpage.UiState
import com.example.todoapp.ui.taskpage.TaskEvent
import com.example.todoapp.ui.taskpage.viewModel.ToDoItemViewModel
import java.util.Calendar


@Composable
fun TaskPage(
    viewModel: ToDoItemViewModel
) {

    val uiState = viewModel.uiState.collectAsState()

    when (uiState.value) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = 0.75f,
                    color = MaterialTheme.colorScheme.blue,
                    strokeWidth = 8.dp
                )
            }
        }

        is UiState.Success -> {
            SuccessScreen(viewModel = viewModel)
        }

        is UiState.Dialog -> {
            DatePickerFun(viewModel::onEvent)
        }

        is UiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (uiState.value as UiState.Error).error,
                    style = Typography.titleLarge,
                    color = MaterialTheme.colorScheme.red
                )
            }
        }

        is UiState.DropDownMenu -> {}
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreen(viewModel: ToDoItemViewModel) {

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = if (scrollState.value > 0) 6.dp else 0.dp
                    )
            ) {
                TopAppBar(

                    title = {
                        Row {
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.blue,
                                containerColor = MaterialTheme.colorScheme.background,
                            ), onClick = {
                                viewModel.onEvent(TaskEvent.OnSaveClickedChange)
                            }) {
                                Text(
                                    text = stringResource(id = R.string.save),
                                    style = Typography.bodyMedium
                                )
                            }
                        }
                    },

                    navigationIcon = {
                        IconButton(onClick = {
                            viewModel.onEvent(TaskEvent.OnBackClicked)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.close),
                                contentDescription = "close",
                            )
                        }
                    },

                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),

                    actions = {

                    },
                )

            }
        },

        ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            TextField(
                value = viewModel.text,
                onValueChange = {
                    viewModel.onEvent(TaskEvent.OnTextChange(it))
                },
                textStyle = TextStyle(
                    textAlign = TextAlign.Start,
                ),
                minLines = 5,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.edit_hint),
                        color = MaterialTheme.colorScheme.tertiaryLabel
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.backSecondary
                    )
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(12.dp)
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.backSecondary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.backSecondary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.backSecondary,
                ),
                shape = RoundedCornerShape(12.dp),
            )

            Text(
                text = stringResource(id = R.string.importance),
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp),
                style = Typography.bodyMedium
            )
            Dropdown(
                importance = viewModel._importance,
                viewModel::onEvent
            )

            Divider(
                modifier = Modifier
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.separator
            )

            Row(
                modifier = Modifier
                    .padding(
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                    )
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = stringResource(id = R.string.dedline_title),
                        style = Typography.bodyMedium
                    )

                    viewModel.deadline?.let {
                        Text(
                            text = ChangeType.changeDateFormat(viewModel.deadline!!),
                            style = Typography.titleSmall,
                            color = MaterialTheme.colorScheme.blue,
                            modifier = Modifier
                                .clickable {
                                    viewModel.onEvent(TaskEvent.OnTextDeadlineClicked)
                                }
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = viewModel.switchState,
                    onCheckedChange = {
                        viewModel.onEvent(TaskEvent.OnSwitchChange)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.blue,
                        checkedTrackColor = MaterialTheme.colorScheme.blueLight,
                        uncheckedThumbColor = MaterialTheme.colorScheme.white,
                        uncheckedTrackColor = MaterialTheme.colorScheme.overlay,
                    ),
                )

            }

            Divider(
                color = MaterialTheme.colorScheme.separator
            )

            TextButton(
                onClick = {
                    viewModel.onEvent(TaskEvent.OnDeleteClickedChange)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = if (viewModel.deleteState) MaterialTheme.colorScheme.red else MaterialTheme.colorScheme.disable
                ),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "Garbage",
                    modifier = Modifier.padding(start = 8.dp)
                )
                Text(
                    text = stringResource(id = R.string.delete)
                )
            }


        }

    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
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

@Preview
@Composable
private fun LightScreenPreview() {
    TodoAppTheme {
        TaskPage(viewModel = ToDoItemViewModel())
    }
}

@Preview
@Composable
private fun DarkScreenPreview() {
    TodoAppTheme(
        darkTheme = true
    ) {
        TaskPage(viewModel = ToDoItemViewModel())
    }
}
