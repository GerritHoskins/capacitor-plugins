import type { Consent, Identifier } from './definitions';

interface mParticleInterface<Events, ScreenEvents> {
  init(key?: string, config?: Record<string, unknown>): Promise<void>;
  identifyUser(identifier: Identifier): Promise<void>;
  setUserAttribute(key: string, value: string): Promise<void>;

  setGDPRConsent(consents: Record<string, Consent>): void;
  getGDPRConsent(consents: string[]): Record<string, boolean> | void;

  getMPID(): Promise<string | void>;

  trackEvent: Events;
  trackPageView: ScreenEvents;
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
type DefaultEvent = (name: string, data?: any) => void;

declare const _default: {
  getInstance<
    Events = DefaultEvent,
    ScreenEvents = DefaultEvent,
  >(): mParticleInterface<Events, ScreenEvents>;
};

export default _default;
