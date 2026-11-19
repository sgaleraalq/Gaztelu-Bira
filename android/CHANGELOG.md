# Changelog

All notable changes to the Gaztelu Bira Android app are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.1] - 2026-11-19

### Fixed
- Google sign-in no longer hangs on an endless loading state on devices with many Google
  accounts: it now uses the "Sign in with Google" button flow instead of the system credential
  selector.
- Splash, match detail, insert player and insert team screens respect the scaffold padding again,
  so content no longer sits under the top app bar or the navigation bar.

### Removed
- Advertising ID permissions merged in by Firebase Analytics; the app shows no ads.

### Added
- Privacy policy hosted on Firebase Hosting (`client/hosting`).
- Play Store screenshots (`android/screenshots/playstore`).

## [1.0.0] - 2026-11-18

- First public release

[1.0.1]: https://github.com/sgaleraalq/Gaztelu-Bira/compare/android/v1.0.0...android/v1.0.1
[1.0.0]: https://github.com/sgaleraalq/Gaztelu-Bira/releases/tag/android/v1.0.0
