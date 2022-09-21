import { registerPlugin } from '@capacitor/core';

import type { DefaultEvent, MparticlePlugin } from './definitions';

const Mparticle = registerPlugin<MparticlePlugin<DefaultEvent, DefaultEvent>>(
  'Mparticle',
  {
    web: () => import('./web').then(m => new m.MparticleWeb()),
  },
);

declare const _default: {
  getInstance<
    Events = DefaultEvent,
    ScreenEvents = DefaultEvent,
  >(): MparticlePlugin<Events, ScreenEvents>;
};

export * from './definitions';
export { Mparticle };

export default _default;
