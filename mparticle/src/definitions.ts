import type { PrivacyConsentState } from '@mparticle/web-sdk';

export type mParticleInitListener = (info: any) => any;

export interface MparticlePlugin {
  initConfig(options: InitConfig): Promise<ConfigResponse>;
  init(options: {
    key?: string;
    configs: { key: string; config: any };
  }): Promise<any>;
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
}
export type InitConfig = {
  isDevelopmentMode?: boolean;
  planID?: string;
  planVer?: number;
  logLevel?: string;
  identifyRequest?: any;
  identityCallback?: () => void;
};
export type ConfigResponse = {
  isDevelopmentMode?: boolean;
  dataPlan?: {
    planId?: string;
    planVersion?: number;
  };
  identifyRequest?: any;
  logLevel?: string;
  identityCallback?: () => void;
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
