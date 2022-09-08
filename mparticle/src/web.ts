import { WebPlugin } from '@capacitor/core';
import type {
  EventType,
  PrivacyConsentState,
  UserIdentities,
  MPConfiguration,
} from '@mparticle/web-sdk';
import mParticle from '@mparticle/web-sdk';

import type { MparticlePlugin, Identifier } from './definitions';

export class MparticleWeb extends WebPlugin implements MparticlePlugin {
  async init(options: { key: string; config: any }): Promise<any> {
    return mParticle.init(options.key, options.config as MPConfiguration);
  }
  async identifyUser(options: { identifier: Identifier }): Promise<void> {
    if (!mParticle.isInitialized()) return;
    const { email, customerId, other } = options.identifier;

    return new Promise(resolve => {
      if (!mParticle.isInitialized()) return resolve();
      const userIdentities = {} as UserIdentities;
      if (email) userIdentities.email = email;
      if (customerId) userIdentities.customerid = customerId;
      if (other) userIdentities.other = other;

      mParticle.Identity.identify(
        {
          userIdentities: userIdentities,
        },
        () => resolve(),
      );
    });
  }
  async setUserAttribute(options: {
    attributeName: string;
    attributeValue: string;
  }): Promise<void> {
    const user = mParticle.Identity.getCurrentUser();
    if (!user) return Promise.resolve();

    user.setUserAttribute(options.attributeName, options.attributeValue);
    return Promise.resolve();
  }
  setGDPRConsent(options: {
    consents: Record<string, PrivacyConsentState>;
  }): void {
    if (!mParticle.isInitialized()) return;
    const user = mParticle.Identity.getCurrentUser();
    if (!user) return;

    const consentState = mParticle.Consent.createConsentState();

    for (const [key, value] of Object.entries(options.consents)) {
      consentState.addGDPRConsentState(
        key,
        mParticle.Consent.createGDPRConsent(
          value.Consented || false,
          value.Timestamp || Date.now(),
          value.ConsentDocument || '',
          value.Location || '',
          value.HardwareId || '',
        ),
      );
    }

    user.setConsentState(consentState);
  }
  getGDPRConsent(options: {
    consents: string[];
  }): Record<string, boolean> | void {
    if (!mParticle.isInitialized()) return;
    const user = mParticle.Identity.getCurrentUser();
    if (!user) return;

    const consentState = user.getConsentState();
    if (!consentState) return;

    const gdprConsentState = consentState.getGDPRConsentState();

    return options.consents.reduce((consentsAcc, consent) => {
      const state = gdprConsentState[consent];
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      consentsAcc[consent] = state ? state.Consented : false;

      return consentsAcc;
    }, {});
  }
  async getMPID(): Promise<string | void> {
    if (!mParticle.isInitialized()) return Promise.resolve();
    const user = mParticle.Identity.getCurrentUser();
    if (!user) return Promise.resolve();

    return Promise.resolve(user.getMPID());
  }
  async logEvent(options: {
    eventName: string;
    eventType: EventType | number;
    eventProperties: any;
  }): Promise<any> {
    if (!mParticle.isInitialized()) return;
    return mParticle.logEvent(
      options.eventName,
      options.eventType,
      options.eventProperties,
    );
  }
  async logPageView(options: {
    pageName: string;
    pageLink: string;
  }): Promise<any> {
    if (!mParticle.isInitialized()) return;
    return mParticle.logPageView(options.pageName, { page: options.pageLink });
  }
  async loginUser(options: {
    email: string;
    customerId: string;
  }): Promise<any> {
    if (!mParticle.isInitialized()) return;
    return mParticle.Identity.login(
      this.identityRequest(options.email, options.customerId),
    );
  }
  async logoutUser(_options: any): Promise<any> {
    if (!mParticle.isInitialized()) return;
    const identityoptionsback = (result: any) => {
      if (result.getUser()) {
        console.log('logging out of mParticle', _options);
      }
    };
    return mParticle.Identity.logout({} as any, identityoptionsback);
  }
  async registerUser(options: {
    email: string;
    customerId: string;
    userAttributes: any;
  }): Promise<any> {
    return mParticle.Identity.login(
      this.identityRequest(options.email, options.customerId),
      function (result: any) {
        if (!result) return;
        const currentUser = result.getUser();
        for (const [key, value] of Object.entries(options.userAttributes)) {
          if (key && value) currentUser.setUserAttribute(key, value);
        }
      },
    );
  }

  public get currentUser(): mParticle.User {
    return mParticle.Identity.getCurrentUser();
  }
  private identityRequest(email: string, customerId: string): any {
    return {
      userIdentities: {
        email,
        customerid: customerId,
      },
    };
  }
}
