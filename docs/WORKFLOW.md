# Build Workflow Documentation

## Build APK Workflow

Defined in `.github/workflows/build_apk.yml` — manually triggered build of a signed APK.

### Trigger

`workflow_dispatch` with three inputs:
- **versionName** — version label (default `1.20`)
- **versionCode** — version code (default `20`)
- **branch** — branch to build (default `main`)

### Steps

1. **Checkout Code** — fetches the specified branch
2. **Set up JDK** — Java 17 (Temurin)
3. **Grant execute permission** — `chmod +x gradlew`
4. **Cache Gradle dependencies** — caches `~/.gradle/caches` and `~/.gradle/wrapper` for faster subsequent runs
5. **Inject versionName/versionCode** — `sed` updates values in `app/build.gradle.kts`
6. **Build APK** — `./gradlew assembleRelease`
7. **Rename APK file** — renames to `DoTrack-v{versionName}-release.apk`
8. **Upload APK Artifact** — saves as a workflow artifact

---

## Build and Deploy to Play Store Workflow

Defined in `.github/workflows/build_bundle.yml` — manually triggered build, sign, and deploy of an Android App Bundle.

### Trigger

`workflow_dispatch` with two inputs:
- **deploymentType** — one of `internal`, `PublicTrack`, `beta`, `production` (default `PublicTrack`)
- **branch** — branch to build (default `main`)

### Steps

#### 1. Environment Setup
- **Checkout Code** — fetches the specified branch
- **Set up JDK** — Java 17 (Temurin)
- **Grant execute permission** — `chmod +x gradlew`
- **Cache Gradle dependencies** — caches `~/.gradle/caches` and `~/.gradle/wrapper`

#### 2. Version Management
- **Read current version from build.gradle.kts** — extracts current version name and code, auto-increments both (version code +1, minor version +1)
- **Update version in build.gradle.kts** — applies the new version values via `sed`

#### 3. Keystore Setup
- **Decode and setup keystore** — decodes `ANDROID_KEYSTORE_BASE64` secret into `keystore_details/release.keystore`

#### 4. Build Process
- **Run unit tests** — `./gradlew test` before building release
- **Build AAB** — `./gradlew bundleRelease` with keystore credentials from environment variables
- **Rename AAB file** — renames to `DoTrack-v{newVersionName}-release.aab`

#### 5. Deployment
- **Upload to Google Play Store** — uses `r0adkll/upload-google-play` to the selected track, with release notes from `whatsnew/` directory
- **Upload AAB Artifact** — saves as a workflow artifact

#### 6. Release Management
- **Create Release Tag** — creates and pushes a Git tag `v{newVersionName}` (only for `production` deployments)
- **Update version in repository** — commits and pushes the updated `app/build.gradle.kts` with bumped version

### Required Secrets

| Secret | Description |
|--------|-------------|
| `ANDROID_KEYSTORE_BASE64` | Base64-encoded Android keystore |
| `ANDROID_KEYSTORE_PASSWORD` | Keystore password |
| `ANDROID_KEY_ALIAS` | Key alias |
| `ANDROID_KEY_PASSWORD` | Key password |
| `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON` | Google Play service account JSON (plain text) |

### Usage

1. Go to **Actions** tab in the GitHub repository
2. Select **"Build and Deploy to Play Store"**
3. Click **"Run workflow"**
4. Choose deployment type and branch
5. Click **"Run workflow"**

The workflow will build, version-bump, and deploy to the selected Google Play track.
