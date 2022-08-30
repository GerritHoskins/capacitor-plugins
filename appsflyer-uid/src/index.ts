import { registerPlugin } from '@capacitor/core';

import type { AppsflyerUidPlugin } from './definitions';

const AppsflyerUid = registerPlugin<AppsflyerUidPlugin>('AppsflyerUid', {
  web: () => import('./web').then(m => new m.AppsflyerUidWeb()),
});

export * from './definitions';
export { AppsflyerUid };
