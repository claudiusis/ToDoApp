package com.example.todoapp.ui.settingspage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.todoapp.R
import com.example.todoapp.ToDoApp
import com.example.todoapp.core.Importance
import com.example.todoapp.domain.AppTheme
import com.example.todoapp.domain.UserSettings
import com.example.todoapp.navigation.Router
import com.example.todoapp.ui.core.TodoAppTheme
import com.example.todoapp.ui.core.labelPrimary
import com.example.todoapp.ui.core.separator
import com.example.todoapp.ui.taskpage.TaskEvent
import com.example.ui_core.Typography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingPage(
    router: Router,
    settings: UserSettings
){
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }

        Column {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =  Modifier.padding(innerPadding)
            ) {
                IconButton(
                    modifier = Modifier.padding(
                        start = 8.dp,
                        top = 8.dp,
                    ),
                    onClick = {
                        router.navigateBack()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back_icon),
                        contentDescription = stringResource(id = R.string.close)
                    )
                }
                Spacer(modifier = Modifier.weight(0.5f))
                Text(
                    text = stringResource(id = R.string.settings_title),
                    style = Typography.titleLarge,
                    color = MaterialTheme.colorScheme.labelPrimary,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.weight(0.8f))
            }
            Row(
                modifier = Modifier
                    .padding(
                        top = 20.dp,
                        start = 8.dp,
                        end = 8.dp
                    )
                    .clickable {
                        showBottomSheet = true
                    }
            ) {
                Text(
                    modifier = Modifier.padding(
                        top = 16.dp,
                        bottom = 16.dp,
                        start = 16.dp
                    ),
                    text = stringResource(id = R.string.theme),
                    style = Typography.bodyMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(
                        top = 16.dp,
                        bottom = 16.dp,
                        end = 16.dp
                    ),
                    text = settings.theme.value,
                    style = Typography.bodyMedium
                )
            }
        }

        if (showBottomSheet){
            ModalBottomSheet(
                sheetState= sheetState,
                onDismissRequest = {
                    showBottomSheet = false
                }) {

                Column(
                    modifier = Modifier.padding(top= 20.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        text = stringResource(id = R.string.theme),
                        style = Typography.bodyLarge,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.labelPrimary,
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .clickable {
                                settings.theme = AppTheme.ModeDay
                                scope
                                    .launch { sheetState.hide() }
                                    .invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                            },
                        text = stringResource(id = R.string.light),
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.labelPrimary,
                    )
                    Divider(
                        color = MaterialTheme.colorScheme.separator,
                        thickness = 1.dp
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .clickable {
                                settings.theme = AppTheme.ModeNight
                                scope
                                    .launch { sheetState.hide() }
                                    .invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                            },
                        text = stringResource(id = R.string.dark),
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.labelPrimary,
                    )
                    Divider(
                        color = MaterialTheme.colorScheme.separator,
                        thickness = 1.dp
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .clickable {
                                settings.theme = AppTheme.ModeSystem
                                scope
                                    .launch { sheetState.hide() }
                                    .invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                            },
                        text = stringResource(id = R.string.system),
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.labelPrimary,
                    )
                }

            }
        }
    }
}