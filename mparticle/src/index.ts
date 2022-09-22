import { registerPlugin } from '@capacitor/core';

import type { DefaultEvent, MparticlePlugin, mParticle } from './definitions';

const Mparticle = registerPlugin<MparticlePlugin>('Mparticle', {
  web: () => import('./web').then(m => new m.MparticleWeb()),
});

declare const _default: {
  getInstance<Events = DefaultEvent, ScreenEvents = DefaultEvent>(): mParticle<
    Events,
    ScreenEvents
  >;
};

export * from './definitions';
export default { Mparticle, _default };
