import { test, expect } from '@mobilewright/test';

test('launch app with custom activity', async ({ device, screen }) => {
  await device.terminateApp(bundleId!).catch(() => {});
  await device.launchApp(bundleId!, {
    activity: '.BasicUIActivity',
  });
  await expect(screen.getByText('RESET COUNTER')).toBeVisible();
});
