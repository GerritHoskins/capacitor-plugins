import { registerPlugin } from '@capacitor/core';

import type { ITwitterLoginPlugin } from './definitions';

const Twitter = registerPlugin<ITwitterLoginPlugin>('Twitter', {
  web: () => import('./web').then(m => new m.TwitterWeb()),
  plugin: () => import('./plugin').then(m => new m.TwitterPlugin()),
});

export * from './definitions';
export { Twitter };
