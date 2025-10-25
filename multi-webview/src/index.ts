import { registerPlugin } from '@capacitor/core';

import type { MultiWebviewPlugin } from './definitions';

const MultiWebview = registerPlugin<MultiWebviewPlugin>('MultiWebview', {
  web: () => import('./web').then(m => new m.MultiWebviewWeb()),
});

export * from './definitions';
export { MultiWebview };
