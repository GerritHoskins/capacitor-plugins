import { registerPlugin } from '@capacitor/core';

import type { ITwitterLoginPlugin } from './definitions';

const TwitterLoginPlugin =
  registerPlugin<ITwitterLoginPlugin>('TwitterLoginPlugin');

class TwitterClass implements ITwitterLoginPlugin {
  isLogged(): Promise<{ in: boolean; out: boolean }> {
    return TwitterLoginPlugin.isLogged();
  }
  login(): Promise<{
    authToken: string;
    authTokenSecret: string;
    userName: string;
    userID: string;
  }> {
    return TwitterLoginPlugin.login();
  }
  logout(): Promise<void> {
    return TwitterLoginPlugin.logout();
  }
}

export const Twitter = new TwitterClass();
