# DoTrack 📅

A **modern task management app** built with **Jetpack Compose** and **Clean Architecture** that transforms your device wallpaper into a dynamic task list.

![DoTrack Wallpaper Preview](https://github.com/shreyashp47/DoTrack/blob/main/SS/Screenshot_20250608_093813.png)

---

## 🌟 Features

* ✅ **Task Management**: Create, edit, complete, and delete tasks
* ⭐ **Priority Levels**: Assign High, Medium, Low priority
* ⏰ **Due Dates**: Set due dates to stay on schedule
* 🎨 **Dynamic Wallpaper**: Update wallpaper with pending tasks
* 🌈 **Customizable Themes**: Pick your favorite color themes
* ⚙️ **User Preferences**: Enable/disable auto wallpaper updates

---

## 📊 Architecture Overview

DoTrack follows **Clean Architecture** with clear separation of concerns:

### 1. **Presentation Layer**

* Jetpack Compose UI
* ViewModels with `StateFlow`
* Navigation using Compose Navigation

### 2. **Domain Layer**

* Business logic via use cases
* Domain models
* Repository interfaces

### 3. **Data Layer**

* Room Database
* DataStore Preferences
* Repository implementations
* Entity <-> Model mappers

---

## 📓 Project Structure

```
DoTrack/
├── app/
│   ├── core/
│   │   └── util/
│   │       ├── SwitchIcon.kt
│   │       └── WallpaperGenerator.kt
│   ├── navigation/
│   │   ├── DoTrackNavHost.kt
│   │   └── ...
│   ├── ui/
│   │   ├── tasks/
│   │   ├── categories/
│   │   └── settings/
│   └── workmanager/
├── core/
│   ├── di/
│   ├── ui/
│   └── util/
├── data/
│   ├── di/
│   ├── local/
│   ├── repository/
│   └── util/
├── domain/
│   ├── model/
│   ├── repository/
│   ├── usecase/
│   └── ReminderScheduler.kt
```

---

## ⚙️ How to Use This Project

### Prerequisites

* Android Studio **Meerkat | 2024.3.1** or newer
* JDK 11
* Android SDK 35
* Minimum SDK: 26

### Setup Steps

1. Clone the repository:

   ```bash
   git clone https://github.com/shreyashp47/DoTrack.git
   ```
2. Open in **Android Studio**
3. Let Gradle sync
4. Run on **emulator** or **physical device**

---

## 🚀 CI/CD Pipeline

Powered by **GitHub Actions**:

* 📅 Build triggered on PRs and pushes to `main`
* 💊 Auto versioning
* 🚀 Signed AAB builds
* 🌐 Deploys to **Google Play (Internal + Production)**
* ⚖️ Release tagging & metadata updates

---

## 👤 Usage Guide

### 📅 Creating Tasks

1. Tap `+` on the Tasks screen
2. Enter title, description, priority, due date
3. Tap `Save`

### 🏒 Managing Tasks

* Swipe to complete/uncomplete
* Tap to view/edit
* Long press for options

### 🎨 Customizing Wallpaper

1. Open **Settings**
2. Enable/Disable "Auto Wallpaper Updates"
3. Choose preferred color theme
4. Wallpaper updates automatically on changes

---

## 🖼️ Screenshots

![Task List](https://github.com/shreyashp47/DoTrack/blob/main/SS/Screenshot_20250608_093726.png)
![Add Task](https://github.com/shreyashp47/DoTrack/blob/main/SS/Screenshot_20250608_092713.png)
![Settings](https://github.com/shreyashp47/DoTrack/blob/main/SS/Screenshot_20250608_095253.png)
![Wallpaper Preview](https://github.com/shreyashp47/DoTrack/blob/main/SS/Screenshot_20250608_093813.png)

---

## 🎨 Open Graph Image (for GitHub preview)

Add the following meta tags to your repo's `README.md` (use `raw.githubusercontent.com` for raw images):

```md
![DoTrack OG](https://raw.githubusercontent.com/shreyashp47/DoTrack/main/SS/DoTrack_OG.png)
```

Or for better preview:

```html
<meta property="og:image" content="https://raw.githubusercontent.com/shreyashp47/DoTrack/main/SS/DoTrack_OG.png" />
<meta name="twitter:card" content="summary_large_image" />
```

---

## ✨ Contributions

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

---

## ✉️ Contact

Feel free to reach out on [LinkedIn](https://www.linkedin.com/in/shreyashp47/) or raise an issue.

---

## 🌍 License

[MIT License](https://opensource.org/licenses/MIT)

---

> Designed & built with ❤️ by [Shreyash P.](https://github.com/shreyashp47)
