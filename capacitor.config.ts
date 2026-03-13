import { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.v7x.app',
  appName: 'V7X',
  webDir: 'www',
  server: {
    url: 'https://v7x.fun/user/login',
    cleartext: true
  },
  plugins: {
    SplashScreen: {
      launchShowDuration: 3000,
      backgroundColor: "#000000",
      showSpinner: true,
      androidScaleType: "CENTER_CROP",
      spinnerColor: "#ffc107"
    }
  }
};

export default config;
