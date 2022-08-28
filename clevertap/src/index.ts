import { registerPlugin } from '@capacitor/core';

import type { ClevertapPlugin } from './definitions';

const Clevertap = registerPlugin<ClevertapPlugin>('Clevertap', {
  web: () => import('./web').then(m => new m.ClevertapWeb()),
});

export * from './definitions';
export { Clevertap };
