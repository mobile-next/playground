// Mobilewright end-to-end flow for the Playground Android app.
//
// One iteration:
//   1. (Re)launch the app so we start on the Home screen.
//   2. Open the "Basic UI" screen and assert it is shown.
//   3. Fill the FIRST text field with "hello world" and assert its value.
//   4. Take a screenshot.
//   5. Press Android back and assert we are back on Home.
// The flow runs 20 times in a loop.
//
// This is a STARTING POINT — written to be correct and readable against the
// real app, but NOT yet validated on a live emulator. See the PR description.
//
// PREREQUISITE: the Playground debug APK must already be installed on a running
// Android emulator. Build + install it first:
//   make -C android
//   adb install -r android/app/build/outputs/apk/debug/app-debug.apk
//
// Locators (grounded in android/app/src/main/res/layout/activity_main.xml and
// activity_basic_ui.xml):
//   - Home has a tappable row with visible text "Basic UI" (id=btn_basic_ui).
//   - The Basic UI screen's first text field is an EditText with
//     id="text_field" and contentDescription="text_field" (hint "Text Field").
//     mobilewright derives a testId from the Android resource-id / content
//     description, so getByTestId('text_field') is the highest-priority,
//     deterministic locator for it.
//
// TODO (Android-native variant): a parallel Espresso / UiAutomator version of
//   this exact flow is planned but intentionally NOT implemented here. This PR
//   is Mobilewright-first only.

import { mkdirSync, writeFileSync } from 'node:fs';
import { dirname } from 'node:path';
import { test, expect } from '@mobilewright/test';
import type { Device, Locator, Screen } from 'mobilewright';

const ITERATIONS = 20;

// Fallback bundle id if the config's bundleId fixture is undefined.
const APP_BUNDLE_ID = 'com.mobilenext.playground';

// Visible text of the Home row that opens the Basic UI screen.
const BASIC_UI_NAV_LABEL = 'Basic UI';

// Another Home row, used to assert we returned Home after pressing back
// (it does NOT appear on the Basic UI screen, so it is a clean marker).
const HOME_ONLY_NAV_LABEL = 'Web View';

// The first text field on the Basic UI screen (id / contentDescription).
const FIRST_TEXT_FIELD_TEST_ID = 'text_field';

const TYPED_TEXT = 'hello world';

test.describe('Playground — Basic UI: fill first text field, screenshot, back (x20)', () => {
  for (let iteration = 1; iteration <= ITERATIONS; iteration++) {
    test(`iteration ${iteration}/${ITERATIONS}`, async ({ screen, device, bundleId }) => {
      await relaunchApp(device, bundleId ?? APP_BUNDLE_ID);
      await expectOnHomeScreen(screen);

      await openBasicUiScreen(screen);

      const firstTextField = await fillFirstTextField(screen, TYPED_TEXT);
      await expect(firstTextField).toHaveValue(TYPED_TEXT);

      await saveScreenshot(screen, `test-results/basic-ui-loop/iteration-${iteration}.png`);

      await goBack(screen);
      await expectOnHomeScreen(screen);
    });
  }
});

// Cold-launch the app (terminate first, ignoring "not running") so every
// iteration starts from a clean Home screen.
async function relaunchApp(device: Device, bundleId: string): Promise<void> {
  await device.terminateApp(bundleId).catch(() => {});
  await device.launchApp(bundleId);
}

async function expectOnHomeScreen(screen: Screen): Promise<void> {
  await expect(screen.getByText(HOME_ONLY_NAV_LABEL)).toBeVisible();
}

// Tap the "Basic UI" row and confirm the destination screen rendered by
// waiting for its first text field to appear.
async function openBasicUiScreen(screen: Screen): Promise<void> {
  await screen.getByText(BASIC_UI_NAV_LABEL).tap();
  await expect(screen.getByTestId(FIRST_TEXT_FIELD_TEST_ID)).toBeVisible();
}

// Focus + clear the first text field, type `value`, and return the locator so
// the caller can assert on it.
async function fillFirstTextField(screen: Screen, value: string): Promise<Locator> {
  const firstTextField = screen.getByTestId(FIRST_TEXT_FIELD_TEST_ID);
  await firstTextField.fill(value);
  return firstTextField;
}

// mobilewright 0.0.22's screen.screenshot() returns a PNG Buffer and does not
// write to disk itself, so we persist it with native fs (no shelling out).
async function saveScreenshot(screen: Screen, path: string): Promise<void> {
  const png = await screen.screenshot();
  mkdirSync(dirname(path), { recursive: true });
  writeFileSync(path, png);
}

async function goBack(screen: Screen): Promise<void> {
  await screen.pressButton('BACK');
}
