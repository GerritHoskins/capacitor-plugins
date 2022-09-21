import type { DefaultEvent, MparticlePlugin } from "./definitions";
export declare class MpTracking  {
  getInstance<Events = DefaultEvent, ScreenEvents = DefaultEvent>(): MparticlePlugin<Events, ScreenEvents>;
}
