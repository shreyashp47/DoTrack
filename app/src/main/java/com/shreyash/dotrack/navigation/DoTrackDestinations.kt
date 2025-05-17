package com.shreyash.dotrack.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.graphics.vector.ImageVector

interface DoTrackDestination {
    val route: String
    val selectedIcon: ImageVector
    val unselectedIcon: ImageVector
    val title: String
}

object Home : DoTrackDestination {
    override val route = "home"
    override val selectedIcon = Icons.Filled.Home
    override val unselectedIcon = Icons.Outlined.Home
    override val title = "Tasks"
}

object Categories : DoTrackDestination {
    override val route = "categories"
    override val selectedIcon = Icons.Filled.Info
    override val unselectedIcon = Icons.Outlined.Info
    override val title = "Categories"
}

// Task detail destination with arguments
object TaskDetail {
    const val route = "task"
    const val taskIdArg = "taskId"
    const val routeWithArgs = "$route/{$taskIdArg}"
    
    fun createRoute(taskId: String) = "$route/$taskId"
}

// Add/Edit task destination
object AddEditTask {
    const val route = "add_edit_task"
    const val taskIdArg = "taskId"
    const val routeWithArgs = "$route?$taskIdArg={$taskIdArg}"
    
    fun createRoute(taskId: String? = null) = if (taskId != null) {
        "$route?$taskIdArg=$taskId"
    } else {
        route
    }
}

// Add/Edit category destination
object AddEditCategory {
    const val route = "add_edit_category"
    const val categoryIdArg = "categoryId"
    const val routeWithArgs = "$route?$categoryIdArg={$categoryIdArg}"
    
    fun createRoute(categoryId: String? = null) = if (categoryId != null) {
        "$route?$categoryIdArg=$categoryId"
    } else {
        route
    }
}

val bottomNavDestinations = listOf(Home, Categories)