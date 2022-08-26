import { registerPlugin } from '@capacitor/core';

import type { LoginProviderPlugin } from './definitions';

const LoginProvider = registerPlugin<LoginProviderPlugin>('LoginProvider', {
  web: () => import('./web').then(m => new m.LoginProviderWeb()),
});

export * from './definitions';
export { LoginProvider };
