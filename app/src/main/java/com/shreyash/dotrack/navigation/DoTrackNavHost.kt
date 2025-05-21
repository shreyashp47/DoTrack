package com.shreyash.dotrack.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shreyash.dotrack.ui.categories.AddEditCategoryScreen
import com.shreyash.dotrack.ui.categories.CategoriesScreen
import com.shreyash.dotrack.ui.settings.SettingsScreen
import com.shreyash.dotrack.ui.tasks.AddEditTaskScreen
import com.shreyash.dotrack.ui.tasks.TaskDetailScreen
import com.shreyash.dotrack.ui.tasks.TasksScreen

@Composable
fun DoTrackNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        // Tasks screen
        composable(route = Home.route) {
            TasksScreen(
                onTaskClick = { taskId ->
                    navController.navigate(TaskDetail.createRoute(taskId))
                },
                onAddTaskClick = {
                    navController.navigate(AddEditTask.route)
                }
            )
        }

        // Categories screen
        composable(route = Categories.route) {
            CategoriesScreen(
                onCategoryClick = { categoryId ->
                    // Navigate to category detail or edit
                    navController.navigate(AddEditCategory.createRoute(categoryId))
                },
                onAddCategoryClick = {
                    navController.navigate(AddEditCategory.route)
                }
            )
        }        // Categories screen
        composable(route = Settings.route) {
            SettingsScreen(

            )
        }

        // Task detail screen
        composable(
            route = TaskDetail.routeWithArgs,
            arguments = listOf(
                navArgument(TaskDetail.taskIdArg) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString(TaskDetail.taskIdArg) ?: ""
            TaskDetailScreen(
                taskId = taskId,
                onEditClick = {
                    navController.navigate(AddEditTask.createRoute(taskId))
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onDeleteSuccess = {
                    navController.popBackStack()
                }
            )
        }

        // Add/Edit task screen
        composable(
            route = AddEditTask.routeWithArgs,
            arguments = listOf(
                navArgument(AddEditTask.taskIdArg) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString(AddEditTask.taskIdArg)
            AddEditTaskScreen(
                taskId = taskId,
                onBackClick = {
                    navController.popBackStack()
                },
                onTaskSaved = {
                    navController.popBackStack()
                }
            )
        }

        // Add/Edit category screen
        composable(
            route = AddEditCategory.routeWithArgs,
            arguments = listOf(
                navArgument(AddEditCategory.categoryIdArg) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString(AddEditCategory.categoryIdArg)
            AddEditCategoryScreen(
                categoryId = categoryId,
                onBackClick = {
                    navController.popBackStack()
                },
//                 onCategorySaved = {
//                     navController.popBackStack()
//                 }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoTrackNavHostPreview() {
    MaterialTheme {
        DoTrackNavHost(
            navController = rememberNavController()
        )
    }
}