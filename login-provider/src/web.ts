import { WebPlugin } from '@capacitor/core';

import { ApplePlugin } from './apple';
import type {
  LoginProviderPlugin,
  LoginProviderOptions,
  LoginProviderPayload,
} from './definitions';
import { ProviderName } from './definitions';
import { FacebookPlugin } from './facebook';
import { GooglePlugin } from './google';

export class LoginProviderWeb extends WebPlugin implements LoginProviderPlugin {
  constructor() {
    super();
  }

  logoutFromProvider(provider: ProviderName): Promise<any> {
    return Promise.reject(
      'logout method not implemented yet.' + provider.toString(),
    );
  }

  public async loginWithProvider(
    providerName: ProviderName,
    options: LoginProviderOptions,
    inviteCode?: '',
  ): Promise<LoginProviderPayload> {
    if (!options)
      return Promise.reject(
        providerName + ' initialization settings required.',
      );

    if (providerName === ProviderName.APPLE) {
      return await this.loginWithApple(options, inviteCode);
    } else if (providerName === ProviderName.FACEBOOK) {
      return await this.loginWithFacebook(options, inviteCode);
    } else if (providerName === ProviderName.GOOGLE) {
      return await this.loginWithGoogle(options, inviteCode);
    } else if (providerName === ProviderName.TWITTER) {
      return await this.loginWithTwitter();
    }

    return Promise.reject(
      providerName + ' does not exit or did not return valid payload.',
    );
  }

  async loginWithApple(
    options: LoginProviderOptions,
    inviteCode?: '',
  ): Promise<LoginProviderPayload> {
    const apple: ApplePlugin = new ApplePlugin();
    const response = await apple.initialize(options).then(() => apple.login());

    if (!response)
      return Promise.reject(
        new Error('google login failed to retrieve valid auth response'),
      );

    return {
      provider: ProviderName.APPLE.toString(),
      token: response.token,
      secret: response.code,
      email: response.email,
      avatarUrl: '',
      inviteCode,
    } as LoginProviderPayload;
  }

  async loginWithFacebook(
    options: LoginProviderOptions,
    inviteCode?: '',
  ): Promise<LoginProviderPayload> {
    const facebook: FacebookPlugin = new FacebookPlugin();
    const response = await facebook
      .initialize(options)
      .then(() => facebook.login(options))
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
      provider: ProviderName.FACEBOOK.toString(),
      token,
      secret: '',
      email: profile.email,
      avatarUrl,
      inviteCode,
    } as LoginProviderPayload;
  }

  async loginWithGoogle(
    options: LoginProviderOptions,
    inviteCode?: '',
  ): Promise<LoginProviderPayload> {
    const google: GooglePlugin = new GooglePlugin();
    return google
      .initialize(options)
      .then(() => google.login())
      .then(response => {
        if (!response || !response.authentication)
          return Promise.reject(
            new Error('google login failed to retrieve valid auth response'),
          );
        return {
          provider: ProviderName.GOOGLE.toString(),
          email: response.email,
          token: response.authentication.idToken,
          secret: '',
          avatarUrl: response.imageUrl,
          inviteCode,
        } as LoginProviderPayload;
      })
      .then(() => google.refresh());
  }

  loginWithTwitter(): Promise<LoginProviderPayload> {
    throw this.unimplemented('Not implemented on web.');
  }
}
