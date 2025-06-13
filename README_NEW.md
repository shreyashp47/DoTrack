# DoTrack

A modern task management application built with clean architecture and Jetpack Compose that transforms your device wallpaper into a dynamic task list.

## Features

- **Task Management**: Create, edit, complete, and delete tasks
- **Priority Levels**: Assign priority levels to tasks (High, Medium, Low)
- **Due Dates**: Set and track task due dates
- **Dynamic Wallpaper**: Automatically update your device wallpaper with pending tasks
- **Customizable Themes**: Choose from multiple color themes for your task wallpaper
- **User Preferences**: Configure app behavior including auto wallpaper updates



## Architecture

### Clean Architecture

DoTrack implements clean architecture principles with three main layers:

1. **Presentation Layer (UI)**:
   - Jetpack Compose UI components
   - ViewModels that manage UI state
   - Navigation components

2. **Domain Layer**:
   - Business logic encapsulated in use cases
   - Repository interfaces
   - Domain models

3. **Data Layer**:
   - Repository implementations
   - Data sources (Room database, DataStore)
   - Data models and mappers

### Key Technologies

- **Pattern**: MVVM (Model-View-ViewModel)
- **UI**: Jetpack Compose with Material 3
- **Dependency Injection**: Hilt
- **Navigation**: Compose Navigation
- **Local Storage**: Room Database for tasks
- **Preferences**: DataStore for user settings
- **Concurrency**: Kotlin Coroutines & Flow
- **Wallpaper**: Custom wallpaper generation with Android Canvas API

## Key Features Implementation

### Dynamic Wallpaper

The app can automatically update your device wallpaper with your pending tasks:

- Tasks are rendered on a custom gradient background
- Text colors adapt based on background brightness
- Tasks are sorted by priority
- Wallpaper updates when tasks are added, completed, or deleted

### Customizable Themes

Users can personalize their experience:

- Choose from multiple color themes for the wallpaper background
- Toggle automatic wallpaper updates
- Settings are persisted using DataStore

## Development Setup

### Requirements
- Android Studio Meerkat | 2024.3.1 or newer
- JDK 11
- Android SDK 35
- Minimum Android version: API 26 (Android 8.0)

### Building the app
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on an emulator or physical device

## CI/CD Pipeline

DoTrack uses GitHub Actions for Continuous Integration and Continuous Deployment:

- **Automated Builds**: Every pull request and push to main branch triggers a build to ensure code quality
- **Version Management**: Automatic version incrementation for each release
- **Release Management**: Streamlined process for creating release builds
- **Google Play Deployment**: Automated deployment to Google Play Store (internal testing and production)

The CI/CD pipeline handles:
1. Building the Android App Bundle (AAB)
2. Signing the release with proper keystore credentials
3. Deploying to Google Play Store (internal testing or production)
4. Creating release tags in GitHub
5. Updating version numbers in the repository

## Usage

### Creating Tasks

1. Tap the "+" button on the Tasks screen
2. Enter task details (title, description, priority, due date)
3. Save the task

### Managing Tasks

- Swipe to complete/uncomplete tasks
- Tap on a task to view details
- Long press for additional options

### Customizing Wallpaper

1. Navigate to Settings
2. Toggle "Auto Wallpaper Updates" to enable/disable automatic updates
3. Tap "Wallpaper Color" to choose your preferred color theme
4. Changes will be applied to the next wallpaper update

## Project Structure

The project follows a multi-module clean architecture approach:

- **app**: Main application module containing UI components and navigation
- **core**: Common utilities, UI components, and base classes shared across modules
- **domain**: Business logic, use cases, and domain models
- **data**: Data sources, repositories, and data mapping

