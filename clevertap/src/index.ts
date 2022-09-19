import { registerPlugin } from '@capacitor/core';

import type { ClevertapPlugin } from './definitions';

const Clevertap = registerPlugin<ClevertapPlugin>('Clevertap', {
  web: () => import('./web').then(m => new m.ClevertapWeb()),
  android: () => import('./plugin').then(m => new m.ClevertapNative()),
  ios: () => import('./plugin').then(m => new m.ClevertapNative()),
});

export * from './definitions';
export { Clevertap };
