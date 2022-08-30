/// <reference types="@capacitor/cli" />

import type { PluginListenerHandle } from '@capacitor/core';

declare module '@capacitor/cli' {
  export interface PluginsConfig {
    LoginProvider?: {
      apple?: {
        clientId: string;
        redirectURI: string;
        scope: string;
      };
      facebook?: {
        appId: string;
        autoLogAppEvents: boolean;
        xfbml: boolean;
        version: string;
        locale: string;
        permissions: string[];
      };
      google?: {
        scopes: string[];
        serverClientId: string;
        forceCodeForRefreshToken: boolean;
      };
      twitter?: {
        consumerKey: string;
        consumerSecret: string;
      };
    };
  }
}

/***/
export interface LoginProviderPlugin {
  loginWithProvider(
    provider: ProviderName,
    options?: LoginProviderOptions,
    inviteCode?: string,
  ): Promise<LoginProviderPayload>;
  loginWithApple(
    options: LoginProviderOptions,
    inviteCode?: string,
  ): Promise<LoginProviderPayload>;
  loginWithFacebook(
    options: LoginProviderOptions,
    inviteCode?: string,
  ): Promise<LoginProviderPayload>;
  loginWithGoogle(
    options: LoginProviderOptions,
    inviteCode?: string,
  ): Promise<LoginProviderPayload>;
  loginWithTwitter(): Promise<LoginProviderPayload>;

  logoutFromProvider(provider: ProviderName): Promise<void | any>;
  addListener(
    eventName: 'appStateChange',
    listenerFunc: AppStateChangeListener,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;
  removeAllListeners(): Promise<void>;
}

export type LoginProviderPayload = {
  provider: string;
  token: string;
  secret: string;
  email: string;
  avatarUrl: string;
  inviteCode?: string;
};

export interface LoginProviderOptions
  extends AppleInitOptions,
    FacebookInitOptions,
    GoogleInitOptions,
    FacebookLoginOptions,
    Pick<
      GoogleInitOptions,
      'scopes' | 'clientId' | 'forceCodeForRefreshToken'
    > {
  grantOfflineAccess?: boolean;
  custom?: Record<string, unknown>;
}
export type AppStateChangeListener = (change: AppStateChange) => void;
export interface AppStateChange {
  user:
    | GoogleLoginResponse
    | FacebookLoginResponse
    | AppleLoginResponse
    | TwitterLoginResponse
    | null;
}

export enum ProviderName {
  FACEBOOK,
  GOOGLE,
  APPLE,
  TWITTER,
}
/***/

// GOOGLE
export interface GoogleInterface {
  initialize(options: Partial<GoogleInitOptions>): Promise<void>;
  login(): Promise<GoogleLoginResponse>;
  refresh(): Promise<GoogleAuth>;
  logout(): Promise<any>;
}
export interface GoogleLoginResponse {
  id: string;
  email: string;
  name: string;
  familyName: string;
  givenName: string;
  imageUrl: string;
  serverAuthCode: string;
  authentication: GoogleAuth;
}
export interface GoogleAuth {
  accessToken: string;
  idToken: string;
  refreshToken?: string;
}
export interface GoogleInitOptions {
  clientId?: string;
  iosClientId?: string;
  androidClientId?: string;
  scopes?: string[];
  serverClientId?: string;
  forceCodeForRefreshToken?: boolean;
}

// FACEBOOK
export interface FacebookInterface {
  initialize(options: Partial<FacebookInitOptions>): Promise<void>;
  login(options: FacebookLoginOptions): Promise<FacebookLoginResponse>;
  logout(): Promise<void>;
  reauthorize(): Promise<FacebookLoginResponse>;
  getCurrentAccessToken(): Promise<FacebookCurrentAccessTokenResponse>;
  getProfile<T extends Record<string, unknown>>(options: {
    fields: readonly string[];
  }): Promise<T>;
}
export interface FacebookAuth {
  applicationId?: string;
  declinedPermissions?: string[];
  expires?: string;
  isExpired?: boolean;
  lastRefresh?: string;
  permissions?: string[];
  token: string;
  userId?: string;
}
export interface FacebookLoginResponse {
  accessToken: FacebookAuth | null;
  recentlyGrantedPermissions?: string[];
  recentlyDeniedPermissions?: string[];
}
export interface FacebookCurrentAccessTokenResponse {
  accessToken: FacebookAuth | null;
}
export interface FacebookGetLoginStatusResponse {
  status: 'connected';
  authResponse: {
    accessToken: string;
    expiresIn: number;
    reauthorize_required_in: number;
    signedRequest: string;
    userID: string;
  };
}
export interface FacebookError {
  message: string;
  type: string;
  code: number;
}
export interface FacebookGetProfileResponse {
  error: FacebookError | null;
}
export interface FacebookLoginOptions {
  permissions?: string[];
}
export interface FacebookInitOptions {
  appId?: string;
  autoLogAppEvents?: boolean;
  xfbml?: boolean;
  version?: string;
  locale?: string;
}

//APPLE
export interface AppleInterface {
  appleScriptUrl: string;
  appleScriptLoaded: boolean | unknown;
  login(): Promise<AppleLoginResponse>;
  initialize(options: AppleInitOptions): Promise<void>;
}
export interface AppleInitOptions {
  clientId?: string;
  redirectURI?: string;
  state?: string;
  scope?: string;
  usePopup?: boolean;
}
export interface AppleLoginResponse {
  user: string | null;
  email: string | null;
  name: string | null;
  token: string;
  code: string;
}

//TWITTER
export interface TwitterInterface {
  isLogged(): Promise<TwitterUserStatusResponse>;
  login(): Promise<TwitterLoginResponse>;
  logout(): Promise<void>;
}
export interface TwitterLoginResponse {
  authToken: string;
  authTokenSecret: string;
  userName: string;
  userID: string;
}
export interface TwitterUserStatusResponse {
  in: boolean;
  out: boolean;
}
