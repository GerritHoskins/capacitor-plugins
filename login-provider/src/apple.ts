import { WebPlugin } from '@capacitor/core';

import type {
  AppleInitOptions,
  AppleInterface,
  AppleLoginResponse,
} from './definitions';

declare let AppleID: any;

export class ApplePlugin extends WebPlugin implements AppleInterface {
  private appleScriptUrl =
    'https://appleid.cdn-apple.com/appleauth/static/jsapi/appleid/1/en_US/appleid.auth.js';
  private appleScriptLoaded = false;

  constructor() {
    super();
  }

  async initialize(options: AppleInitOptions): Promise<void> {
    this.appleScriptLoaded = await this.loadAppleAuthScript();
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

    const { email, identityToken, authorizationCode } = response;

    return {
      email: email,
      identityToken: identityToken,
      authorizationCode: authorizationCode,
    } as AppleLoginResponse;
  }

  private async loadAppleAuthScript(): Promise<boolean> {
    return new Promise(resolve => {
      if (!this.appleScriptLoaded) {
        if (typeof window !== undefined) {
          const script = document.createElement('script');
          script.src = this.appleScriptUrl;
          script.type = 'text/javascript';
          script.defer = true;
          script.async = true;
          script.id = 'apple_auth';
          script.onload = () => resolve(true);
          document.head.appendChild(script);
        } else {
          resolve(false);
        }
      } else {
        resolve(true);
      }
    });
  }
}
