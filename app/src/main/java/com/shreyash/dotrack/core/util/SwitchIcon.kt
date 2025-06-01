package com.shreyash.dotrack.core.util

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.util.Log

fun cleanupDuplicateIcons(context: Context) {
    try {
        val packageManager = context.packageManager
        val darkComponent = ComponentName(context, "${context.packageName}.IconDarkAlias")
        val lightComponent = ComponentName(context, "${context.packageName}.IconLightAlias")

        val darkState = packageManager.getComponentEnabledSetting(darkComponent)
        val lightState = packageManager.getComponentEnabledSetting(lightComponent)

        Log.d("SwitchIcon", "Cleanup - Dark state: $darkState, Light state: $lightState")

        // If both are enabled, disable both and then enable the correct one
        if (darkState == PackageManager.COMPONENT_ENABLED_STATE_ENABLED &&
            lightState == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        ) {

            Log.d("SwitchIcon", "Both icons enabled, cleaning up...")

            // Disable both
            packageManager.setComponentEnabledSetting(
                darkComponent,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
            packageManager.setComponentEnabledSetting(
                lightComponent,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )

            // Wait a moment then call the normal switch function
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                switchAppIcon(context)
            }, 1000)
        }
    } catch (e: Exception) {
        Log.e("SwitchIcon", "Error cleaning up duplicate icons", e)
    }
}

fun switchAppIcon(context: Context) {
    try {
        val isDarkTheme = (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        // Check if we've already set the correct icon for this theme
        val prefs = context.getSharedPreferences("app_icon_prefs", Context.MODE_PRIVATE)
        val lastIconState = prefs.getBoolean("is_dark_icon_enabled", false)

        // If the icon state matches the current theme, no need to switch
        if (lastIconState == isDarkTheme) {
            Log.d(
                "SwitchIcon",
                "Icon already matches theme (isDark: $isDarkTheme), skipping switch"
            )
            return
        }

        val packageManager = context.packageManager

        val darkComponent = ComponentName(context, "${context.packageName}.IconDarkAlias")
        val lightComponent = ComponentName(context, "${context.packageName}.IconLightAlias")

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

            // First, disable the other component to ensure only one is active
            if (otherState == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                packageManager.setComponentEnabledSetting(
                    otherComponent,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                Log.d("SwitchIcon", "Old icon disabled")
            }

            // Then enable the target component
            packageManager.setComponentEnabledSetting(
                targetComponent,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )

            // Save the new icon state
            prefs.edit().putBoolean("is_dark_icon_enabled", isDarkTheme).apply()
            Log.d("SwitchIcon", "New icon enabled successfully")

        } else {
            Log.d("SwitchIcon", "Icon already in correct state, updating preference")
            // Update preference to match current state
            prefs.edit().putBoolean("is_dark_icon_enabled", isDarkTheme).apply()

            // Ensure the other component is disabled
            if (otherState == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                packageManager.setComponentEnabledSetting(
                    otherComponent,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                Log.d("SwitchIcon", "Disabled duplicate icon")
            }
        }
    } catch (e: Exception) {
        Log.e("SwitchIcon", "Error switching app icon", e)
        // Don't crash the app if icon switching fails
    }
}
