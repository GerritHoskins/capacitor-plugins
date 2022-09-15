import { WebPlugin } from '@capacitor/core';

import type { ITwitterPlugin } from './definitions';

export class TwitterPluginWeb extends WebPlugin implements ITwitterPlugin {
  isLogged(): Promise<{ in: boolean; out: boolean }> {
    return Promise.reject('unimplemented.');
  }

  login(): Promise<{
    authToken: string;
    authTokenSecret: string;
    userName: string;
    userID: string;
  }> {
    return Promise.reject('unimplemented.');
  }

  logout(): Promise<void> {
    return Promise.reject('unimplemented.');
  }
}
