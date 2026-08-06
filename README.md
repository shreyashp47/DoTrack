# DoTrack

<div align="center"> 
<meta property="twitter:image" content="https://github.com/shreyashp47/DoTrack/blob/main/SS/coverimage.png">
<img src="https://github.com/shreyashp47/DoTrack/blob/main/SS/coverimage.png" />
 
**A modern task management application that transforms your device wallpaper into a dynamic task list**

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=26)
[![Kotlin](https://img.shields.io/badge/Kotlin-100%25-blue.svg)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Build](https://github.com/shreyashp47/DoTrack/actions/workflows/build_bundle.yml/badge.svg)](https://github.com/shreyashp47/DoTrack/actions/workflows/build_bundle.yml)
[![APK](https://github.com/shreyashp47/DoTrack/actions/workflows/build_apk.yml/badge.svg)](https://github.com/shreyashp47/DoTrack/actions/workflows/build_apk.yml)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](CONTRIBUTING.md)
[![Release](https://img.shields.io/github/v/release/shreyashp47/DoTrack?sort=semver)](https://github.com/shreyashp47/DoTrack/releases)
[![Play Store](https://img.shields.io/badge/Live%20on-Google%20Play-00D184.svg)](https://play.google.com/store/apps/details?id=com.shreyash.dotrack)

[Download APK](https://github.com/shreyashp47/DoTrack/releases) • [Get it on Google Play](https://play.google.com/store/apps/details?id=com.shreyash.dotrack) • [Documentation](docs/) • [Changelog](CHANGELOG.md) • [Report Bug](https://github.com/shreyashp47/DoTrack/issues) • [Request Feature](https://github.com/shreyashp47/DoTrack/issues)

</div>

---

## Features

<table>
<tr>
<td width="50%">

### **Task Management**
- Create, edit, complete, and delete tasks
- Priority levels (High, Medium, Low)
- Due dates and time tracking
- Category organization
- Sort and filter tasks (by date, priority, title, status)
- Clear completed tasks in bulk
- Smart reminder notifications

</td>
<td width="50%">

### **Dynamic Wallpaper**
- Auto-update device wallpaper with tasks
- Customizable color themes
- Adaptive text colors for readability
- Real-time wallpaper sync
- Manual sync control

</td>
</tr>
</table>

---

## Screenshots

<div align="center">
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/shreyashp47/DoTrack/blob/main/SS/Screenshot_20250608_093726.png" alt="Tasks Screen" width="200"/>
      <br><b>Tasks Overview</b>
    </td>
    <td align="center">
      <img src="https://github.com/shreyashp47/DoTrack/blob/main/SS/Screenshot_20250608_092713.png" alt="Add Task" width="200"/>
      <br><b>Add/Edit Task</b>
    </td>
    <td align="center">
      <img src="https://github.com/shreyashp47/DoTrack/blob/main/SS/Screenshot_20250608_095253.png" alt="Settings" width="200"/>
      <br><b>Settings</b>
    </td>
    <td align="center">
      <img src="https://github.com/shreyashp47/DoTrack/blob/main/SS/Screenshot_20250608_093813.png" alt="Wallpaper" width="200"/>
      <br><b>Dynamic Wallpaper</b>
    </td>
  </tr>
</table>
</div>

---

## Quick Start

Want to run DoTrack yourself? Check out the **[Quick Start Guide](QUICK_START.md)** — prerequisites, setup, and APK download.

---

## Architecture

DoTrack follows **Clean Architecture** principles with a multi-module approach:

```
DoTrack
├── app/                    # Presentation Layer
│   ├── ui/                 # Compose UI Components
│   ├── navigation/         # Navigation Logic
│   └── workmanager/        # Background Tasks
├── domain/                 # Business Logic Layer
│   ├── model/              # Domain Models
│   ├── repository/         # Repository Interfaces
│   └── usecase/            # Business Use Cases
├── data/                   # Data Layer
│   ├── local/              # Room Database
│   ├── repository/         # Repository Implementations
│   └── di/                 # Dependency Injection
└── core/                   # Shared Components
    ├── ui/                 # Common UI Components
    └── util/               # Utilities
```

### Tech Stack

<table>
<tr>
<td><b>Architecture</b></td>
<td>MVVM + Clean Architecture</td>
</tr>
<tr>
<td><b>UI Framework</b></td>
<td>Jetpack Compose + Material 3</td>
</tr>
<tr>
<td><b>Dependency Injection</b></td>
<td>Hilt</td>
</tr>
<tr>
<td><b>Navigation</b></td>
<td>Compose Navigation</td>
</tr>
<tr>
<td><b>Database</b></td>
<td>Room</td>
</tr>
<tr>
<td><b>Preferences</b></td>
<td>DataStore</td>
</tr>
<tr>
<td><b>Concurrency</b></td>
<td>Kotlin Coroutines + Flow</td>
</tr>
<tr>
<td><b>Background Tasks</b></td>
<td>WorkManager</td>
</tr>
<tr>
<td><b>Testing</b></td>
<td>JUnit, MockK, Espresso</td>
</tr>
</table>

---

## How to Use

New to DoTrack? Check out the **[How to Use Guide](HOW_TO_USE.md)** — create tasks, set reminders, customize your wallpaper, and more in minutes.

---

## Project Structure

<details>
<summary><b>Detailed File Structure</b></summary>

```
DoTrack/
├── app/src/main/java/com/shreyash/dotrack/
│   ├── ui/
│   │   ├── tasks/
│   │   │   ├── TasksScreen.kt
│   │   │   ├── TasksViewModel.kt
│   │   │   ├── TaskDetailScreen.kt
│   │   │   ├── TaskDetailViewModel.kt
│   │   │   ├── TaskFilterBar.kt
│   │   │   ├── SelectableOptionItem.kt
│   │   │   ├── SortDropdownMenu.kt
│   │   │   ├── FilterBottomSheet.kt
│   │   │   └── addedit/
│   │   │       ├── AddEditTaskScreen.kt
│   │   │       └── AddEditTaskViewModel.kt
│   │   ├── categories/
│   │   │   ├── CategoriesScreen.kt
│   │   │   └── CategoriesViewModel.kt
│   │   └── settings/
│   │       ├── SettingsScreen.kt
│   │       └── SettingsViewModel.kt
│   ├── navigation/
│   │   ├── DoTrackNavHost.kt
│   │   ├── DoTrackDestinations.kt
│   │   └── DoTrackBottomNavigation.kt
│   ├── workmanager/
│   │   ├── ReminderSchedulerImpl.kt
│   │   └── ReminderModule.kt
│   ├── core/util/
│   │   └── WallpaperGenerator.kt
│   ├── DoTrackApplication.kt
│   └── MainActivity.kt
│
├── domain/src/main/java/com/shreyash/dotrack/domain/
│   ├── model/
│   │   ├── Task.kt
│   │   └── Category.kt
│   ├── repository/
│   │   ├── TaskRepository.kt
│   │   ├── CategoryRepository.kt
│   │   └── UserPreferencesRepository.kt
│   └── usecase/
│       ├── task/
│       │   ├── AddTaskUseCase.kt
│       │   ├── GetTasksUseCase.kt
│       │   ├── UpdateTaskUseCase.kt
│       │   └── DeleteTaskUseCase.kt
│       ├── category/
│       │   └── [Category Use Cases]
│       └── preferences/
│           └── [Preference Use Cases]
│
├── data/src/main/java/com/shreyash/dotrack/data/
│   ├── local/
│   │   ├── entity/
│   │   │   ├── TaskEntity.kt
│   │   │   └── CategoryEntity.kt
│   │   ├── dao/
│   │   │   ├── TaskDao.kt
│   │   │   └── CategoryDao.kt
│   │   └── TaskDatabase.kt
│   ├── repository/
│   │   ├── TaskRepositoryImpl.kt
│   │   ├── CategoryRepositoryImpl.kt
│   │   └── UserPreferencesRepositoryImpl.kt
│   └── di/
│       ├── DatabaseModule.kt
│       ├── RepositoryModule.kt
│       └── DataStoreModule.kt
│
└── core/src/main/java/com/shreyash/dotrack/core/
    ├── ui/
    │   ├── components/
    │   │   └── LoadingIndicator.kt
    │   └── theme/
    │       ├── Color.kt
    │       ├── Theme.kt
    │       └── Type.kt
    └── util/
        └── Result.kt
```

</details>

---

## CI/CD Pipeline

DoTrack uses **GitHub Actions** for manual deployment (triggered via GitHub UI — no automated PR/push builds):

- **Build APK** — manually triggered signed APK build
- **Build & Deploy AAB** — build, version-bump, and deploy to Google Play (internal testing, beta, or production)
- **Version Management** — auto-increment version code and name on each deploy
- **Release Tags** — automatically created for production deployments

---

## Testing

### Running Tests

```bash
# Unit Tests
./gradlew test

# Instrumented Tests
./gradlew connectedAndroidTest

# All Tests
./gradlew check
```

### Test Coverage

- **Unit Tests**: Domain layer business logic
- **Integration Tests**: Repository implementations
- **UI Tests**: Compose UI components
- **End-to-End Tests**: Complete user workflows

---

## Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details.

### Development Workflow

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Code Style

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use [ktlint](https://ktlint.github.io/) for formatting
- Write meaningful commit messages
- Add tests for new features

---

## Documentation

| Document | Description |
|----------|-------------|
| [Quick Start](QUICK_START.md) | Setup and first run |
| [How to Use](HOW_TO_USE.md) | Step-by-step usage guide |
| [User Guide](docs/USER_GUIDE.md) | Complete user manual |
| [Developer Guide](docs/DEVELOPER_GUIDE.md) | Development setup and guidelines |
| [API Documentation](docs/DOCUMENTATION.md) | Code documentation |
| [Workflow Guide](docs/WORKFLOW.md) | CI/CD and release process |
| [Contributing](CONTRIBUTING.md) | Contribution guidelines |
| [Changelog](CHANGELOG.md) | Release history |
| [Security](SECURITY.md) | Security policy and vulnerability reporting |

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- **Material Design 3** for the beautiful UI components
- **Android Jetpack** for the robust architecture components
- **Kotlin Coroutines** for seamless asynchronous programming
- **Open Source Community** for the amazing libraries and tools

---
## 🚀 Get it on Google Play

<div align="center">

<a href="https://play.google.com/store/apps/details?id=com.shreyash.dotrack">
  <img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" alt="Get it on Google Play" width="200"/>
</a>

</div>

**Join the Open Testing Program** to try the latest features before everyone else — open the [Play Store listing](https://play.google.com/store/apps/details?id=com.shreyash.dotrack), tap **"Join the beta"**, no code, no sideloading. Report issues via [GitHub](https://github.com/shreyashp47/DoTrack/issues). Your feedback makes DoTrack better! 💚

---

## 📞 Support & Contact

<div align="center">

**Need help or have suggestions?**

[![Email](https://img.shields.io/badge/Email-support%40dotrack.app-red?style=for-the-badge&logo=gmail)](shreyashp47@gmail.com)
[![GitHub Issues](https://img.shields.io/badge/GitHub-Issues-black?style=for-the-badge&logo=github)](https://github.com/shreyashp47/DoTrack/issues)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-@DoTrackApp-blue?style=for-the-badge&logo=linkedin)](https://www.linkedin.com/in/shreyashpattewardeveloper/)
[![LinkedIn Post](https://img.shields.io/badge/LinkedIn-DoTrack%20Launch-0A66C2?style=for-the-badge&logo=linkedin)](https://www.linkedin.com/posts/shreyashpattewardeveloper_androiddevelopment-kotlin-opensource-activity-7489967756902055926-dqLD?utm_source=social_share_send&utm_medium=member_desktop_web&rcm=ACoAABzBY68BS8qwG4Ns2vV82zF0DpF0GHnrV1k)

**⭐ Star this repo if you find it helpful!**

</div>

---

<div align="center">

**Made with ❤️ by [Shreyash](https://github.com/shreyashp47)**

*DoTrack - Transform your productivity, one task at a time* ✨

</div>
