import { registerPlugin } from '@capacitor/core';

import type { Events, MparticlePlugin, ScreenEvents } from './definitions';

const Mparticle = registerPlugin<MparticlePlugin<Events, ScreenEvents>>(
  'Mparticle',
  {
    web: () => import('./web').then(m => new m.MparticleWeb()),
  },
);

export * from './definitions';
export { Mparticle };
