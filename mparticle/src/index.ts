import { registerPlugin } from '@capacitor/core';

import type { MparticlePlugin } from './definitions';
import { Tracking as MpTracking} from './plugin';

const Mparticle = registerPlugin<MparticlePlugin>(
  'Mparticle',
  {
    web: () => import('./web').then(m => new m.MparticleWeb()),
  },
);

const Tracking = registerPlugin<MpTracking>(
  'Tracking',
  {
    web: () => import('./plugin').then(m => new m.Tracking()),
  },
);
export * from './definitions';
export { Mparticle, Tracking};
