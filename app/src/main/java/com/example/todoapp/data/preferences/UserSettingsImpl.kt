package com.example.todoapp.data.preferences

import android.content.Context
import androidx.core.content.edit
import com.example.todoapp.domain.AppTheme
import com.example.todoapp.domain.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.prefs.Preferences
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class UserSettingsImpl @Inject constructor(
    context: Context
) : UserSettings {
    override val themeStream: MutableStateFlow<AppTheme>
    override var theme: AppTheme by AppThemePreferencesDelegate("app_theme", AppTheme.ModeSystem)

    private val preferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE)


    init {
        themeStream = MutableStateFlow(theme)
    }

    inner class AppThemePreferencesDelegate(
        private val name : String,
        private val default : AppTheme
    ) : ReadWriteProperty<Any?, AppTheme> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): AppTheme =
            AppTheme.fromOrdinal(preferences.getInt(name, default.ordinal))

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: AppTheme) {
            themeStream.value = value
            preferences.edit {
                putInt(name, value.ordinal)
            }
        }
    }
}