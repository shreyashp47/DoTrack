# DoTrack - Developer Guide

## Introduction

This guide is intended for developers who want to understand, modify, or contribute to the DoTrack project. It provides detailed information about the project's architecture, code organization, and development practices.

## Table of Contents

1. [Development Environment Setup](#development-environment-setup)
2. [Project Architecture](#project-architecture)
3. [Module Structure](#module-structure)
4. [Key Components](#key-components)
5. [UI Implementation](#ui-implementation)
6. [Data Management](#data-management)
7. [Background Processing](#background-processing)
8. [Testing](#testing)
9. [Contribution Guidelines](#contribution-guidelines)
10. [Code Style and Conventions](#code-style-and-conventions)

## Development Environment Setup

### Requirements

- Android Studio Meerkat | 2024.3.1 or newer
- JDK 11
- Android SDK 35
- Minimum Android version: API 26 (Android 8.0)

### Getting Started

1. Clone the repository:
   ```
   git clone https://github.com/shreyash/dotrack.git
   ```

2. Open the project in Android Studio

3. Sync Gradle files

4. Build the project:
   ```
   ./gradlew build
   ```

5. Run the app on an emulator or physical device

## Project Architecture

DoTrack follows a multi-module clean architecture approach with MVVM pattern:

### Clean Architecture

The project is divided into three main layers:

1. **Presentation Layer (UI)**
   - Contains UI components, ViewModels, and UI state
   - Depends on the Domain layer
   - Located in the `app` module

2. **Domain Layer**
   - Contains business logic, use cases, and domain models
   - Independent of other layers
   - Located in the `domain` module

3. **Data Layer**
   - Contains repository implementations, data sources, and data models
   - Depends on the Domain layer
   - Located in the `data` module

### MVVM Pattern

- **Model**: Domain models and repository interfaces
- **View**: Jetpack Compose UI components
- **ViewModel**: Manages UI state and business logic

## Module Structure

### App Module

The `app` module contains:
- UI components (Compose screens and composables)
- ViewModels
- Navigation
- DI modules for the presentation layer
- Main application class

Key packages:
- `com.shreyash.dotrack.ui`: UI components organized by feature
- `com.shreyash.dotrack.navigation`: Navigation components
- `com.shreyash.dotrack.di`: Dependency injection modules
- `com.shreyash.dotrack.core.util`: Utilities specific to the presentation layer

### Domain Module

The `domain` module contains:
- Domain models
- Repository interfaces
- Use cases
- Business logic

Key packages:
- `com.shreyash.dotrack.domain.model`: Domain models
- `com.shreyash.dotrack.domain.repository`: Repository interfaces
- `com.shreyash.dotrack.domain.usecase`: Use cases organized by feature

### Data Module

The `data` module contains:
- Repository implementations
- Data sources (Room, DataStore)
- Data models
- Mappers between data and domain models

Key packages:
- `com.shreyash.dotrack.data.repository`: Repository implementations
- `com.shreyash.dotrack.data.local`: Local data sources (Room, DataStore)
- `com.shreyash.dotrack.data.mapper`: Mappers between data and domain models
- `com.shreyash.dotrack.data.worker`: WorkManager workers

### Core Module

The `core` module contains:
- Common utilities
- Extensions
- Base classes
- Shared UI components

Key packages:
- `com.shreyash.dotrack.core.util`: Common utilities
- `com.shreyash.dotrack.core.ui`: Shared UI components and theme

## Key Components

### Task Management

The task management feature is implemented with:

- **Domain Models**:
  ```kotlin
  data class Task(
      val id: String,
      val title: String,
      val description: String,
      val isCompleted: Boolean,
      val dueDate: LocalDateTime? = null,
      val priority: Priority,
      val reminderEnabled: Boolean = false,
      val createdAt: LocalDateTime,
      val updatedAt: LocalDateTime,
      val categoryId: Int? = null
  )
  
  enum class Priority(val value: Int) {
      LOW(1),
      MEDIUM(2),
      HIGH(3)
  }
  ```

- **Repository Interface**:
  ```kotlin
  interface TaskRepository {
      fun getTasks(): Flow<Result<List<Task>>>
      fun getTaskById(id: String): Flow<Result<Task>>
      suspend fun addTask(
          title: String,
          description: String,
          dueDate: LocalDateTime? = null,
          priority: Priority,
          reminderEnabled: Boolean
      ): Result<Unit>
      suspend fun updateTask(task: Task): Result<Unit>
      suspend fun deleteTask(id: String): Result<Unit>
      suspend fun deleteAllTask(): Result<Unit>
      suspend fun completeTask(id: String): Result<Unit>
      suspend fun uncompleteTask(id: String): Result<Unit>
      suspend fun disableReminder(id: String): Result<Unit>
  }
  ```

- **Use Cases**: Individual use cases for each operation (e.g., `GetTasksUseCase`, `AddTaskUseCase`)

- **ViewModels**: `TasksViewModel`, `TaskDetailViewModel`, `AddEditTaskViewModel`

- **UI Components**: `TasksScreen`, `TaskDetailScreen`, `AddEditTaskScreen`

### Dynamic Wallpaper

The dynamic wallpaper feature is implemented with:

- **WallpaperGenerator**:
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
      suspend fun generateAndSetWallpaper(tasks: List<Task>): Result<Unit> {
          // Implementation
      }
      
      private fun generateTaskListBitmap(
          tasks: List<Task>,
          startColorHex: String,
          endColorHex: String,
          highPriorityColorHex: String,
          mediumPriorityColorHex: String,
          lowPriorityColorHex: String
      ): Bitmap {
          // Implementation
      }
  }
  ```

- **Preferences**: DataStore preferences for wallpaper and priority colors

- **UI Components**: Settings screen for customizing wallpaper appearance

### Reminders and Notifications

The reminders and notifications feature is implemented with:

- **ReminderScheduler**:
  ```kotlin
  interface ReminderScheduler {
      fun scheduleReminder(taskId: String, title: String, dueDate: LocalDateTime)
      fun cancelReminder(taskId: String)
  }
  ```

- **ReminderWorker**:
  ```kotlin
  @HiltWorker
  class ReminderWorker @AssistedInject constructor(
      @Assisted appContext: Context,
      @Assisted workerParams: WorkerParameters,
      private val taskRepository: TaskRepository
  ) : CoroutineWorker(appContext, workerParams) {
      override suspend fun doWork(): Result {
          // Implementation
      }
      
      private fun showNotification(taskId: String, title: String) {
          // Implementation
      }
  }
  ```

- **Deep Linking**:
  ```kotlin
  object DeepLinkHandler {
      const val ACTION_OPEN_TASK = "com.shreyash.dotrack.OPEN_TASK"
      const val EXTRA_TASK_ID = "task_id"
      
      fun handleIntent(intent: Intent, navController: NavController): Boolean {
          // Implementation
      }
  }
  ```

## UI Implementation

DoTrack uses Jetpack Compose for its UI implementation:

### Theme

The app's theme is defined in `DoTrackTheme.kt`:

```kotlin
@Composable
fun DoTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Implementation
}
```

### Navigation

Navigation is implemented using Jetpack Compose Navigation:

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
    }
}
```

### Screens

Each screen is implemented as a Composable function:

```kotlin
@Composable
fun TasksScreen(
    onTaskClick: (String) -> Unit,
    onAddTaskClick: () -> Unit,
    viewModel: TasksViewModel = hiltViewModel()
) {
    // Implementation
}
```

### Previews

Previews are provided for UI components to facilitate development:

```kotlin
@Preview(showBackground = true)
@Composable
fun TasksScreenPreview() {
    DoTrackTheme {
        TasksScreen(
            onTaskClick = {},
            onAddTaskClick = {}
        )
    }
}
```

## Data Management

### Room Database

DoTrack uses Room for local data persistence:

- **Entities**: `TaskEntity`, `CategoryEntity`
- **DAOs**: `TaskDao`, `CategoryDao`
- **Database**: `TaskDatabase`

### DataStore

DoTrack uses DataStore for storing user preferences:

- **Preferences**: Wallpaper colors, priority colors, auto-update settings

### Repository Pattern

Repositories abstract data sources and provide a clean API for the domain layer:

```kotlin
@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val dispatcher: CoroutineDispatcher
) : TaskRepository {
    // Implementation
}
```

## Background Processing

### WorkManager

DoTrack uses WorkManager for scheduling reminders:

```kotlin
@Singleton
class ReminderSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : ReminderScheduler {
    override fun scheduleReminder(taskId: String, title: String, dueDate: LocalDateTime) {
        // Implementation using WorkManager
    }
}
```

### Coroutines and Flow

DoTrack uses Kotlin Coroutines and Flow for asynchronous operations and reactive programming:

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

## Testing

### Unit Tests

Unit tests focus on testing individual components in isolation:

```kotlin
@Test
fun `getTaskById returns success with task when found`() = runTest {
    // Arrange
    val taskId = "task-1"
    val taskEntity = TaskEntity(
        id = taskId,
        title = "Test Task",
        description = "Description",
        isCompleted = false,
        priority = Priority.MEDIUM.value,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
    coEvery { taskDao.getTaskById(taskId) } returns flowOf(taskEntity)
    
    // Act
    val result = taskRepository.getTaskById(taskId).first()
    
    // Assert
    assertTrue(result is Result.Success)
    assertEquals(taskId, (result as Result.Success).data.id)
}
```

### UI Tests

UI tests focus on testing UI components and user interactions:

```kotlin
@Test
fun taskScreen_displaysTaskList() {
    // Arrange
    val tasks = listOf(
        Task(
            id = "1",
            title = "Task 1",
            description = "Description 1",
            isCompleted = false,
            priority = Priority.HIGH,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    )
    
    // Act
    composeTestRule.setContent {
        DoTrackTheme {
            TasksScreen(
                onTaskClick = {},
                onAddTaskClick = {}
            )
        }
    }
    
    // Assert
    composeTestRule.onNodeWithText("Task 1").assertIsDisplayed()
}
```

## Contribution Guidelines

### Pull Request Process

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Commit Message Format

Follow the conventional commits format:

```
<type>(<scope>): <subject>

<body>

<footer>
```

Types:
- feat: A new feature
- fix: A bug fix
- docs: Documentation only changes
- style: Changes that do not affect the meaning of the code
- refactor: A code change that neither fixes a bug nor adds a feature
- perf: A code change that improves performance
- test: Adding missing tests or correcting existing tests
- chore: Changes to the build process or auxiliary tools

### Code Review

All code changes require a code review before merging:
- Code must pass all automated tests
- Code must follow the project's code style
- Code must be well-documented
- Pull requests should be focused on a single feature or fix

## Code Style and Conventions

### Kotlin Style Guide

Follow the [Kotlin style guide](https://developer.android.com/kotlin/style-guide):
- Use camelCase for variables and functions
- Use PascalCase for classes and interfaces
- Use UPPER_SNAKE_CASE for constants
- Use 4 spaces for indentation

### Architecture Guidelines

- Follow clean architecture principles
- Keep layers separated and dependencies flowing inward
- Use dependency injection for all dependencies
- Write unit tests for all components

### Compose Guidelines

- Follow the [Compose API guidelines](https://github.com/androidx/androidx/blob/androidx-main/compose/docs/compose-api-guidelines.md)
- Use preview functions for all composables
- Keep composables small and focused
- Use proper state hoisting

## Conclusion

This developer guide provides an overview of the DoTrack project's architecture, code organization, and development practices. By following these guidelines, you can contribute to the project effectively and maintain its quality and consistency.