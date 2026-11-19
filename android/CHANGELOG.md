# Changelog

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
