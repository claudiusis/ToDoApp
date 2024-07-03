package com.example.todoapp.ui.mainpage.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.core.ChangeType
import com.example.todoapp.core.Importance
import com.example.todoapp.data.repository.TodoItem
import com.example.todoapp.ui.core.TodoAppTheme
import com.example.todoapp.ui.core.Typography
import com.example.todoapp.ui.core.backPrimary
import com.example.todoapp.ui.core.backSecondary
import com.example.todoapp.ui.core.blue
import com.example.todoapp.ui.core.gray
import com.example.todoapp.ui.core.green
import com.example.todoapp.ui.core.labelPrimary
import com.example.todoapp.ui.core.red
import com.example.todoapp.ui.core.tertiaryLabel
import com.example.todoapp.ui.core.white
import com.example.todoapp.ui.mainpage.TodoListEvent
import com.example.todoapp.ui.mainpage.UiState
import com.example.todoapp.ui.mainpage.viewModel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: TodoViewModel
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val todoList = viewModel.todoList.collectAsState().value
    val eyeVisible = viewModel.eyeVisible.collectAsState().value
    val counter = viewModel.countOfCompleted.collectAsState().value
    val uiState = viewModel.uiState.collectAsState().value

    TodoAppTheme {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .background(MaterialTheme.colorScheme.backPrimary),
            topBar = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = if (scrollBehavior.state.collapsedFraction > 0.5) 6.dp else 0.dp
                        )
                        .background(MaterialTheme.colorScheme.backPrimary)
                ) {
                    LargeTopAppBar(
                        title = {
                            Column {
                                if (scrollBehavior.state.collapsedFraction < 0.5) {
                                    Text(
                                        text = "Мои дела",
                                        style = Typography.titleLarge,
                                        modifier = Modifier.padding(start = 26.dp)
                                    )

                                    Row {
                                        Text(
                                            text = "Выполнено - $counter",
                                            style = Typography.bodyMedium,
                                            modifier = Modifier.padding(start = 26.dp, top = 4.dp),
                                            color = MaterialTheme.colorScheme.tertiaryLabel
                                        )

                                        Spacer(modifier = Modifier.weight(1f))

                                        Icon(
                                            painter = if (eyeVisible) painterResource(id = R.drawable.visibility) else
                                                painterResource(id = R.drawable.visibility_off),
                                            contentDescription = "Visibility eye",
                                            modifier = Modifier
                                                .padding(top = 12.dp, end = 24.dp)
                                                .clickable {
                                                    viewModel.onEvent(TodoListEvent.OnEyeChange)
                                                },
                                            tint = MaterialTheme.colorScheme.blue,
                                        )
                                    }
                                } else {
                                    Text(
                                        text = "Мои дела",
                                        style = Typography.titleMedium,
                                        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                                    )
                                }
                            }
                        },

                        actions = {
                            if (scrollBehavior.state.collapsedFraction >= 0.5) {
                                Icon(
                                    painter = if (eyeVisible) painterResource(id = R.drawable.visibility) else
                                        painterResource(id = R.drawable.visibility_off),
                                    contentDescription = "Visibility eye",
                                    modifier = Modifier
                                        .padding(top = 12.dp, end = 24.dp)
                                        .clickable {
                                            viewModel.onEvent(TodoListEvent.OnEyeChange)
                                        },
                                    tint = MaterialTheme.colorScheme.blue,
                                )
                            }
                        },

                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.backPrimary,
                            titleContentColor = MaterialTheme.colorScheme.labelPrimary,
                            actionIconContentColor = MaterialTheme.colorScheme.blue
                        ),

                        modifier = Modifier.background(MaterialTheme.colorScheme.backPrimary),

                        scrollBehavior = scrollBehavior
                    )
                }
            },

            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(bottom = 16.dp, end = 8.dp)
                        .size(56.dp),
                    containerColor = MaterialTheme.colorScheme.blue,
                    contentColor = MaterialTheme.colorScheme.white,
                    onClick = {
                        viewModel.onEvent(TodoListEvent.OnCreateNewPage)
                    },
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Add, contentDescription = "Add"
                    )
                }
            }
        ) { innerPadding ->

            when (uiState) {
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
                is UiState.Dialog, UiState.DropDownMenu -> {}
                is UiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.error,
                            style = Typography.titleLarge,
                            color = MaterialTheme.colorScheme.red
                        )
                    }
                }

                is UiState.Success -> {
                    LazyColumn(
                        contentPadding = innerPadding,
                        modifier = Modifier
                            .padding(
                                start = 8.dp,
                                top = 16.dp,
                                end = 8.dp,
                            )
                            .fillMaxWidth()
                    ) {
                        item {
                            Row(
                                modifier = Modifier
                                    .padding(top=16.dp)
                                    .background(
                                        MaterialTheme.colorScheme.backSecondary,
                                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                                    )
                                    .fillMaxWidth()
                                    .height(8.dp)
                            ){}
                        }

                        items(todoList) { item ->
                            TodoColumnItem(item, viewModel::onEvent)
                        }
                        item {
                            Row(
                                modifier = Modifier
                                    .padding(bottom=16.dp)
                                    .background(
                                        MaterialTheme.colorScheme.backSecondary,
                                        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),

                                        )
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.onEvent(TodoListEvent.OnCreateNewPage)
                                    }
                            ) {
                                Text(
                                    text = "Новое",
                                    color = MaterialTheme.colorScheme.tertiaryLabel,
                                    modifier = Modifier
                                        .padding(
                                            start = 52.dp,
                                            top = 16.dp,
                                            bottom = 16.dp
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodoColumnItem(item: TodoItem, onEvent: (TodoListEvent) -> Unit) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.backSecondary),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = item.isCompleted,
            onCheckedChange = {
                onEvent(TodoListEvent.ToggleCompleted(item))
            },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.green,
                uncheckedColor =
                if (item.importance == Importance.Urgent) MaterialTheme.colorScheme.red
                else MaterialTheme.colorScheme.tertiaryLabel,
                checkmarkColor = MaterialTheme.colorScheme.background,
                disabledCheckedColor = MaterialTheme.colorScheme.tertiaryLabel,
                disabledUncheckedColor = if (item.importance == Importance.Urgent) MaterialTheme.colorScheme.red
                else MaterialTheme.colorScheme.tertiaryLabel,
            ),


            )

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(vertical = 12.dp),
        ) {
            when (item.importance) {
                is Importance.Urgent -> {
                    Icon(
                        modifier = Modifier.padding(top = 2.dp, end = 4.dp),
                        painter = painterResource(id = R.drawable.importance),
                        contentDescription = "High importance",
                        tint = MaterialTheme.colorScheme.red,
                    )
                }

                is Importance.Low -> {
                    Icon(
                        modifier = Modifier.padding(top = 2.dp, end = 4.dp),
                        painter = painterResource(id = R.drawable.arrow_down),
                        contentDescription = "Low importance",
                        tint = MaterialTheme.colorScheme.gray
                    )
                }

                is Importance.Normal -> {}
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(
                    modifier = Modifier.clickable {
                        onEvent(TodoListEvent.ToggleCompleted(item))
                    },
                    color = if (item.isCompleted)
                        MaterialTheme.colorScheme.tertiaryLabel
                    else MaterialTheme.colorScheme.labelPrimary,
                    style = Typography.bodyMedium,
                    text = item.text,
                    textDecoration = if (item.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    },
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
                item.deadLine?.let {
                    Text(
                        text = ChangeType.changeDateFormat(item.deadLine!!),
                        style = Typography.titleSmall,
                        color = MaterialTheme.colorScheme.tertiaryLabel,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        IconButton(onClick = {
            onEvent(TodoListEvent.OnInfoBtnClicked(item.id))
        }) {
            Icon(
                painter = painterResource(id = R.drawable.info_outline),
                contentDescription = "Info button",
                tint = MaterialTheme.colorScheme.tertiaryLabel
            )
        }

    }
}

