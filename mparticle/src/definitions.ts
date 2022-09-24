/// <reference types="@capacitor/cli" />
import type { PluginListenerHandle } from '@capacitor/core';
import type { MPConfiguration } from '@mparticle/web-sdk';

declare module '@capacitor/cli' {
  export interface PluginsConfig {
    Mparticle: {
      key?: string;
      secret?: string;
      config?: MPConfiguration;
    };
  }
}

export interface MparticlePlugin {
  init(
    key: string,
    config: Record<string, unknown>,
    secret?: string,
  ): Promise<void>;
  identifyUser(identifier?: Identifier): Promise<void>;
  setUserAttribute(attribute: Attribute): Promise<void>;
  setGDPRConsent(gdprConsents: GDPRConsents): void;
  getGDPRConsent(options: {
    consents: string[];
  }): Record<string, boolean> | void;
  getMPID(): Promise<string | void>;
  trackEvent(event: DefaultEvent): void;
  trackPageView(event: DefaultEvent): void;
  loginUser(identifier?: Identifier): Promise<any>;
  logoutUser(options?: any): Promise<any>;
  registerUser(identifier?: Identifier): Promise<any>;
  addListener(
    eventName: 'mParticleReady',
    listenerFunc: mParticleReadyListener,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;
}
export interface MparticleReadyEvent {
  ready: boolean;
}
export type Attribute = {
  name: string;
  value: string;
};
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
export type DefaultEvent = { name: string; data?: any };
export type GDPRConsents = Record<string, Consent>;
export type mParticleReadyListener = (event: MparticleReadyEvent) => void;
