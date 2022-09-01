/// <reference types="@capacitor/cli" />

import type { PluginListenerHandle } from '@capacitor/core';

import GoogleUser = gapi.auth2.GoogleUser;

declare module '@capacitor/cli' {
  export interface PluginsConfig {
    LoginProvider?: {
      apple?: AppleInitOptions;
      facebook?: FacebookInitOptions;
      google?: GoogleInitOptions;
      twitter?: TwitterInitOptions;
    };
  }
}

export interface LoginProviderPlugin {
  loginWithProvider(options: {
    provider: ProviderName;
    options?: LoginProviderOptions;
    inviteCode?: string;
  }): Promise<LoginProviderPayload>;
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
  loginWithTwitter(
    provider: ProviderName,
    options?: LoginProviderOptions,
    inviteCode?: string,
  ): Promise<LoginProviderPayload>;
  logoutFromProvider(options: { provider: ProviderName }): Promise<void | any>;
  addListener(options: {
    eventName: 'appStateChange';
    listenerFunc: AppStateChangeListener;
  }): Promise<PluginListenerHandle> & PluginListenerHandle;
  removeAllListeners(): Promise<void>;
}
export type LoginProviderPayload = {
  provider: ProviderName;
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
    TwitterInitOptions,
    Pick<GoogleInitOptions, 'scope' | 'clientId' | 'forceCodeForRefreshToken'> {
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
export type ProviderName =
  | 'GOOGLE'
  | 'APPLE'
  | 'FACEBOOK'
  | 'TWITTER'
  | 'EMAIL';

// GOOGLE
export interface GoogleInterface {
  loadScript(): Promise<void>;
  onGapiLoadPromise(options: GoogleInitOptions): Promise<unknown>;
  loadingAuth2(params: any): Promise<unknown>;
  load(params: any): Promise<unknown>;
  wrapper(f: any, method: string): any;
  login(): Promise<GoogleUser>;
  logout(): any;
  isSignedIn(): boolean;
  currentUser(): GoogleUser;
  grantOfflineAccess(): any;
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
  scope?: string;
  serverClientId?: string;
  forceCodeForRefreshToken?: boolean;
  redirectURI?: string;
}

// FACEBOOK
export interface FacebookInterface {
  initialize(options: Partial<FacebookInitOptions>): Promise<void>;
  login(options: FacebookLoginOptions): Promise<FacebookLoginResponse>;
  logout(): Promise<void>;
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
export interface FacebookInitOptions extends FacebookLoginOptions {
  appId?: string;
  autoLogAppEvents?: boolean;
  xfbml?: boolean;
  version?: string;
  locale?: string;
}

//APPLE
export interface AppleInterface {
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
  email: string | null;
  identityToken: string;
  authorizationCode: string;
}

//TWITTER
export interface TwitterInterface {
  isLogged(): Promise<TwitterUserStatusResponse>;
  login(): Promise<TwitterLoginResponse>;
  logout(): Promise<void>;
}
export interface TwitterInitOptions {
  consumerKey?: string;
  consumerSecret?: string;
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
