/// <reference types="@capacitor/cli" />
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
  identifyUser(identifier?: Identifier): Promise<any>;
  setUserAttribute(attribute: Attribute): Promise<void>;
  setUserAttributes(attributes: Attribute[]): Promise<void>;
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
  attributes?: any;
};
export type DefaultEvent = { name: string; data?: any };
export type GDPRConsents = Record<string, Consent>;
