export type mParticleInitListener = (info: any) => any;

export interface MparticlePlugin {
  echo(options: { value: string }): Promise<{ value: string }>;

  mParticleConfig(call: {
    isDevelopmentMode?: boolean;
    planID?: string;
    planVer?: number;
    logLevel?: string;
    identifyRequest?: any;
    identityCallback?: () => void;
  }): Promise<MparticleConfigType>;

  mParticleInit(call: { key: string; mParticleConfig: any }): Promise<any>;

  loginMparticleUser(call: { email: string; customerId: string }): Promise<any>;

  logoutMparticleUser(call?: any): Promise<any>;

  logMparticleEvent(call: {
    eventName: string;
    eventType: any;
    eventProperties: any;
  }): Promise<any>;
  logMparticlePageView(call: { pageName: string; pageLink: any }): Promise<any>;

  setUserAttribute(call: {
    attributeName: string;
    attributeValue: string;
  }): Promise<any>;
  setUserAttributeList(call: {
    attributeName: string;
    attributeValues: any;
  }): Promise<any>;

  registerMparticleUser(call: {
    email: string;
    customerId: string;
    userAttributes: any;
  }): Promise<any>;
}

export enum MparticleEventType {
  Navigation = 1,
  Location = 2,
  Search = 3,
  Transaction = 4,
  UserContent = 5,
  UserPreference = 6,
  Social = 7,
  Other = 8,
}

export type MparticleConfigType = {
  isDevelopmentMode?: boolean;
  dataPlan?: {
    planId?: string;
    planVersion?: number;
  };
  identifyRequest?: any;
  logLevel?: string;
  identityCallback?: () => void;
};
