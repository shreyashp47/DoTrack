package com.shreyash.dotrack.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shreyash.dotrack.domain.model.AppLanguage
import com.shreyash.dotrack.domain.usecase.preferences.GetDarkModeUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetLanguageUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetDarkModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val getDarkModeUseCase: GetDarkModeUseCase,
    private val setDarkModeUseCase: SetDarkModeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase
) : ViewModel() {

    val darkMode = getDarkModeUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "system")

    val language = getLanguageUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppLanguage.SYSTEM.value)

    var isUpdating by mutableStateOf(false)
        private set

    fun setDarkMode(mode: String) {
        viewModelScope.launch {
            isUpdating = true
            setDarkModeUseCase(mode)
            isUpdating = false
        }
    }
}
