import { WebPlugin } from '@capacitor/core';

import type {
  TwitterInterface,
  TwitterUserStatusResponse,
  TwitterLoginResponse,
} from './definitions';

export class TwitterPlugin extends WebPlugin implements TwitterInterface {
  constructor() {
    super({
      name: 'Twitter',
      platforms: ['web'],
    });
  }
  isLogged(): Promise<TwitterUserStatusResponse> {
    throw this.unimplemented('Not implemented on web.');
  }
  login(): Promise<TwitterLoginResponse> {
    throw this.unimplemented('Not implemented on web.');
  }
  logout(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }
}
