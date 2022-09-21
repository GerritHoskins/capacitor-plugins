import { registerPlugin } from '@capacitor/core';

import type { DefaultEvent, MparticlePlugin } from './definitions';

const Mparticle = registerPlugin<MparticlePlugin<DefaultEvent, DefaultEvent>>(
  'Mparticle',
  {
    web: () => import('./web').then(m => new m.MparticleWeb()),
    plugin: () => import('./plugin').then(m => new m.Tracking()),
  },
);

export * from './definitions';
export { Mparticle };
