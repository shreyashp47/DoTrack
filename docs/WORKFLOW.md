# Build Bundle Workflow Documentation

This document explains the GitHub Actions workflow defined in `.github/workflows/build_bundle.yml` that handles building and deploying the DoTrack app to Google Play Store.

## Workflow Overview

The `Build and Deploy to Play Store` workflow automates the process of building an Android App Bundle (AAB), versioning, signing, and deploying the app to Google Play Store.

## Workflow Trigger

The workflow is triggered manually using the `workflow_dispatch` event with two input parameters:

- **deploymentType**: Specifies the deployment target
  - Options: `internal` (default) or `live`
  - Internal deploys to Google Play internal testing track
  - Live deploys to Google Play production track

- **branch**: Specifies which branch to build from
  - Default: `main`

## Workflow Steps

### 1. Environment Setup

- **Checkout Code**: Fetches the specified branch with complete history
- **Set up JDK**: Configures Java 17 (Temurin distribution)
- **Grant execute permission**: Makes gradlew executable

### 2. Version Management

- **Get latest version**: 
  - Extracts current version name and code from build.gradle.kts
  - Automatically increments the version code
  - Increments the minor version of the version name (e.g., 1.09 → 1.10)
  - Outputs both current and new versions

- **Update version in build.gradle.kts**:
  - Updates the version name and code in the build file

### 3. Keystore and Signing Setup

- **Setup keystore**: Creates necessary directory structure
- **Decode and setup keystore**: Decodes the Base64 encoded keystore from GitHub Secrets

### 4. Build Process

- **Build AAB**: 
  - Runs `./gradlew bundleRelease`
  - Uses environment variables for keystore credentials
  
- **Rename AAB file**: 
  - Renames the output file to include version information
  - Format: `DoTrack-v{version}-release.aab`

### 5. Deployment

- **Upload to Google Play Store**:
  - Uses the r0adkll/upload-google-play action
  - Deploys to internal or production track based on input parameter
  - Sets release status to 'completed'
  - Includes what's new information from the whatsnew/ directory
  
- **Upload AAB Artifact**: 
  - Saves the AAB file as a workflow artifact for reference

### 6. Release Management

- **Create Release Tag**:
  - Only for 'live' deployments
  - Creates and pushes a new Git tag for the release version
  
- **Update version in repository**:
  - Commits and pushes the updated build.gradle.kts with new version numbers
  - Ensures version numbers are always up-to-date in the repository

## Secret Requirements

The workflow requires these GitHub Secrets:

- `ANDROID_KEYSTORE_BASE64`: Base64-encoded Android keystore file
- `ANDROID_KEYSTORE_PASSWORD`: Password for the keystore
- `ANDROID_KEY_ALIAS`: Key alias in the keystore
- `ANDROID_KEY_PASSWORD`: Password for the key alias
- `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON`: Google Play service account JSON key file (plain text)

## Usage

To trigger the workflow:

1. Go to the Actions tab in the GitHub repository
2. Select "Build and Deploy to Play Store" workflow
3. Click "Run workflow"
4. Select deployment type (internal or live)
5. Specify the branch (typically main)
6. Click "Run workflow"

The workflow will build the app, increment the version, and deploy it to the specified Google Play track.