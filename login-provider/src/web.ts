import { WebPlugin } from '@capacitor/core';

import { ApplePlugin } from './apple';
import type {
  LoginProviderPlugin,
  LoginProviderPayload,
  ProviderName,
  LoginProviderInitOptions,
  AppleInitOptions,
  FacebookInitOptions,
  LoginProviderOptions,
} from './definitions';
import { FacebookPlugin } from './facebook';
import { GooglePlugin } from './google';

export class LoginProviderWeb extends WebPlugin implements LoginProviderPlugin {
  constructor() {
    super();
  }

  async logoutFromProvider(options: { provider: ProviderName }): Promise<any> {
    return Promise.reject(
      'logout method not implemented yet.' + options.provider.toString(),
    );
  }

  public async loginWithProvider(
    options: LoginProviderInitOptions,
  ): Promise<LoginProviderPayload> {
    const { provider, loginOptions, inviteCode } = options;

    if (!loginOptions)
      return Promise.reject(provider + ' initialization settings required.');

    if (provider === 'APPLE') {
      return await this.loginWithApple({ loginOptions, inviteCode });
    } else if (provider === 'FACEBOOK') {
      return await this.loginWithFacebook({ loginOptions, inviteCode });
    } else if (provider === 'GOOGLE') {
      return await this.loginWithGoogle({ loginOptions, inviteCode });
    } else if (provider === 'TWITTER') {
      return await this.loginWithTwitter({ loginOptions, inviteCode });
    }

    return Promise.reject(
      provider + ' does not exit or did not return valid payload.',
    );
  }

  async loginWithApple(options: {
    loginOptions?: LoginProviderOptions;
    inviteCode?: string;
  }): Promise<LoginProviderPayload> {
    const { loginOptions, inviteCode } = options;

    const apple: ApplePlugin = new ApplePlugin();
    const response = await apple
      .initialize(loginOptions as AppleInitOptions)
      .then(() => apple.login());

    if (!response)
      return Promise.reject(
        new Error('google login failed to retrieve valid auth response'),
      );

    return {
      provider: 'APPLE',
      token: response.identityToken,
      secret: response.authorizationCode,
      email: response.email,
      avatarUrl: '',
      inviteCode,
    } as LoginProviderPayload;
  }

  async loginWithFacebook(options: {
    loginOptions?: LoginProviderOptions;
    inviteCode?: string;
  }): Promise<LoginProviderPayload> {
    const { loginOptions, inviteCode } = options;

    const facebook: FacebookPlugin = new FacebookPlugin();
    const response = await facebook
      .initialize(loginOptions as Partial<FacebookInitOptions>)
      .then(() => facebook.login(loginOptions as Partial<FacebookInitOptions>))
      .then(() => facebook.getCurrentAccessToken());

    if (!response)
      return Promise.reject(
        new Error('google login failed to retrieve valid auth response'),
      );

    const token = response.accessToken?.token || '';
    const userID = response.accessToken?.userId || '';
    const avatarUrl = userID
      ? `https://graph.facebook.com/${userID}/picture?type=square`
      : '';

    const profile = await facebook.getProfile<{ email: string }>({
      fields: ['email'],
    });

    return {
      provider: 'FACEBOOK',
      token,
      secret: '',
      email: profile.email,
      avatarUrl,
      inviteCode,
    } as LoginProviderPayload;
  }

  async loginWithGoogle(options: {
    loginOptions?: LoginProviderOptions;
    inviteCode?: string;
  }): Promise<LoginProviderPayload> {
    const { loginOptions, inviteCode } = options;

    const google: GooglePlugin = new GooglePlugin();
    return google
      .load(loginOptions)
      .then(() => {
        if (google.isSignedIn()) {
          return google.currentUser();
        }

        return google.login();
      })
      .then(response => {
        if (!response) {
          return Promise.reject(
            new Error('google login failed to retrieve valid auth response'),
          );
        }

        return {
          provider: 'GOOGLE',
          email: response.getBasicProfile().getEmail(),
          token: response.getAuthResponse().id_token,
          secret: '',
          avatarUrl: response.getBasicProfile().getImageUrl(),
          inviteCode,
        } as LoginProviderPayload;
      })
      .catch(err => {
        return Promise.reject(err);
      });
  }

  loginWithTwitter(options: {
    loginOptions?: LoginProviderOptions;
    inviteCode?: string;
  }): Promise<LoginProviderPayload> {
    const { loginOptions, inviteCode } = options;
    throw this.unimplemented(
      'Not implemented on web.' + loginOptions?.locale + inviteCode,
    );
  }

  /*async addListener(options: {
    eventName: 'appStateChange';
    listenerFunc: AppStateChangeListener;
  }): Promise<PluginListenerHandle> & PluginListenerHandle {
    return Promise.reject('Not implemented on web.');
  }

  async removeAllListeners(): Promise<void> {
    return Promise.reject('Not implemented on web.');
  }*/
}
