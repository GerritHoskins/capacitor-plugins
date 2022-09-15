import { registerPlugin } from '@capacitor/core';

import type { TwitterLoginPlugin } from './definitions';

const Twitter = registerPlugin<TwitterLoginPlugin>('Twitter', {
  web: () => import('./web').then(m => new m.TwitterLoginWeb()),
});

export * from './definitions';
export { Twitter };
