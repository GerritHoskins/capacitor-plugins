import { registerPlugin } from '@capacitor/core';

import type { ITwitterPlugin } from './definitions';

const Twitter = registerPlugin<ITwitterPlugin>('Twitter', {
  web: () => import('./web').then(m => new m.TwitterPluginWeb()),
  ios: () => import('./plugin').then(m => new m.ITwitter()),
  android: () => import('./plugin').then(m => new m.ITwitter()),
});

export * from './definitions';
export { Twitter };
