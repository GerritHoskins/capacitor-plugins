import { WebPlugin } from '@capacitor/core';

import type { DefaultEvent, MparticlePlugin } from './definitions';

export declare class MpTracking extends WebPlugin {
  getInstance<
    Events = DefaultEvent,
    ScreenEvents = DefaultEvent,
  >(): MparticlePlugin<Events, ScreenEvents>;
}
