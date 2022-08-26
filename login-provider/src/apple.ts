import { WebPlugin } from '@capacitor/core';
import * as $script from 'scriptjs';

import type {
  AppleInitOptions,
  AppleInterface,
  AppleLoginResponse,
} from './definitions';

declare global {
  interface Window {
    AppleID: {
      auth: {
        init: (options: AppleInitOptions) => Promise<void>;
        signIn: () => Promise<{
          user: null;
          email: string | null;
          givenName: string | null;
          familyName: string | null;
          identityToken: string;
          authorizationCode: string;
        }>;
      };
    };
  }
}

const AppleID = window.AppleID;

export class ApplePlugin extends WebPlugin implements AppleInterface {
  appleScriptUrl =
    'https://appleid.cdn-apple.com/appleauth/static/jsapi/appleid/1/en_US/appleid.auth.js';
  appleScriptLoaded: false | unknown;

  constructor() {
    super({
      name: 'SignInWithApple',
      platforms: ['web'],
    });
  }

  async initialize(options: AppleInitOptions): Promise<void> {
    this.appleScriptLoaded = await this.loadSignInWithAppleJS();
    if (this.appleScriptLoaded) {
      return await AppleID.auth.init(options);
    } else {
      return Promise.reject(
        'Unable to initialize Sign in with Apple JS framework.',
      );
    }
  }

  async login(): Promise<AppleLoginResponse> {
    const response = await AppleID.auth.signIn();
    if (!response) return Promise.reject('Unable to Sign in with Apple.');

    const {
      user,
      email,
      givenName,
      familyName,
      identityToken,
      authorizationCode,
    } = response;

    return {
      user: user,
      email: email,
      name: givenName + ' ' + familyName,
      token: identityToken,
      code: authorizationCode,
    } as AppleLoginResponse;
  }

  async loadSignInWithAppleJS(): Promise<boolean> {
    return new Promise(resolve => {
      if (!this.appleScriptLoaded) {
        if (typeof window !== undefined) {
          $script.get(this.appleScriptUrl, () => resolve(true));
        } else {
          resolve(false);
        }
      } else {
        resolve(true);
      }
    });
  }
}
