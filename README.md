# DoTrack

A task management application built with modern Android development practices.

## Project Structure

The project follows a multi-module clean architecture approach:

- **app**: Main application module containing UI components and navigation
- **core**: Common utilities, UI components, and base classes shared across modules
- **domain**: Business logic, use cases, and domain models
- **data**: Data sources, repositories, and data mapping

## Architecture

- **Pattern**: MVVM (Model-View-ViewModel)
- **UI**: Jetpack Compose
- **Dependency Injection**: Hilt
- **Navigation**: Compose Navigation
- **Local Storage**: Room Database
- **Concurrency**: Kotlin Coroutines & Flow

## Features (Planned)

- Task creation and management
- Task categorization and prioritization
- Due date tracking
- Task completion tracking
- Statistics and insights

## Development Setup

### Requirements
- Android Studio Meerkat | 2024.3.1 or newer
- JDK 11
- Android SDK 35

### Building the app
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on an emulator or physical device

## License

[MIT License](LICENSE)