app/src/main/java/com/shreyash/dotrack/
├── core
│   └── util
│       ├── SwitchIcon.kt
│       └── WallpaperGenerator.kt
├── DoTrackApplication.kt
├── MainActivity.kt
├── navigation
│   ├── DeepLinkHandler.kt
│   ├── DoTrackBottomNavigation.kt
│   ├── DoTrackDestinations.kt
│   └── DoTrackNavHost.kt
├── ReminderWorker.kt
├── TrackConstants.kt
├── ui
│   ├── categories
│   │   ├── AddEditCategoryScreen.kt
│   │   ├── CategoriesScreen.kt
│   │   └── CategoriesViewModel.kt
│   ├── settings
│   │   ├── SettingsScreen.kt
│   │   └── SettingsViewModel.kt
│   └── tasks
│       ├── addedit
│       │   ├── AddEditTaskScreen.kt
│       │   ├── AddEditTaskViewModel.kt
│       │   └── TimePickerDialog.kt
│       ├── TaskDetailScreen.kt
│       ├── TaskDetailViewModel.kt
│       ├── TasksScreen.kt
│       └── TasksViewModel.kt
└── workmanager
├── ReminderModule.kt
├── ReminderSchedulerImpl.kt
└── TaskRepositoryEntryPoint.kt



core/src/main/java/com/shreyash/dotrack
└── core
├── di
│   └── DispatcherModule.kt
├── ui
│   ├── components
│   │   └── LoadingIndicator.kt
│   └── theme
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── util
└── Result.kt


data/src/main/java/com/shreyash/dotrack
└── data
├── di
│   ├── DatabaseModule.kt
│   ├── DataStoreModule.kt
│   ├── RepositoryModule.kt
│   └── WorkManagerModule.kt
├── local
│   ├── converter
│   │   ├── DateTimeConverters.kt
│   │   └── PriorityConverters.kt
│   ├── dao
│   │   ├── CategoryDao.kt
│   │   └── TaskDao.kt
│   ├── entity
│   │   ├── CategoryEntity.kt
│   │   └── TaskEntity.kt
│   └── TaskDatabase.kt
├── repository
│   ├── CategoryRepositoryImpl.kt
│   ├── TaskRepositoryImpl.kt
│   └── UserPreferencesRepositoryImpl.kt
└── util
└── DateTimeConverters.kt

domain/src/main/java/com/shreyash/dotrack
└── domain
├── model
│   ├── Category.kt
│   └── Task.kt
├── ReminderScheduler.kt
├── repository
│   ├── CategoryRepository.kt
│   ├── TaskRepository.kt
│   └── UserPreferencesRepository.kt
└── usecase
├── AddTaskUseCase.kt
├── category
│   ├── AddCategoryUseCase.kt
│   ├── DeleteCategoryUseCase.kt
│   ├── GetCategoriesUseCase.kt
│   ├── GetCategoryByIdUseCase.kt
│   └── UpdateCategoryUseCase.kt
├── GetTasksUseCase.kt
├── preferences
│   ├── GetAutoWallpaperEnabledUseCase.kt
│   ├── GetHighPriorityColorUseCase.kt
│   ├── GetLowPriorityColorUseCase.kt
│   ├── GetMediumPriorityColorUseCase.kt
│   ├── GetSecondaryWallpaperColorUseCase.kt
│   ├── GetWallpaperColorUseCase.kt
│   ├── SetAutoWallpaperEnabledUseCase.kt
│   ├── SetHighPriorityColorUseCase.kt
│   ├── SetLowPriorityColorUseCase.kt
│   ├── SetMediumPriorityColorUseCase.kt
│   ├── SetSecondaryWallpaperColorUseCase.kt
│   └── SetWallpaperColorUseCase.kt
└── task
├── AddTaskUseCase.kt
├── CompleteTaskUseCase.kt
├── DeleteTaskUseCase.kt
├── DisableReminderUseCase.kt
├── GetTaskByIdUseCase.kt
├── GetTasksUseCase.kt
├── UncompleteTaskUseCase.kt
└── UpdateTaskUseCase.kt


## Screenshots

Here are some screenshots of the application:

<table>
  <tr>
    <td><img src="https://github.com/shreyashp47/DoTrack/blob/main/SS/Screenshot_20250608_093726.png" alt="Task List Screen" width="200"/></td>
    <td><img src="https://github.com/shreyashp47/DoTrack/blob/main/SS/Screenshot_20250608_092713.png" alt="Add Task Screen" width="200"/></td>
    <td><img src="https://github.com/shreyashp47/DoTrack/blob/main/SS/Screenshot_20250608_095253.png" alt="Settings Screen" width="200"/></td>
    <td><img src="https://github.com/shreyashp47/DoTrack/blob/main/SS/Screenshot_20250608_093813.png" alt="Wallpaper Preview" width="200"/></td>
  </tr>
</table>

