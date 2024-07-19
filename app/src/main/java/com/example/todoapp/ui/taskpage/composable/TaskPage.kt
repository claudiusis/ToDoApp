package com.example.todoapp.ui.taskpage.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todoapp.R
import com.example.todoapp.core.Importance
import com.example.todoapp.domain.Mapper
import com.example.ui_core.Typography
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
import com.example.todoapp.ui.mainpage.composable.ShowProgressBar
import com.example.todoapp.ui.mainpage.composable.ShowSnackBar
import com.example.todoapp.ui.taskpage.TaskEvent
import com.example.todoapp.ui.taskpage.viewModel.ToDoItemViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskPage(
    viewModel: ToDoItemViewModel
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

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
                        if (uiState is UiState.Success
                            || (uiState is UiState.Error && viewModel._toDoItem != null)
                        ) {
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
                        }
                    },

                    navigationIcon = {
                        IconButton(onClick = {
                            viewModel.onEvent(TaskEvent.OnBackClicked)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.close_btn),
                                contentDescription = "close",
                            )
                        }
                    },

                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                )
            }
        },

        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->

        when (uiState) {
            is UiState.Loading -> {
                ShowProgressBar()
            }

            is UiState.Success -> {
                SuccessScreen(
                    viewModel = viewModel,
                    paddingValues,
                    scrollState,
                )
            }

            is UiState.Dialog -> {
                DatePickerFun(viewModel::onEvent)
            }

            is UiState.Error -> {

                SuccessScreen(
                    viewModel = viewModel,
                    paddingValues,
                    scrollState,
                )

                ShowSnackBar(
                    message = uiState.error,
                    scaffoldState = snackbarHostState,
                    scope = scope
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreen(
    viewModel: ToDoItemViewModel,
    paddingValues: PaddingValues,
    scrollState: ScrollState,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    var highlight by remember { mutableStateOf(false) }
    var isHighImportance by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        targetValue = if (highlight) MaterialTheme.colorScheme.red.copy(alpha = 0.5f) else Color.Transparent,
        animationSpec = tween(durationMillis = 200)
    )

    val items = listOf(
        stringResource(id = R.string.no),
        stringResource(id = R.string.low),
        stringResource(id = R.string.high)
    )

    LaunchedEffect(key1 = highlight) {
        if (highlight) {
            delay(200)
            highlight = false
        }
    }

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
        TextButton(
            onClick = {
                showBottomSheet = true
            },
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 8.dp
                )
                .wrapContentSize()
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(16.dp)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.labelPrimary
            )
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.importance),
                    style = Typography.bodyMedium
                )
                Text(
                    text = viewModel.importance.value,
                    color = if (viewModel.importance == Importance.Urgent) MaterialTheme.colorScheme.red else MaterialTheme.colorScheme.tertiaryLabel,
                    style = Typography.titleSmall,
                )
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    showBottomSheet = false
                }
            ) {
                LazyColumn {
                    itemsIndexed(items) { index, item ->

                        Text(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(20.dp)
                                .clickable {
                                    viewModel.onEvent(
                                        TaskEvent.OnImportanceChange(
                                            Importance.fromUiString(item)
                                        )
                                    )
                                    if (item == "!! Высокий") {
                                        highlight = true
                                    }
                                    scope
                                        .launch { sheetState.hide() }
                                        .invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                            }
                                        }
                                },
                            text = item,
                            style = Typography.bodyMedium,
                            color = MaterialTheme.colorScheme.labelPrimary,
                        )

                        if (index < items.lastIndex) {
                            Divider(
                                color = MaterialTheme.colorScheme.separator,
                                thickness = 1.dp

                            )
                        }

                    }
                }
            }
        }
        Divider(
            modifier = Modifier
                .padding(16.dp),
            color = MaterialTheme.colorScheme.separator
        )
        DeadLineRow(viewModel = viewModel)
        Divider(
            color = MaterialTheme.colorScheme.separator
        )
        DeleteButton(viewModel = viewModel)
    }
}


@Composable
fun DeadLineRow(viewModel: ToDoItemViewModel) {
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
                    text = Mapper.changeDateFormat(viewModel.deadline!!),
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
}


@Composable
fun DeleteButton(viewModel: ToDoItemViewModel) {
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
            painter = painterResource(id = R.drawable.delete_icon),
            contentDescription = "Garbage",
            modifier = Modifier.padding(start = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.delete)
        )
    }
}