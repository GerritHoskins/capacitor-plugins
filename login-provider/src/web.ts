import { WebPlugin } from '@capacitor/core';

import { ApplePlugin } from './apple';
import type {
  AppleInitOptions,
  LoginProviderPlugin,
  Provider,
  ProviderResponse,
  FacebookInitOptions,
  FacebookLoginOptions,
  GoogleInitOptions,
} from './definitions';
import { ProviderName } from './definitions';
import { FacebookPlugin } from './facebook';
import { GooglePlugin } from './google';
import { TwitterPlugin } from './twitter';

export class LoginProviderWeb extends WebPlugin implements LoginProviderPlugin {
  provider: Provider | undefined;
  google: GooglePlugin = new GooglePlugin();
  facebook: FacebookPlugin = new FacebookPlugin();
  apple: ApplePlugin = new ApplePlugin();
  twitter: TwitterPlugin = new TwitterPlugin();

  constructor() {
    super();
  }

  signOutFromProvider(provider: ProviderName): Promise<any> {
    return Promise.reject(
      'logout method not implemented yet.' + provider.toString(),
    );
  }

  public async initializeProvider(
    initProvider: ProviderName,
    options: GoogleInitOptions | FacebookInitOptions | AppleInitOptions,
  ): Promise<void> {
    if (initProvider === ProviderName.Google) {
      return this.google.initialize(options as GoogleInitOptions);
    } else if (initProvider === ProviderName.Facebook) {
      return await this.facebook.initialize(options as FacebookInitOptions);
    } else if (initProvider === ProviderName.Apple) {
      return await this.apple.initialize(options as AppleInitOptions);
    } else if (initProvider === ProviderName.Twitter) {
      return Promise.resolve();
    }
  }

  public async signInWithProvider(
    loginProvider: ProviderName,
    options: FacebookLoginOptions,
    inviteCode?: '',
  ): Promise<ProviderResponse> {
    if (inviteCode) {
      console.log(inviteCode);
    }

    if (loginProvider === ProviderName.Google) {
      return await this.google.login();
    } else if (loginProvider === ProviderName.Facebook) {
      return await this.facebook.login(options as FacebookLoginOptions);
    } else if (loginProvider === ProviderName.Apple) {
      return await this.apple.login();
    } else if (loginProvider === ProviderName.Twitter) {
      return await this.twitter.login();
    }

    return Promise.reject(
      'login provider returned no valid authentication data',
    );
  }
}
