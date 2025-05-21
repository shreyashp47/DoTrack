# DoTrack

A modern task management application built with clean architecture and Jetpack Compose that transforms your device wallpaper into a dynamic task list.

## Features

- **Task Management**: Create, edit, complete, and delete tasks
- **Task Categories**: Organize tasks by categories
- **Priority Levels**: Assign priority levels to tasks (High, Medium, Low)
- **Due Dates**: Set and track task due dates
- **Dynamic Wallpaper**: Automatically update your device wallpaper with pending tasks
- **Customizable Themes**: Choose from multiple color themes for your task wallpaper
- **User Preferences**: Configure app behavior including auto wallpaper updates

## Project Structure

The project follows a multi-module clean architecture approach:

- **app**: Main application module containing UI components and navigation
- **core**: Common utilities, UI components, and base classes shared across modules
- **domain**: Business logic, use cases, and domain models
- **data**: Data sources, repositories, and data mapping

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
- **Local Storage**: Room Database for tasks and categories
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

## Usage

### Creating Tasks

1. Tap the "+" button on the Tasks screen
2. Enter task details (title, description, priority, due date)
3. Select a category (optional)
4. Save the task

### Managing Tasks

- Swipe to complete/uncomplete tasks
- Tap on a task to view details
- Long press for additional options

### Customizing Wallpaper

1. Navigate to Settings
2. Toggle "Auto Wallpaper Updates" to enable/disable automatic updates
3. Tap "Wallpaper Color" to choose your preferred color theme
4. Changes will be applied to the next wallpaper update

## License

[MIT License](LICENSE)