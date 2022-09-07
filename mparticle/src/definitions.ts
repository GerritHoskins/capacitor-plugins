/// <reference types="@capacitor/cli" />
import type { PluginListenerHandle } from '@capacitor/core';
import type { PrivacyConsentState, MPConfiguration } from '@mparticle/web-sdk';

declare module '@capacitor/cli' {
  export interface PluginsConfig {
    MparticlePlugin: {
      appId?: string;
      config?: MPConfiguration;
    };
  }
}

export interface MparticlePlugin {
  initConfig(options: MPConfiguration): Promise<MPConfiguration>;
  init(options: { key?: string; configs: MPConfiguration }): Promise<any>;
  identifyUser(options: { identifier: Identifier }): Promise<void>;
  setUserAttribute(options: {
    attributeName: string;
    attributeValue: string;
  }): Promise<void>;
  setGDPRConsent(options: {
    consents: Record<string, PrivacyConsentState>;
  }): void;
  getGDPRConsent(options: {
    consents: string[];
  }): Record<string, boolean> | void;
  getMPID(): Promise<string | void>;
  logEvent(options: {
    eventName: string;
    eventType: any;
    eventProperties: any;
  }): Promise<any>;
  logPageView(options: { pageName: string; pageLink: any }): Promise<any>;
  loginUser(options: { email: string; customerId: string }): Promise<any>;
  logoutUser(options?: any): Promise<any>;
  registerUser(options: {
    email: string;
    customerId: string;
    userAttributes: any;
  }): Promise<any>;
  addListener(
    eventName: 'mParticleReady',
    listenerFunc: mParticleReadyListener,
  ): Promise<PluginListenerHandle> & PluginListenerHandle;
}
export type Identifier = {
  email?: string;
  customerId?: string;
  other?: string;
};
export type mParticleReadyListener = (event: MparticleReadyEvent) => void;
export interface MparticleReadyEvent {
  ready: boolean;
}
