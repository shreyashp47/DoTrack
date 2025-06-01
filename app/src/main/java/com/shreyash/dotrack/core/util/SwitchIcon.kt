package com.shreyash.dotrack.core.util

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.util.Log

fun switchAppIcon(context: Context) {
    try {
        val isDarkTheme = (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        // Check if we've already set the correct icon for this theme
        val prefs = context.getSharedPreferences("app_icon_prefs", Context.MODE_PRIVATE)
        val lastIconState = prefs.getBoolean("is_dark_icon_enabled", false)
        
        // If the icon state matches the current theme, no need to switch
        if (lastIconState == isDarkTheme) {
            Log.d("SwitchIcon", "Icon already matches theme (isDark: $isDarkTheme), skipping switch")
            return
        }

        val packageManager = context.packageManager

        val darkComponent = ComponentName(context, "${context.packageName}.DarkIcon")
        val lightComponent = ComponentName(context, "${context.packageName}.LightIcon")

        val targetComponent = if (isDarkTheme) darkComponent else lightComponent
        val otherComponent = if (isDarkTheme) lightComponent else darkComponent

        // Check current state to avoid unnecessary switches
        val targetState = packageManager.getComponentEnabledSetting(targetComponent)
        val otherState = packageManager.getComponentEnabledSetting(otherComponent)
        
        Log.d("SwitchIcon", "isDarkTheme: $isDarkTheme")
        Log.d("SwitchIcon", "Target component state: $targetState")
        Log.d("SwitchIcon", "Other component state: $otherState")

        // Only switch if the target component is not already enabled
        if (targetState != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            Log.d("SwitchIcon", "Switching app icon...")
            
            // Enable the target icon first
            packageManager.setComponentEnabledSetting(
                targetComponent,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
            
            // Save the new icon state immediately
            prefs.edit().putBoolean("is_dark_icon_enabled", isDarkTheme).apply()
            
            // If the other component is currently enabled, disable it after a delay
            if (otherState == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                // Wait a bit before disabling the old icon to avoid race conditions
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    try {
                        packageManager.setComponentEnabledSetting(
                            otherComponent,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                        Log.d("SwitchIcon", "Old icon disabled successfully (delayed)")
                    } catch (e: Exception) {
                        Log.e("SwitchIcon", "Error disabling old icon", e)
                    }
                }, 2000) // 2 second delay
                
                Log.d("SwitchIcon", "New icon enabled, old icon will be disabled shortly")
            } else {
                Log.d("SwitchIcon", "Target icon enabled")
            }
        } else {
            Log.d("SwitchIcon", "Icon already in correct state, updating preference")
            // Update preference to match current state
            prefs.edit().putBoolean("is_dark_icon_enabled", isDarkTheme).apply()
        }
    } catch (e: Exception) {
        Log.e("SwitchIcon", "Error switching app icon", e)
        // Don't crash the app if icon switching fails
    }
}
