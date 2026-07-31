# Changelog

All notable changes to DoTrack are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html). Version bumps are applied automatically by the CI/CD pipeline ([docs/WORKFLOW.md](docs/WORKFLOW.md)).

## [Unreleased]

### Changed
- Local `gradle.properties` memory setting adjusted for development environments.

## [1.29] - 2026-07-31

### Added
- Play Store release notes are now uploaded correctly during deployment.

### Changed
- Optimized performance, removed dead dependencies, upgraded to AGP 9.
- Added Room database v4 migration.

## [1.28] - 2026-07-28

### Fixed
- Bug fixes and stability improvements.

## [1.27] - 2026-07-15

### Added
- Dark mode support.
- Sort/filter bottom sheets with improved UI/UX.

### Fixed
- Date picker showing the previous day.
- Play Store recommendations: edge-to-edge layout, deprecated APIs removed, R8 full mode enabled.
- Minimum-duration loading overlay for delete operations.
- Clear-completed dialog text.

### Changed
- Lint check added to `build_bundle` workflow for production readiness.

## [1.26] - 2026-07-15

### Changed
- Polish of Settings and Task Detail screens.
- String resources extracted.
- Time picker fixes.

## [1.25] - 2026-07-14

### Added
- Auto-deploy workflow: PR merge to `main` deploys to Google Play beta track.

## [1.24] - 2026-07-01

### Fixed
- Notification icon issue (#84).
- Widget DI bypass (#73).
- Destructive migration fallback (#74).
- Category ID handling (#72).

## [1.23] - 2026-06-24

### Changed
- Documentation updates.

## [1.22] - 2026-06-22

### Changed
- Improved CI workflows and documentation.

## [1.21] - 2026-06-22

### Fixed
- High/medium severity issues: unsafe operators, coroutine leaks.
- Add/edit category stub.
- Duplicate classes and dead code.
- Per-item ViewModel allocation.
- Test flakiness.
- Dark-mode wallpaper colors.

### Changed
- Migrated dependencies to version catalog (`gradle/libs.versions.toml`).
- Added backup rules.

---

## [1.13] - 2025-07-27 and earlier

Initial development and early releases, including:

- Core task management (create, edit, complete, delete).
- Dynamic wallpaper generation with priority colors.
- Reminders via WorkManager (30 minutes before due).
- Categories, DataStore preferences, and Compose Material 3 UI.
- GitHub Actions CI/CD pipeline.

[Unreleased]: https://github.com/shreyashp47/DoTrack/compare/1.29...HEAD
[1.29]: https://github.com/shreyashp47/DoTrack/releases/tag/1.29
