import { WebPlugin } from '@capacitor/core';

import type {
  FacebookInitOptions,
  FacebookCurrentAccessTokenResponse,
  FacebookGetProfileResponse,
  FacebookLoginResponse,
  FacebookInterface,
  LoginProviderOptions,
} from './definitions';

export class FacebookPlugin extends WebPlugin implements FacebookInterface {
  constructor() {
    super();
  }

  initialize(options: Partial<FacebookInitOptions>): Promise<void> {
    const defaultOptions = { version: 'v10.0' };
    return new Promise((resolve, reject) => {
      try {
        return this.loadScript(options.locale).then(() => {
          FB.init({ ...defaultOptions, ...options });
          resolve();
        });
      } catch (err) {
        reject(err);
      }
    });
  }

  private loadScript(locale: string | undefined): Promise<void> {
    if (typeof document === 'undefined') {
      return Promise.resolve();
    }
    const scriptId = 'fb';
    const scriptEl = document?.getElementById(scriptId);
    if (scriptEl) {
      return Promise.resolve();
    }

    const head = document.getElementsByTagName('head')[0];
    const script = document.createElement('script');
    return new Promise<void>(resolve => {
      script.defer = true;
      script.async = true;
      script.id = scriptId;
      script.onload = () => {
        resolve();
      };
      script.src = `https://connect.facebook.net/${locale ?? 'en_US'}/sdk.js`;
      head.appendChild(script);
    });
  }

  async login(options: LoginProviderOptions): Promise<FacebookLoginResponse> {
    return new Promise<FacebookLoginResponse>((resolve, reject) => {
      FB.login(
        response => {
          console.debug('FB.login', response);

          if (response.status === 'connected') {
            resolve({
              accessToken: {
                token: response.authResponse.accessToken,
              },
            });
          } else {
            reject({
              accessToken: {
                token: null,
              },
            });
          }
        },
        { scope: options?.permissions || '' },
      );
    });
  }

  async logout(): Promise<void> {
    return new Promise<void>(resolve => {
      FB.logout(() => resolve());
    });
  }

  async getCurrentAccessToken(): Promise<FacebookCurrentAccessTokenResponse> {
    return new Promise<FacebookCurrentAccessTokenResponse>(
      (resolve, reject) => {
        FB.getLoginStatus(response => {
          if (response.status === 'connected') {
            const result: FacebookCurrentAccessTokenResponse = {
              accessToken: {
                applicationId: undefined,
                declinedPermissions: [],
                expires: undefined,
                isExpired: undefined,
                lastRefresh: undefined,
                permissions: [],
                token: response.authResponse.accessToken,
                userId: response.authResponse.userID,
              },
            };
            resolve(result);
          } else {
            reject({
              accessToken: {
                token: null,
              },
            });
          }
        });
      },
    );
  }

  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  // eslint-disable-next-line @typescript-eslint/ban-types
  async getProfile<T extends object>(options: {
    fields: readonly string[];
  }): Promise<T> {
    const fields = options.fields.join(',');

    return new Promise<T>((resolve, reject) => {
      FB.api<{ fields: string }, FacebookGetProfileResponse>(
        '/me',
        { fields },
        response => {
          if (response.error) {
            reject(response.error.message);

            return;
          }

          // eslint-disable-next-line @typescript-eslint/ban-ts-comment
          // @ts-ignore
          // eslint-disable-next-line @typescript-eslint/consistent-type-assertions
          resolve(<T>response);
        },
      );
    });
  }
}
