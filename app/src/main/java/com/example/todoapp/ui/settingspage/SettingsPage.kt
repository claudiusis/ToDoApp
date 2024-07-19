package com.example.todoapp.ui.settingspage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.ui.core.TodoAppTheme
import com.example.todoapp.ui.core.labelPrimary
import com.example.ui_core.Typography

@Composable
fun SettingPage(){
    TodoAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->

            Column {

                Row(
                    Modifier.padding(innerPadding),
                ) {
                    IconButton(
                        modifier = Modifier.padding(
                            start = 8.dp,
                            top = 8.dp,
                        ),
                        onClick = {

                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back_icon),
                            contentDescription = "Back arrow"
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.settings_title),
                        style = Typography.titleLarge,
                        color = MaterialTheme.colorScheme.labelPrimary,
                        textAlign = TextAlign.Center,
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(
                            top = 20.dp
                        )
                        .clickable {

                        }
                ) {
                    Text(
                        modifier = Modifier.padding(
                            start = 16.dp
                        ),
                        text = stringResource(id = R.string.theme),
                        style = Typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        modifier = Modifier.padding(
                            end = 16.dp
                        ),
                        text = stringResource(id = R.string.theme),
                        style = Typography.bodyMedium
                    )
                }
            }
        }
    }
}