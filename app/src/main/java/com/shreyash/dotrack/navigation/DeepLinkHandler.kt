package com.shreyash.dotrack.navigation

import android.content.Intent
import androidx.navigation.NavController

/**
 * Handles deep links for the application
 */
object DeepLinkHandler {
    
    // Constants for deep link actions and extras
    const val ACTION_OPEN_TASK = "com.shreyash.dotrack.OPEN_TASK"
    const val EXTRA_TASK_ID = "task_id"
    
    /**
     * Handles the given intent and navigates to the appropriate destination
     * @param intent The intent to handle
     * @param navController The navigation controller to use for navigation
     * @return true if the intent was handled, false otherwise
     */
    fun handleIntent(intent: Intent, navController: NavController): Boolean {
        return when (intent.action) {
            ACTION_OPEN_TASK -> {
                val taskId = intent.getStringExtra(EXTRA_TASK_ID)
                if (taskId != null) {
                    // Navigate to the task detail screen
                    navController.navigate(TaskDetail.createRoute(taskId))
                    true
                } else {
                    false
                }
            }
            else -> false
        }
    }
}