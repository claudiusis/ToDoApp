package com.example.todoapp.ui.core

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat


private val DarkColorScheme = darkColorScheme(
    outline = SupportSeparatorDark,
    onPrimary = LabelPrimaryDark,
    onSecondary = LabelSecondaryDark,
    onTertiary = LabelTertiaryDark,
    background = BackPrimaryDark,
    surface = BackPrimaryDark,
)

private val LightColorScheme = lightColorScheme(
    outline = SupportSeparatorLight,
    onPrimary = LabelPrimaryLight,
    onSecondary = LabelSecondaryLight,
    onTertiary = LabelTertiaryLight,
    background = BackPrimaryLight,
    surface = BackPrimaryLight,
)

@get:Composable
val ColorScheme.blue: Color
    get() = Blue

@get:Composable
val ColorScheme.red: Color
    get() = Red

@get:Composable
val ColorScheme.green: Color
    get() = Green

@get:Composable
val ColorScheme.gray: Color
    get() = Gray

val ColorScheme.grayLight: Color
    @Composable
    get() = if (isSystemInDarkTheme()) GrayLightDark else GrayLightLight

@get:Composable
val ColorScheme.white: Color
    get() = White

val ColorScheme.separator: Color
    @Composable
    get() = if (isSystemInDarkTheme()) SupportSeparatorDark else SupportSeparatorLight

val ColorScheme.backPrimary: Color
    @Composable
    get() = if (isSystemInDarkTheme()) BackPrimaryDark else BackPrimaryLight

val ColorScheme.overlay: Color
    @Composable
    get() = if (isSystemInDarkTheme()) SupportOverlayDark else SupportOverlayLight

val ColorScheme.disable: Color
    @Composable
    get() = if (isSystemInDarkTheme()) LabelDisableDark else LabelDisableLight

val ColorScheme.labelPrimary: Color
    @Composable
    get() = if (isSystemInDarkTheme()) LabelPrimaryDark else LabelPrimaryLight

val ColorScheme.labelSecondary: Color
    @Composable
    get() = if (isSystemInDarkTheme()) LabelSecondaryDark else LabelSecondaryLight

val ColorScheme.tertiaryLabel: Color
    @Composable
    get() = if (isSystemInDarkTheme()) LabelTertiaryDark else LabelTertiaryLight

val ColorScheme.backSecondary: Color
    @Composable
    get() = if (isSystemInDarkTheme()) BackSecondaryDark else White

val ColorScheme.backElevated: Color
    @Composable
    get() = if (isSystemInDarkTheme()) BackElevatedDark else White

@get:Composable
val ColorScheme.lightRed: Color
    get() = RedLight

val ColorScheme.blueLight: Color
    @Composable
    get() = if (isSystemInDarkTheme()) DarkBackSide else BlueBackSide

@Composable
fun TodoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


