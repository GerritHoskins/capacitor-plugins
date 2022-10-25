/// <reference types="@capacitor/cli" />
import type { MPConfiguration } from '@mparticle/web-sdk';

declare module '@capacitor/cli' {
  export interface PluginsConfig {
    Mparticle: {
      androidKey?: string;
      androidSecret?: string;
      iosKey?: string;
      iosSecret?: string;
      config?: MPConfiguration;
    };
  }
}

export interface MparticlePlugin {
  init(webKey: string, config: Record<string, unknown>): Promise<void>;
  identifyUser(identifier?: Identifier): Promise<string>;
  setUserAttribute(options: {
    userId?: string;
    attribute: Attribute;
  }): Promise<void>;
  setUserAttributes(options: {
    userId?: string;
    attributes: Attribute[];
  }): Promise<void>;
  setGDPRConsent(gdprConsents: GDPRConsents): void;
  getGDPRConsent(options: {
    consents: string[];
  }): Record<string, boolean> | void;
  getMPID(): Promise<string | void>;
  trackEvent(event: DefaultEvent): void;
  trackPageView(event: DefaultEvent): void;
  trackPurchase(product: Product): Promise<void>;
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
export type Product = {
  name: string;
  sku: string;
  price: number;
  quantity: number;
  transactionId: string;
  customAttributes?: any;
};
export type DefaultEvent = { name: string; data?: any };
export type GDPRConsents = Record<string, Consent>;
