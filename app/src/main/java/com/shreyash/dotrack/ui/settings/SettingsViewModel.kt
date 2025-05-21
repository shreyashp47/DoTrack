package com.shreyash.dotrack.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shreyash.dotrack.domain.usecase.preferences.GetAutoWallpaperEnabledUseCase
import com.shreyash.dotrack.domain.usecase.preferences.GetWallpaperColorUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetAutoWallpaperEnabledUseCase
import com.shreyash.dotrack.domain.usecase.preferences.SetWallpaperColorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getAutoWallpaperEnabledUseCase: GetAutoWallpaperEnabledUseCase,
    private val setAutoWallpaperEnabledUseCase: SetAutoWallpaperEnabledUseCase,
    private val getWallpaperColorUseCase: GetWallpaperColorUseCase,
    private val setWallpaperColorUseCase: SetWallpaperColorUseCase
) : ViewModel() {

    /**
     * Auto wallpaper enabled state
     */
    val autoWallpaperEnabled: StateFlow<Boolean> = getAutoWallpaperEnabledUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    /**
     * Wallpaper color state
     */
    val wallpaperColor: StateFlow<String> = getWallpaperColorUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "#3A0CA3" // Default blue color
        )

    /**
     * Show color picker dialog state
     */
    var showColorPickerDialog by mutableStateOf(false)
        private set

    /**
     * Available color options
     */
    val colorOptions = listOf(
        ColorOption("#3A0CA3", "Indigo"),      // same, deep and vibrant
        ColorOption("#D62828", "Dark Red"),    // slightly darker red
        ColorOption("#264653", "Dark Teal"),   // muted greenish blue
        ColorOption("#E76F51", "Burnt Orange"),// darker orange
        ColorOption("#6A0DAD", "Purple"),      // same, good
        ColorOption("#1C1C1C", "Graphite")     // better alternative to pure black
    )

    /**
     * Set auto wallpaper enabled
     */
    fun setAutoWallpaperEnabled(enabled: Boolean) {
        viewModelScope.launch {
            setAutoWallpaperEnabledUseCase(enabled)
        }
    }

    /**
     * Set wallpaper color
     */
    fun setWallpaperColor(colorHex: String) {
        viewModelScope.launch {
            setWallpaperColorUseCase(colorHex)
        }
    }

    /**
     * Show color picker dialog
     */
    fun showColorPicker() {
        showColorPickerDialog = true
    }

    /**
     * Hide color picker dialog
     */
    fun hideColorPicker() {
        showColorPickerDialog = false
    }
}

/**
 * Color option data class
 */
data class ColorOption(
    val hex: String,
    val name: String
)