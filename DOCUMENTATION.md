# DoTrack - Technical Documentation

## Overview

DoTrack is a modern task management application built with clean architecture principles and Jetpack Compose. It transforms your device wallpaper into a dynamic task list, providing a unique way to stay on top of your tasks.

## Table of Contents

1. [Architecture](#architecture)
2. [Project Structure](#project-structure)
3. [Key Features](#key-features)
4. [Technical Implementation](#technical-implementation)
5. [UI Components](#ui-components)
6. [Data Flow](#data-flow)
7. [Notifications and Reminders](#notifications-and-reminders)
8. [Deep Linking](#deep-linking)
9. [Customization](#customization)
10. [Testing](#testing)
11. [Future Enhancements](#future-enhancements)

## Architecture

DoTrack follows a multi-module clean architecture approach, separating concerns into distinct layers:

### Clean Architecture Layers

1. **Presentation Layer (UI)**
   - Jetpack Compose UI components
   - ViewModels that manage UI state
   - Navigation components

2. **Domain Layer**
   - Business logic encapsulated in use cases
   - Repository interfaces
   - Domain models

3. **Data Layer**
   - Repository implementations
   - Data sources (Room database, DataStore)
   - Data models and mappers

### Design Patterns

- **MVVM (Model-View-ViewModel)**: Separates UI from business logic
- **Repository Pattern**: Abstracts data sources
- **Use Case Pattern**: Encapsulates business logic
- **Dependency Injection**: Uses Hilt for DI
- **Observer Pattern**: Uses Kotlin Flow for reactive programming

## Project Structure

The project is organized into multiple modules:

### App Module
- Contains UI components, ViewModels, and navigation
- Implements the presentation layer
- Handles user interactions and UI state

### Domain Module
- Contains business logic and use cases
- Defines repository interfaces
- Contains domain models

### Data Module
- Implements repository interfaces
- Contains data sources (Room, DataStore)
- Handles data mapping between domain and data models

### Core Module
- Contains common utilities and extensions
- Shared UI components
- Base classes used across modules

## Key Features

### Task Management
- Create, edit, complete, and delete tasks
- Assign priority levels (High, Medium, Low)
- Set due dates and reminders
- Categorize tasks

### Dynamic Wallpaper
- Automatically updates device wallpaper with pending tasks
- Tasks are sorted by priority
- Custom gradient background
- Text colors adapt based on background brightness

### Reminders and Notifications
- Set reminders for tasks
- Receive notifications before task due dates
- Deep link from notifications to task details

### Customization
- Choose wallpaper colors
- Customize priority colors
- Toggle automatic wallpaper updates
- Settings are persisted using DataStore

## Technical Implementation

### Dependency Injection
DoTrack uses Hilt for dependency injection, providing a standard way to incorporate dependency injection into the application.

```kotlin
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val uncompleteTaskUseCase: UncompleteTaskUseCase,
    private val wallpaperGenerator: WallpaperGenerator,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getAutoWallpaperEnabledUseCase: GetAutoWallpaperEnabledUseCase,
    private val disableReminderUseCase: DisableReminderUseCase,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {
    // Implementation
}
```

### Navigation
DoTrack uses Jetpack Compose Navigation for handling navigation between screens.

```kotlin
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
        // Navigation destinations
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
        
        // Other destinations...
    }
}
```

### Data Persistence
DoTrack uses Room for storing task data and DataStore for user preferences.

#### Room Database
- Stores tasks, categories, and their relationships
- Provides type-safe queries
- Supports reactive data with Flow

#### DataStore
- Stores user preferences like wallpaper colors and settings
- Provides type-safe access to preferences
- Supports reactive data with Flow

### Concurrency
DoTrack uses Kotlin Coroutines and Flow for handling asynchronous operations.

```kotlin
fun getTasks(): Flow<Result<List<Task>>> {
    return flow {
        emit(Result.Loading)
        try {
            val tasks = taskDao.getAllTasks().map { it.toDomain() }
            emit(Result.Success(tasks))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
```

## UI Components

### Screens
1. **TasksScreen**: Displays a list of tasks with completion status
2. **TaskDetailScreen**: Shows detailed information about a task
3. **AddEditTaskScreen**: Form for creating or editing tasks
4. **SettingsScreen**: Allows customization of app behavior and appearance
5. **CategoriesScreen**: Manages task categories

### Composables
DoTrack uses Jetpack Compose for building the UI, with custom composables for specific UI elements:

```kotlin
@Composable
fun TaskItem(
    task: Task,
    onTaskClick: (String) -> Unit,
    onCheckboxClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // Implementation
}
```

## Data Flow

1. **User Interaction**: User interacts with the UI
2. **ViewModel**: Processes user actions and updates UI state
3. **Use Cases**: Execute business logic
4. **Repository**: Abstracts data operations
5. **Data Sources**: Perform actual data operations (Room, DataStore)
6. **Flow**: Data flows back to the UI through Flow and StateFlow

## Notifications and Reminders

DoTrack uses WorkManager for scheduling task reminders:

```kotlin
@Singleton
class ReminderSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : ReminderScheduler {

    override fun scheduleReminder(taskId: String, title: String, dueDate: LocalDateTime) {
        CoroutineScope(ioDispatcher).launch {
            val triggerTime = dueDate.minusMinutes(30) // 30 minutes before due time
            val delay = Duration.between(LocalDateTime.now(), triggerTime).toMillis()
            if (delay > 0) {
                val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .setInputData(workDataOf(
                        ReminderWorker.KEY_TASK_ID to taskId,
                        ReminderWorker.KEY_TITLE to title
                    ))
                    .addTag(taskId)
                    .build()

                WorkManager.getInstance(context).enqueue(workRequest)
            }
        }
    }

    override fun cancelReminder(taskId: String) {
        CoroutineScope(ioDispatcher).launch {
            WorkManager.getInstance(context).cancelAllWorkByTag(taskId)
        }
    }
}
```

The `ReminderWorker` handles showing notifications and updating the task's reminder status:

```kotlin
@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val taskRepository: TaskRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        try {
            val taskId = inputData.getString(KEY_TASK_ID) ?: return Result.failure()
            val title = inputData.getString(KEY_TITLE) ?: "Task Reminder"

            // Disable the reminder in the database
            withContext(Dispatchers.IO) {
                taskRepository.disableReminder(taskId)
            }

            // Show the notification
            showNotification(taskId, title)
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
    
    // Implementation of showNotification method
}
```

## Deep Linking

DoTrack supports deep linking from notifications to specific task details:

```kotlin
object DeepLinkHandler {
    
    const val ACTION_OPEN_TASK = "com.shreyash.dotrack.OPEN_TASK"
    const val EXTRA_TASK_ID = "task_id"
    
    fun handleIntent(intent: Intent, navController: NavController): Boolean {
        return when (intent.action) {
            ACTION_OPEN_TASK -> {
                val taskId = intent.getStringExtra(EXTRA_TASK_ID)
                if (taskId != null) {
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
```

## Customization

DoTrack allows users to customize various aspects of the app:

### Wallpaper Customization
- Background color
- Priority colors
- Auto-update settings

### Settings Screen
The SettingsScreen allows users to:
- Toggle automatic wallpaper updates
- Choose wallpaper colors
- Customize priority colors
- Manage notification permissions

## Dynamic Wallpaper Generation

The WallpaperGenerator class is responsible for creating and setting the wallpaper:

```kotlin
@Singleton
class WallpaperGenerator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getWallpaperColorUseCase: GetWallpaperColorUseCase,
    private val getSecondaryWallpaperColorUseCase: GetSecondaryWallpaperColorUseCase,
    private val getHighPriorityColorUseCase: GetHighPriorityColorUseCase,
    private val getMediumPriorityColorUseCase: GetMediumPriorityColorUseCase,
    private val getLowPriorityColorUseCase: GetLowPriorityColorUseCase
) {
    // Implementation of wallpaper generation
}
```

The wallpaper is generated using Android's Canvas API:
1. Create a bitmap with the device's dimensions
2. Draw a gradient background
3. Draw task items sorted by priority
4. Set the bitmap as the device wallpaper

## Testing

DoTrack includes various types of tests:

### Unit Tests
- Test individual components in isolation
- Use JUnit and MockK for mocking dependencies

### Integration Tests
- Test interactions between components
- Ensure proper data flow between layers

### UI Tests
- Test UI components and user interactions
- Use Compose UI testing framework

## Future Enhancements

Potential future enhancements for DoTrack:

1. **Task Collaboration**: Allow sharing tasks with other users
2. **Cloud Sync**: Sync tasks across multiple devices
3. **Advanced Filtering**: Filter tasks by multiple criteria
4. **Statistics and Analytics**: Provide insights into task completion patterns
5. **Widgets**: Add home screen widgets for quick task management
6. **Voice Commands**: Add support for creating tasks via voice commands
7. **Calendar Integration**: Integrate with device calendar
8. **Dark Mode Improvements**: Enhanced dark mode support
9. **Accessibility Enhancements**: Improve app accessibility
10. **Performance Optimizations**: Further optimize app performance

## Conclusion

DoTrack is a modern, feature-rich task management application built with clean architecture principles and the latest Android technologies. Its unique wallpaper feature provides a constant reminder of pending tasks, helping users stay organized and productive.