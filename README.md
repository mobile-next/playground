# 🛝 Playground

Playground for iOS and Android is an app that exercises a wide range of native UI components, web view integration, and accessibility attributes.

## 🗂️ Structure

```
playground/
├── ios/        # SwiftUI app (Xcode project)
└── android/    # Kotlin app (Gradle project)
```

Both implementations are feature-equivalent: same screens, same components, same accessibility labels.

## 📱 Screens

### 🏠 Home
Navigation links to the two demo screens.

### 🧩 Basic UI
A scrollable showcase of native components:

| Category | Components |
|---|---|
| ✏️ Text input | Text field, password field, multiline editor |
| 🎛️ Controls | Toggle switch, stepper (0–100), reset button |
| 🎚️ Slider | 0.0–1.0 range with live value display |
| 📋 Pickers | Dropdown picker + segmented control (3 options each) |
| 📅 Date picker | Native date selector with formatted display |
| ⏳ Progress | Horizontal progress bar + activity indicator |
| 🏷️ Labels | Icon label, plain label, tappable hyperlink |
| 💬 Dialogs | Alert dialog, confirmation dialog, contextual menu |
| 📤 Share / Photos | Share sheet + photo picker |

Every element carries an accessibility identifier (iOS) or content description (Android).

### 🌐 Web View
Loads a URL inside a native `WKWebView` (iOS) / `WebView` (Android) with JavaScript and DOM storage enabled.

### 🔐 SharedPref / Keychain
Save, load, and delete a username and password using platform-native storage:

| Platform | Username | Password |
|---|---|---|
| iOS | Keychain (`kSecClassGenericPassword`) | Keychain (`kSecClassGenericPassword`) |
| Android | `SharedPreferences` | `EncryptedSharedPreferences` (AES256-GCM) |

Includes a reveal/hide toggle for the password field.

## 🔨 Building

### 🍎 iOS

Requirements: Xcode 15+, iOS 17+ simulator or device.

```
open ios/playground.xcodeproj
```

Select a simulator or device and press Run (⌘R).

### 🤖 Android

Requirements: Android Studio Hedgehog+, min SDK 26, compile SDK 36.

```
cd android
./gradlew assembleDebug
```

Or open the `android/` folder in Android Studio and run the `app` configuration.

## 📄 License

Apache 2.0 — see [LICENSE](LICENSE).
