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
  setUserAttribute(
    attributeName: string,
    attributeValue: string,
  ): Promise<void>;
  setGDPRConsent(consents: Record<string, Consent>): void;
  getGDPRConsent(consents: string[]): Record<string, boolean> | void;
  getMPID(): Promise<string | void>;
  trackEvent: Events;
  trackPageView: ScreenEvents;
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
export type Events = DefaultEvent;
export type ScreenEvents = DefaultEvent;
export type mParticleReadyListener = (event: MparticleReadyEvent) => void;
export type DefaultEvent = (name: string, data?: any) => void;
