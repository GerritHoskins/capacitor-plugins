/*import { registerPlugin } from '@capacitor/core';

import type { MparticlePlugin } from './definitions';

const Mparticle = registerPlugin<MparticlePlugin>('Mparticle', {
  web: () => import('./web').then(m => new m.MparticleWeb()),
});

export * from './definitions';
export { Mparticle };
*/

export * from './definitions';
export * from './plugin';
export * from './web';
