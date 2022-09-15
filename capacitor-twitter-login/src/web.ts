import { WebPlugin } from '@capacitor/core';

import type {
  TwitterLoginPlugin,
  TwitterLoginResponse,
  TwitterLoggedResponse,
} from './definitions';

export class TwitterLoginWeb extends WebPlugin implements TwitterLoginPlugin {
  constructor() {
    super();
  }
  isLogged(): Promise<TwitterLoggedResponse> {
    return Promise.reject('Not implemented on web.');
  }
  login(): Promise<TwitterLoginResponse> {
    return Promise.reject('Not implemented on web.');
  }
  logout(): Promise<void> {
    return Promise.reject('Not implemented on web.');
  }
}
