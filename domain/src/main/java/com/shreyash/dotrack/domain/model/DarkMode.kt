package com.shreyash.dotrack.domain.model

enum class DarkMode(val value: String) {
    SYSTEM("system"),
    LIGHT("light"),
    DARK("dark");

    companion object {
        fun fromValue(value: String): DarkMode {
            return entries.firstOrNull { it.value == value } ?: SYSTEM
        }
    }
}
