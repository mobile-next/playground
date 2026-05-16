import { defineConfig, type MobilewrightConfig } from 'mobilewright';

const config: MobilewrightConfig = {
  testDir: '.',
  retries: 0,
  timeout: 120_000,
  platform: 'ios',
  bundleId: 'com.mobilenext.playground',
  fullyParallel: true,
  workers: process.env.CI ? 2 : 1,
  installApps: '../ios/Playground-dev.ipa',
  reporter: [
    ['list'],
    ['html', { outputFolder: 'mobilewright-report' }],
  ],
};

if (process.env['MOBILE_USE_API_KEY']) {
  config.driver = {
    type: 'mobile-use',
    apiKey: process.env['MOBILE_USE_API_KEY'],
  };
}

export default defineConfig(config);
