import { WebPlugin } from '@capacitor/core';

import type { GoogleInterface, LoginProviderOptions } from './definitions';

import GoogleUser = gapi.auth2.GoogleUser;
import ClientConfig = gapi.auth2.ClientConfig;

let auth2: gapi.auth2.GoogleAuth | undefined;
let loadingPromise: Promise<unknown> | undefined;

export class GooglePlugin extends WebPlugin implements GoogleInterface {
  constructor() {
    super();
  }

  loadScript = (): Promise<void> => {
    return new Promise(resolve => {
      const el = document.getElementById('auth2_script_id');
      if (!el) {
        const gplatformScript = document.createElement('script');
        gplatformScript.setAttribute(
          'src',
          'https://apis.google.com/js/platform.js?onload=onGapiLoad',
        );
        gplatformScript.type = 'text/javascript';
        gplatformScript.defer = true;
        gplatformScript.async = true;
        gplatformScript.id = 'auth2_script_id';
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
      (window as any).onGapiLoad = () => {
        gapi.load('auth2', () => {
          try {
            auth2 = gapi.auth2.init(Object.assign({}, settings));
          } catch (err) {
            reject({
              err: 'client_id missing or is incorrect, or if you added extra params maybe they are written incorrectly, did you add it to the component or plugin?',
            });
          }
          resolve(auth2);
        });
      };
    });
  };

  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  loadingAuth2 = (params): Promise<unknown> => {
    if (auth2) {
      return Promise.resolve(auth2);
    } else {
      if (!loadingPromise) loadingPromise = this.onGapiLoadPromise(params);
      return loadingPromise;
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
        err: 'Script not loaded correctly, did you added the plugin or the client_id to the component?',
      };
      return Promise.reject(err);
    }
  };

  login = (): Promise<GoogleUser> => this.wrapper(auth2, 'signIn');

  logout = (): any => this.wrapper(auth2, 'signOut');

  isSignedIn = (): boolean => this.wrapper(auth2?.isSignedIn, 'get');

  currentUser = (): GoogleUser => this.wrapper(auth2?.currentUser, 'get');

  grantOfflineAccess = (): any => this.wrapper(auth2, 'grantOfflineAccess');
}
