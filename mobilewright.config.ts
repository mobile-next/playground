import { defineConfig } from 'mobilewright';

export default defineConfig({
  platform: 'android',
  testDir: './tests',
  bundleId: 'com.mobilenext.playground',
  reporter: 'html',
});

// Prerequisite before running: the Playground debug APK must be installed on
// the running emulator. Build and install it with:
//   make -C android                    # produces android/Playground-<ver>.apk
//   adb install -r android/app/build/outputs/apk/debug/app-debug.apk
// (mobilewright 0.0.22's config has no installApps option, so installation is a
//  manual/CI setup step for now.)
