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

[Quick Start](QUICK_START.md) • [How to Use](HOW_TO_USE.md) • [Changelog](CHANGELOG.md) • [Report an Issue](https://github.com/shreyashp47/DoTrack/issues)

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

## Getting Started

- **[Quick Start](QUICK_START.md)** — prerequisites, setup, install, and downloading the APK
- **[How to Use](HOW_TO_USE.md)** — create tasks, set reminders, and customize your wallpaper

---

## Architecture

DoTrack follows **Clean Architecture** principles with a multi-module approach:

```
DoTrack
├── app/                    # Presentation Layer (UI, ViewModels, Navigation, WorkManager, Widgets)
├── domain/                 # Business Logic Layer (models, repositories, use cases)
├── data/                   # Data Layer (Room, DataStore, repository implementations, DI)
└── core/                   # Shared Components (theme, DispatcherModule, utilities)
```

<details>
<summary><b>Tech Stack</b></summary>

| Layer | Technology |
|-------|------------|
| Architecture | MVVM + Clean Architecture |
| UI Framework | Jetpack Compose + Material 3 |
| Dependency Injection | Hilt |
| Navigation | Compose Navigation |
| Database | Room |
| Preferences | DataStore |
| Concurrency | Kotlin Coroutines + Flow |
| Background Tasks | WorkManager |
| Testing | JUnit, MockK, Espresso |

</details>

---

## Development

- **CI/CD** — GitHub Actions (manually triggered) for building signed APKs/AABs and deploying to Google Play, with auto versioning and release tags
- **Testing** — unit tests (`./gradlew test`) and instrumented tests (`./gradlew connectedAndroidTest`) with `./gradlew check` for all
- **See the [Developer Guide](docs/DEVELOPER_GUIDE.md)** for setup, workflows, and code conventions

---

## Contributing

We welcome contributions! Please see our **[Contributing Guidelines](CONTRIBUTING.md)**.

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes
4. **Push** to the branch
5. **Open** a Pull Request

---

## Documentation

| Document | Description |
|----------|-------------|
| [Quick Start](QUICK_START.md) | Setup and first run |
| [How to Use](HOW_TO_USE.md) | Step-by-step usage guide |
| [User Guide](docs/USER_GUIDE.md) | Complete user manual |
| [Developer Guide](docs/DEVELOPER_GUIDE.md) | Development setup and guidelines |
| [Workflow Guide](docs/WORKFLOW.md) | The CI/CD and release process |
| [Changelog](CHANGELOG.md) | Release history |
| [Security](SECURITY.md) | Security policy and vulnerability reporting |

---

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file.

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

Join the **Open Testing Program** to get the latest features before everyone else — open the [Play Store listing](https://play.google.com/store/apps/details?id=com.shreyash.dotrack) and tap **"Join the beta"**. Your feedback makes DoTrack better! 💚

---

## 📞 Support & Contact

<div align="center">

**Have questions, feedback, or feature ideas?**

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