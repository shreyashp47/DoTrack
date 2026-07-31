# Contributing to DoTrack

First off, thank you for taking the time to contribute! 🎉

DoTrack is a community-driven, open-source project. Contributions — bug reports, feature requests, docs, or code — are always welcome.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [How to Contribute](#how-to-contribute)
- [Development Workflow](#development-workflow)
- [Code Style](#code-style)
- [Testing](#testing)
- [Pull Request Guidelines](#pull-request-guidelines)
- [Release Process](#release-process)

## Code of Conduct

By participating in this project, you agree to treat everyone with respect and maintain a welcoming, inclusive environment. Harassment, trolling, and offensive behavior of any kind will not be tolerated.

## Getting Started

### Prerequisites

- **Android Studio**: Meerkat | 2024.3.1 or newer
- **JDK**: 11 or higher
- **Android SDK**: API 36 (compileSdk)
- **Minimum supported Android**: API 26 (Android 8.0)

### Development Setup

1. **Fork the repository** on GitHub
2. **Clone your fork:**

   ```bash
   git clone https://github.com/<your-username>/DoTrack.git
   cd DoTrack
   ```

3. **Add the upstream remote:**

   ```bash
   git remote add upstream https://github.com/shreyashp47/DoTrack.git
   ```

4. **Open the project in Android Studio** and let Gradle sync. If you prefer the command line:

   ```bash
   ./gradlew assembleDebug
   ```

   > **Note:** Release builds require a keystore (`keystore_details/keystore.properties` or the `ANDROID_KEYSTORE_*` environment variables). Debug builds work out of the box.

## How to Contribute

### Reporting Bugs

Before opening an issue, please:

1. **Search** existing [issues](https://github.com/shreyashp47/DoTrack/issues) to avoid duplicates.
2. Use the **Bug Report** template and include:
   - Steps to reproduce
   - Expected vs. actual behavior
   - Device / OS / app version
   - Screenshots or screen recordings, if applicable

### Requesting Features

1. Search existing issues and [discussions](https://github.com/shreyashp47/DoTrack/discussions) first.
2. Open a **Feature Request** describing the problem you want to solve and your proposed solution.

### Documentation

Docs live in the [`docs/`](docs/) directory and the [wiki-style guides](README.md#documentation). Fixes and improvements to `README.md`, `docs/*.md`, and inline KDoc are always appreciated.

## Development Workflow

1. **Sync your fork with upstream:**

   ```bash
   git checkout main
   git pull upstream main
   ```

2. **Create a feature branch** (use a descriptive name):

   ```bash
   git checkout -b feat/your-feature-name
   # or for fixes:
   git checkout -b fix/your-fix-name
   ```

3. **Make your changes**, keeping them focused on the issue at hand.
4. **Write or update tests** — see [Testing](#testing).
5. **Verify the build passes** locally:

   ```bash
   ./gradlew test assembleDebug
   ```

6. **Commit your changes** with a meaningful message:

   ```bash
   git commit -m "Add ability to set custom reminder times"
   ```

7. **Push and open a Pull Request:**

   ```bash
   git push origin feat/your-feature-name
   ```

   Open a PR against the `main` branch and fill in the PR template.

## Code Style

- Follow the official [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).
- The project enforces ktlint via Gradle — run it before committing:

  ```bash
  ./gradlew ktlintCheck
  ```

- Follow the existing patterns:
  - **Architecture**: MVVM + Clean Architecture. UI in `app`, business logic in `domain`, data sources in `data`, shared components in `core`.
  - **Dependency Injection**: Use Hilt (`@HiltViewModel`, `@Inject`, `@AndroidEntryPoint`).
  - **Concurrency**: Kotlin Coroutines + Flow; inject dispatchers via `DispatcherModule` (`@IoDispatcher`, `@MainDispatcher`, `@DefaultDispatcher`).
  - **Result handling**: Use the project's custom `Result<T>` sealed class from the `core` module.
- Write meaningful commit messages following [Conventional Commits](https://www.conventionalcommits.org/):
  - `feat:`, `fix:`, `docs:`, `refactor:`, `test:`, `chore:`
- Add tests for new functionality.

## Testing

All new features and bug fixes must be covered by tests where practical.

```bash
# Unit tests (JUnit + MockK + Turbine)
./gradlew test

# Instrumented tests (Compose UI) — requires an emulator/device
./gradlew connectedAndroidTest

# Run a single test class
./gradlew connectedAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=com.shreyash.dotrack.ui.tasks.QuickTaskTest
```

See [TEST_GUIDE.md](TEST_GUIDE.md) and [docs/DEVELOPER_GUIDE.md](docs/DEVELOPER_GUIDE.md) for details.

## Pull Request Guidelines

- Target the `main` branch.
- Keep PRs small and focused — one logical change per PR.
- Reference the issue your PR closes (e.g., `Closes #42`).
- Ensure **all checks pass** (unit tests, lint, ktlint, build).
- Update the relevant docs if your change affects usage or architecture.
- For UI changes, include screenshots or recordings.

## Release Process

Releases are managed via GitHub Actions (see [docs/WORKFLOW.md](docs/WORKFLOW.md)):

- **`build_apk.yml`** — manual signed APK build.
- **`build_bundle.yml`** — auto-increments version, runs tests, and deploys to Google Play (internal/beta/production).
- **`deploy.yml`** — auto-deploy on PR merge to `main` (beta track).

Version bumps and release notes are handled automatically by the workflows — no manual version edits needed.

---

## Questions?

Open a [discussion](https://github.com/shreyashp47/DoTrack/discussions) or reach out via the [support channels](README.md#-support--contact).

Thank you for helping make DoTrack better! ❤️