@Preview
@Composable
private fun LightTheme(
    line1 : List<Color> = listOf(
        MaterialTheme.colorScheme.separator,
        MaterialTheme.colorScheme.overlay
    ),
    line2 : List<Color> = listOf(
        MaterialTheme.colorScheme.labelPrimary,
        MaterialTheme.colorScheme.labelSecondary,
        MaterialTheme.colorScheme.tertiaryLabel,
        MaterialTheme.colorScheme.disable
    ),
    line3 : List<Color> = listOf(
        MaterialTheme.colorScheme.red,
        MaterialTheme.colorScheme.green,
        MaterialTheme.colorScheme.blue,
        MaterialTheme.colorScheme.gray,
        MaterialTheme.colorScheme.grayLight,
        MaterialTheme.colorScheme.white
    ),
    line4 : List<Color> = listOf(
        MaterialTheme.colorScheme.backPrimary,
        MaterialTheme.colorScheme.backSecondary,
        MaterialTheme.colorScheme.backElevated
    ),
){
    TodoAppTheme(
        darkTheme = false
    ) {
        Scaffold(
            modifier = Modifier.fillMaxWidth()
        ) { innerPadding ->
            Column {


                LazyRow(
                    contentPadding = innerPadding
                ) {
                    items(line1) { item ->
                        Box(
                            modifier = Modifier
                                .background(item)
                                .size(128.dp, 64.dp)
                        ) {}
                    }
                }
                LazyRow(
                    contentPadding = innerPadding
                ) {
                    items(line2) { item ->
                        Box(
                            modifier = Modifier
                                .background(item)
                                .size(128.dp, 64.dp)
                        ) {}
                    }
                }
                LazyRow(
                    contentPadding = innerPadding
                ) {
                    items(line3) { item ->
                        Box(
                            modifier = Modifier
                                .background(item)
                                .size(128.dp, 64.dp)
                        ) {}
                    }
                }
                LazyRow(
                    contentPadding = innerPadding
                ) {
                    items(line4) { item ->
                        Box(
                            modifier = Modifier
                                .background(item)
                                .size(128.dp, 64.dp)
                        ) {}
                    }
                }
                Text(
                    text = "Large title",
                    style = Typography.titleLarge,
                )
                Text(
                    text = "Title",
                    style = Typography.titleMedium,
                )
                Text(
                    text = "Button",
                    style = Typography.bodyLarge,
                )
                Text(
                    text = "Body",
                    style = Typography.bodyMedium,
                )
                Text(
                    text = "Subhead",
                    style = Typography.titleSmall,
                )
            }
        }
    }
}

@Preview
@Composable
private fun BlackTheme(
    line1 : List<Color> = listOf(
        MaterialTheme.colorScheme.separator,
        MaterialTheme.colorScheme.overlay
    ),
    line2 : List<Color> = listOf(
        MaterialTheme.colorScheme.labelPrimary,
        MaterialTheme.colorScheme.labelSecondary,
        MaterialTheme.colorScheme.tertiaryLabel,
        MaterialTheme.colorScheme.disable
    ),
    line3 : List<Color> = listOf(
        MaterialTheme.colorScheme.red,
        MaterialTheme.colorScheme.green,
        MaterialTheme.colorScheme.blue,
        MaterialTheme.colorScheme.gray,
        MaterialTheme.colorScheme.grayLight,
        MaterialTheme.colorScheme.white
    ),
    line4 : List<Color> = listOf(
        MaterialTheme.colorScheme.backPrimary,
        MaterialTheme.colorScheme.backSecondary,
        MaterialTheme.colorScheme.backElevated
    ),
){
    TodoAppTheme(
        darkTheme = true
    ) {
        Scaffold(
            modifier = Modifier.fillMaxWidth()
        ) { innerPadding ->
            Column {


                LazyRow(
                    contentPadding = innerPadding
                ) {
                    items(line1) { item ->
                        Box(
                            modifier = Modifier
                                .background(item)
                                .size(128.dp, 64.dp)
                        ) {}
                    }
                }
                LazyRow(
                    contentPadding = innerPadding
                ) {
                    items(line2) { item ->
                        Box(
                            modifier = Modifier
                                .background(item)
                                .size(128.dp, 64.dp)
                        ) {}
                    }
                }
                LazyRow(
                    contentPadding = innerPadding
                ) {
                    items(line3) { item ->
                        Box(
                            modifier = Modifier
                                .background(item)
                                .size(128.dp, 64.dp)
                        ) {}
                    }
                }
                LazyRow(
                    contentPadding = innerPadding
                ) {
                    items(line4) { item ->
                        Box(
                            modifier = Modifier
                                .background(item)
                                .size(128.dp, 64.dp)
                        ) {}
                    }
                }
                Text(
                    text = "Large title",
                    style = Typography.titleLarge,
                )
                Text(
                    text = "Title",
                    style = Typography.titleMedium,
                )
                Text(
                    text = "Button",
                    style = Typography.bodyLarge,
                )
                Text(
                    text = "Body",
                    style = Typography.bodyMedium,
                )
                Text(
                    text = "Subhead",
                    style = Typography.titleSmall,
                )
            }
        }
    }
}