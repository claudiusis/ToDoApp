package com.example.todoapp.domain


enum class AppTheme(val value: String) {
    ModeDay("Всегда светлая"),
    ModeNight("Всегда тёмная"),
    ModeSystem("Как в системе");

    companion object {
        fun fromOrdinal(ordinal: Int) = entries[ordinal]
    }
}