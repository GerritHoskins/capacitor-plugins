import { registerPlugin } from '@capacitor/core';

import type { MparticlePlugin } from './definitions';
import type { MpTracking } from './plugin';

const Mparticle = registerPlugin<MparticlePlugin>('Mparticle', {
  web: () => import('./web').then(m => new m.MparticleWeb()),
});

const Tracking = registerPlugin<typeof MpTracking>('Tracking', {
  web: () => import('./plugin').then(m => new m.MpTracking()),
});
export * from './definitions';
export { Mparticle, Tracking };
