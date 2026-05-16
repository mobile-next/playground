import { test, expect } from '@mobilewright/test';

test.use({ video: 'on' });

test.beforeEach(async ({ device, bundleId }) => {
  await device.terminateApp(bundleId!).catch(() => {});
  await device.launchApp(bundleId!);
});

test.afterEach(async ({ screen }, testInfo) => {
  if (testInfo.status === 'passed') {
    const screenshot = await screen.screenshot();
    await testInfo.attach('screenshot', { body: screenshot, contentType: 'image/png' });
  }
});

test.describe('main menu', () => {
  test('shows at least two items in the menu list', async ({ screen }) => {
    await expect(screen.getByText('Basic UI')).toBeVisible();
    await expect(screen.getByText('Web View')).toBeVisible();
  });
});
