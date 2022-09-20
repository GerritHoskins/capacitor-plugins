import { registerPlugin } from '@capacitor/core';

import type { AdidPlugin } from './definitions';

const Adid = registerPlugin<AdidPlugin>('Adid', {
  web: () => import('./web').then(m => new m.AdidWeb()),
});

export * from './definitions';
export { Adid };
