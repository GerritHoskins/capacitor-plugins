/// <reference types="@capacitor/cli" />
declare module '@capacitor/cli' {
  export interface PluginsConfig {
    TwitterPlugin?: TwitterPluginOptions;
  }
}

export interface ITwitterLoginPlugin {
  isLogged(): Promise<{ in: boolean; out: boolean }>;

  login(): Promise<{
    authToken: string;
    authTokenSecret: string;
    userName: string;
    userID: string;
  }>;

  logout(): Promise<void>;
}
export interface TwitterPluginOptions {
  consumerKey?: string;
  consumerSecret?: string;
}
