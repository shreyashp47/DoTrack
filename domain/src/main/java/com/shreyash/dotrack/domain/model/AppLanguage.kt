package com.shreyash.dotrack.domain.model

enum class AppLanguage(val value: String) {
    SYSTEM("system"),
    ENGLISH("en"),
    HINDI("hi"),
    JAPANESE("ja"),
    GERMAN("de");

    companion object {
        fun fromValue(value: String): AppLanguage {
            return entries.firstOrNull { it.value == value } ?: SYSTEM
        }
    }
}
