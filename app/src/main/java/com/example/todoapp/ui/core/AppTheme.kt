package com.example.todoapp.ui.core

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.ui_core.BackElevatedDark
import com.example.ui_core.BackPrimaryDark
import com.example.ui_core.BackPrimaryLight
import com.example.ui_core.BackSecondaryDark
import com.example.ui_core.Blue
import com.example.ui_core.BlueBackSide
import com.example.ui_core.DarkBackSide
import com.example.ui_core.Gray
import com.example.ui_core.GrayLightDark
import com.example.ui_core.GrayLightLight
import com.example.ui_core.Green
import com.example.ui_core.LabelDisableDark
import com.example.ui_core.LabelDisableLight
import com.example.ui_core.LabelPrimaryDark
import com.example.ui_core.LabelPrimaryLight
import com.example.ui_core.LabelSecondaryDark
import com.example.ui_core.LabelSecondaryLight
import com.example.ui_core.LabelTertiaryDark
import com.example.ui_core.LabelTertiaryLight
import com.example.ui_core.Red
import com.example.ui_core.RedLight
import com.example.ui_core.SupportOverlayDark
import com.example.ui_core.SupportOverlayLight
import com.example.ui_core.SupportSeparatorDark
import com.example.ui_core.SupportSeparatorLight
import com.example.ui_core.Typography
import com.example.ui_core.White


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
    get() = if (ThemeCheck.isDarkTheme) GrayLightDark else GrayLightLight

@get:Composable
val ColorScheme.white: Color
    get() = White

val ColorScheme.separator: Color
    @Composable
    get() = if (ThemeCheck.isDarkTheme) SupportSeparatorDark else SupportSeparatorLight

val ColorScheme.backPrimary: Color
    @Composable
    get() = if (ThemeCheck.isDarkTheme) BackPrimaryDark else BackPrimaryLight

val ColorScheme.overlay: Color
    @Composable
    get() = if (ThemeCheck.isDarkTheme) SupportOverlayDark else SupportOverlayLight

val ColorScheme.disable: Color
    @Composable
    get() = if (ThemeCheck.isDarkTheme) LabelDisableDark else LabelDisableLight

val ColorScheme.labelPrimary: Color
    @Composable
    get() = if (ThemeCheck.isDarkTheme) LabelPrimaryDark else LabelPrimaryLight

val ColorScheme.labelSecondary: Color
    @Composable
    get() = if (ThemeCheck.isDarkTheme) LabelSecondaryDark else LabelSecondaryLight

val ColorScheme.tertiaryLabel: Color
    @Composable
    get() = if (ThemeCheck.isDarkTheme) LabelTertiaryDark else LabelTertiaryLight

val ColorScheme.backSecondary: Color
    @Composable
    get() = if (ThemeCheck.isDarkTheme) BackSecondaryDark else White

val ColorScheme.backElevated: Color
    @Composable
    get() = if (ThemeCheck.isDarkTheme) BackElevatedDark else White

@get:Composable
val ColorScheme.lightRed: Color
    get() = RedLight

val ColorScheme.blueLight: Color
    @Composable
    get() = if (ThemeCheck.isDarkTheme) DarkBackSide else BlueBackSide

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

    ThemeCheck.isDarkTheme = darkTheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}