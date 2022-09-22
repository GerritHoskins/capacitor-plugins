/// <reference types="@capacitor/cli" />
import type { PluginListenerHandle } from '@capacitor/core';
import type { MPConfiguration, PrivacyConsentState } from '@mparticle/web-sdk';
import type mParticle from 'mparticle__web-sdk';

declare module '@capacitor/cli' {
  export interface PluginsConfig {
    Mparticle: {
      key?: string;
      secret?: string;
      config?: MPConfiguration;
    };
  }
}

export interface MparticlePlugin<
  Events = DefaultEvent,
  ScreenEvents = DefaultEvent,
> {
  init(
    key: string,
    config: Record<string, unknown>,
    secret?: string,
  ): Promise<void>;
  identifyUser(identifier?: Identifier): Promise<void>;
  setUserAttribute(
    attributeName: string,
    attributeValue: string,
  ): Promise<void>;
  setGDPRConsent(options: {
    consents: Record<string, PrivacyConsentState>;
  }): void;
  getGDPRConsent(consents: string[]): Record<string, boolean> | void;
  getMPID(): Promise<string | void>;
  trackEvent: Events;
  trackPageView: ScreenEvents;
  getInstance(): mParticleInstanceType;
  loginUser(options?: { email: string; customerId: string }): Promise<any>;
  logoutUser(options?: any): Promise<any>;
  registerUser(options?: {
    email: string;
    customerId: string;
    userAttributes: any;
  }): Promise<any>;
  addListener(
    eventName: 'mParticleReady',
    listenerFunc: mParticleReadyListener,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;
}
export interface MparticleReadyEvent {
  ready: boolean;
}
export type Identifier = {
  email?: string;
  customerId?: string;
  other?: string;
};
export type Consent = {
  consented?: boolean;
  timestamp?: number;
  consentDocument?: string;
  location?: string;
  hardwareId?: string;
};
export type mParticleReadyListener = (event: MparticleReadyEvent) => void;
export type DefaultEvent = (name: string, data?: any) => void;
export type mParticleInstanceType = typeof mParticle;
/*type mparticleInstanceType = typeof mParticleInstance;
interface MparticleInstanceTypeInterface extends mparticleInstanceType {
  getInstance<
    Events = DefaultEvent,
    ScreenEvents = DefaultEvent,
  >(): MparticlePlugin<Events, ScreenEvents>;
}
export declare class mParticleInstance {
  constructor(instanceName?: string);
}*/
