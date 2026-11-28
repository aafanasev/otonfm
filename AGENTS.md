# AGENTS.md

This file provides guidance to coding agents, such as Claude Code and Codex, when working with code in this repository. `CLAUDE.md` imports it.

## Overview

OTON FM is the Android app for a live radio station, built with Jetpack Compose, Navigation 3, Media3, and Firebase. It is a single Gradle module (`:app`) with the root package `net.afanasev.otonfm`.

## Commands

```bash
./gradlew :app:assembleDebug        # build the debug APK
./gradlew :app:installDebug         # install on a connected device
./gradlew :app:testDebugUnitTest    # run all unit tests
./gradlew :app:lintDebug            # run lint
./gradlew :app:minifyReleaseWithR8  # check the R8 release build without signing an APK

# Run one test class, or one test by its backticked name
./gradlew :app:testDebugUnitTest --tests 'net.afanasev.otonfm.player.fsm.PlayerReducerTest'
./gradlew :app:testDebugUnitTest --tests '*PlayerReducerTest.starts Idle*'

# Log every player state transition to logcat under the RadioPlayer tag
adb shell setprop log.tag.RadioPlayer DEBUG
```

`lintDebug` currently fails on one existing error, `MissingIntentFilterForMediaSearch` in the manifest.

## Architecture

### Package dependencies

Top-level packages are independent features. Keep dependencies pointing this way:

```
                 MainActivity + MainScreen + MainRoutes
                                 │
  adminstatus  menu  player  chat ─▶ auth  theme  contacts  notifications
                                 │
                              common
```

- Features must not import each other. The only exception is `chat` → `auth.data`, for `UserModel` and the `users` collection.
- `MainActivity` owns navigation and connects features through callbacks. `MainScreen` assembles the home screen from the top bar, player, and chat button.
- Features must not import `MainActivity` or `MainRoutes`. Services open the app with `getLaunchIntentForPackage`.
- `common` must not import any feature.

### Player

Playback is a state machine in `player/fsm`, documented in `docs/player-fsm.png`. Update that diagram when transitions change.

```
PlayerViewModel, PlaybackService ──▶ RadioPlayer ──▶ reduce() ──▶ new PlayerState + effects
                                          ▲                                 │
                                          └── events ◀── PlayerEffectHandler ┘
```

- `reduce()` is pure, with no I/O, clocks, or Android calls. All playback decisions belong there, and side effects are returned as `PlayerEffect`s.
- `RadioPlayer` is an app-scoped singleton that processes events one at a time on the main thread. It outlives the UI so that notification, Bluetooth, and Android Auto changes are tracked with no screen alive.
- `PlaybackService` owns the ExoPlayer and attaches it to `RadioPlayer`. `PlayerViewModel` starts the service lazily on the first play, and player commands issued before then wait in `PlayerEffectHandler`.
- The order of calls in `StartPlayback`, `StopPlayback`, and `StopForRetry` is deliberate. The reducer relies on `playWhenReady` to tell its own actions from an outside pause, so read the comments in `PlayerEffect.kt` before changing them.
- `PlayerButtonState` is a UI simplification of `Playback` and lives in `PlayButton.kt`. The FSM must not depend on it. Keep the mapping in `PlayerViewModel`, because its `StateFlow` drops equal values and prevents needless recomposition.
- The stream's media item sets only the artist (the station name), never a title. Media3 gives media item metadata priority over stream metadata, so a fixed title would stop the track name from updating.

### Artwork

- The screens pass `null` when there's no artwork. Coil's `fallback` and `error` images show the bundled `default_artwork` drawable, for both missing and failed artwork.
- Only Media3 (the notification, lock screen, and Android Auto) needs the default as a URI, so `DEFAULT_ARTWORK_URI` lives in `PlaybackService`. Media3 loads artwork itself and doesn't get Coil's fallback for failed downloads.
- The default artwork is a PNG, not the vector logo, because Media3 needs a bitmap it can decode.
- Build resource URIs from the resource id (`android.resource://<applicationId>/<resId>`). Coil 3 can't load the `.../drawable/<name>` form.

### Firebase

- Firestore collections are read and written directly by the repositories in each feature's `data` package.
- Firestore models need default values. Boolean fields named `isX` need `@JvmField` so they keep that field name in Firestore.
- Every Firestore model needs a `-keepclassmembers` rule in `app/proguard-rules.pro`, or it breaks only in release builds.
- The pinned message query needs a Firestore composite index on `messages`: `isPinned` ascending, `createdAt` descending.
- `@Secret` from the sekret plugin marks fields to hide from generated `toString()` output.

### Analytics

All analytics events go through `common/log/Logger`. Release builds obfuscate class names, so never log `::class.simpleName`. The `state` value of `play_btn_click` is `Playback.toString()`, so renaming a `Playback` state or its fields changes reported analytics values.

## UI

- Shared components such as `Pill` and `MarqueeText` live in `common/ui/components`. Reuse them instead of building another capsule or scrolling text.
- Use theme colors and `MaterialTheme.typography` styles, not hard-coded colors or `sp` sizes. Screens change only the font weight of a style.
- The app font is Montserrat (`common/ui/style/Type.kt`). A replacement font must cover the Sakha letters ҕ ҥ һ ө ү ӈ, as well as Russian.

## Strings

The default locale (`res/values`) is a mix of Russian and Sakha, and English is in `res/values-en`. Add user-facing strings to both.

Uppercase translated text with the app's locale (`toUpperCase(Locale.current)`), not with locale-free uppercasing.

## Code style

Don't add extension functions to global types such as `String`, `Int`, or `List`; they show up in autocomplete everywhere. Put the logic on the app's own type instead, such as a companion factory `TrackTitle.parse(title)` rather than `String.toTrackTitle()`. Extensions on the app's own types, like `Playback.toButtonState()`, are fine.

## Commits and pull requests

- Each commit should compile on its own.
- A PR description includes a test plan that separates the automated checks run from the device checks, and leaves unchecked anything not yet tested.
