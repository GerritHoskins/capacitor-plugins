import { WebPlugin } from '@capacitor/core';

import type { GoogleInterface, LoginProviderOptions } from './definitions';

import GoogleUser = gapi.auth2.GoogleUser;
import ClientConfig = gapi.auth2.ClientConfig;

export class GooglePlugin extends WebPlugin implements GoogleInterface {
  private authInstance: gapi.auth2.GoogleAuth | undefined;
  private loadingPromise: Promise<unknown> | undefined;
  private gapiScript =
    'https://apis.google.com/js/platform.js?onload=onGapiLoad';

  constructor() {
    super();
  }

  loadScript = (): Promise<void> => {
    return new Promise((resolve, reject) => {
      if (typeof window === undefined) reject();
      const el = document.getElementById('auth2_script');
      if (!el) {
        const gplatformScript = document.createElement('script');
        gplatformScript.src = this.gapiScript;
        gplatformScript.type = 'text/javascript';
        gplatformScript.defer = true;
        gplatformScript.async = true;
        gplatformScript.id = 'auth2_script';
        document.head.appendChild(gplatformScript);
      }
      resolve();
    });
  };

  onGapiLoadPromise = (params: LoginProviderOptions): Promise<unknown> => {
    const settings: ClientConfig = {
      client_id: params.clientId,
      scope: params.scope || '',
    };
    return new Promise((resolve, reject) => {
      if (typeof window === undefined) reject();
      (window as any).onGapiLoad = () => {
        gapi.load('auth2', () => {
          try {
            this.authInstance = gapi.auth2.init(Object.assign({}, settings));
          } catch (err) {
            reject({
              err: 'incorrect or missing settings or options',
            });
          }
          resolve(this.authInstance);
        });
      };
    });
  };

  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  loadingAuth2 = (params): Promise<unknown> => {
    if (this.authInstance) {
      return Promise.resolve(this.authInstance);
    } else {
      if (!this.loadingPromise)
        this.loadingPromise = this.onGapiLoadPromise(params);
      return this.loadingPromise;
    }
  };

  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  load = (params): Promise<unknown> => {
    return Promise.all([this.loadingAuth2(params), this.loadScript()]).then(
      results => {
        return results[0];
      },
    );
  };

  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  wrapper = (f, method: string): any => {
    if (f) return f[method]();
    else {
      const err = {
        err: 'script failed to load correctly.',
      };
      return Promise.reject(err);
    }
  };

  login = (): Promise<GoogleUser> => this.wrapper(this.authInstance, 'signIn');

  logout = (): any => this.wrapper(this.authInstance, 'signOut');

  isSignedIn = (): boolean =>
    this.wrapper(this.authInstance?.isSignedIn, 'get');

  currentUser = (): GoogleUser =>
    this.wrapper(this.authInstance?.currentUser, 'get');

  grantOfflineAccess = (): any =>
    this.wrapper(this.authInstance, 'grantOfflineAccess');
}
