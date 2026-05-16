import { defineConfig, type MobilewrightConfig } from 'mobilewright';

const config: MobilewrightConfig = {
  testDir: '.',
  retries: 0,
  timeout: 120_000,
  bundleId: 'com.mobilenext.playground',
  fullyParallel: true,
  workers: process.env.CI ? 2 : 1,
  reporter: [
    ['list'],
    ['html', { outputFolder: 'mobilewright-report' }],
  ],
  projects: [
    {
      name: 'ios',
      use: {
        platform: 'ios',
        installApps: 'ios/Playground-dev.zip',
      },
    },
    {
      name: 'android',
      use: {
        platform: 'android',
        installApps: 'android/Playground-dev.apk',
      },
    },
  ],
};

if (process.env['MOBILE_USE_API_KEY']) {
  config.driver = {
    type: 'mobile-use',
    apiKey: process.env['MOBILE_USE_API_KEY'],
  };
}

export default defineConfig(config);
