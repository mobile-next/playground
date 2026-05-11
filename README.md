# mobilewright-demo

A playground app for [Mobilewright](https://mobilewright.dev) — available for both iOS and Android. It exercises the full range of UI components, web view integration, and accessibility attributes so you can see exactly how Mobilewright behaves across platforms.

## Structure

```
mobilewright-demo/
├── ios/        # SwiftUI app (Xcode project)
└── android/    # Kotlin app (Gradle project)
```

Both implementations are feature-equivalent: same screens, same components, same accessibility labels.

## Screens

### Home
Displays the Mobilewright logo and navigation links to the two demo screens.

### Basic UI
A scrollable showcase of native components:

| Category | Components |
|---|---|
| Text input | Text field, password field, multiline editor |
| Controls | Toggle switch, stepper (0–100), reset button |
| Slider | 0.0–1.0 range with live value display |
| Pickers | Dropdown picker + segmented control (3 options each) |
| Date picker | Native date selector with formatted display |
| Progress | Horizontal progress bar + activity indicator |
| Labels | Icon label, plain label, tappable hyperlink |
| Dialogs | Alert dialog, confirmation dialog, contextual menu |
| Share / Photos | Share sheet + photo picker |

Every element carries an accessibility identifier (iOS) or content description (Android), making the full screen testable with Mobilewright out of the box.

### Web View
Loads `https://mobilewright.dev` inside a native `WKWebView` (iOS) / `WebView` (Android) with JavaScript and DOM storage enabled.

## Building

### iOS

Requirements: Xcode 15+, iOS 17+ simulator or device.

```
open ios/mobilewright-demo.xcodeproj
```

Select a simulator or device and press Run (⌘R).

### Android

Requirements: Android Studio Hedgehog+, min SDK 26, compile SDK 36.

```
cd android
./gradlew assembleDebug
```

Or open the `android/` folder in Android Studio and run the `app` configuration.

## License

Apache 2.0 — see [LICENSE](LICENSE).

## Purpose

This app is the reference target used when developing and testing Mobilewright. If a UI pattern works here, it works in Mobilewright. If something breaks, this is the first place to reproduce it.
