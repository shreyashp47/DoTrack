# Security Policy

## Reporting a Vulnerability

If you discover a security vulnerability in DoTrack, please **do not open a public issue**. Instead, report it privately so it can be addressed before disclosure.

**How to report:**

- **Email:** [shreyashp47@gmail.com](mailto:shreyashp47@gmail.com)
- **GitHub Security Advisories:** [Report a vulnerability](https://github.com/shreyashp47/DoTrack/security/advisories/new) (preferred)

Please include the following details in your report:

1. Type of vulnerability (e.g., code execution, information disclosure, etc.).
2. Full paths of source file(s) related to the issue.
3. Step-by-step reproduction instructions.
4. Impact and any suggested mitigation, if known.

You should receive a response within **72 hours**. If you don't, please follow up.

## Disclosure Policy

- Vulnerabilities are fixed privately and released as soon as a patch is ready.
- Security fixes are noted in the [CHANGELOG](CHANGELOG.md) release notes.
- Public disclosure happens after the fix is available to users.

## Supported Versions

| Version | Supported          |
| ------- | ------------------ |
| Latest release | ✅ Supported |
| Older releases | ❌ Not supported |

Only the latest release receives security updates. Please update to the newest version from the [Releases page](https://github.com/shreyashp47/DoTrack/releases) or the [Google Play Store](https://play.google.com/store/apps/details?id=com.shreyash.dotrack).

## Security Best Practices for Developers

- Never commit secrets, API keys, or keystore passwords to the repository.
- CI/CD credentials are injected as GitHub Actions secrets only (`ANDROID_KEYSTORE_BASE64`, etc.) — never hardcode them in workflows.
- Keep dependencies up to date via `gradle/libs.versions.toml`; run `./gradlew dependencyUpdates` and Gradle's dependency verification periodically.
- Run the lint check included in the release workflow before deploying.
