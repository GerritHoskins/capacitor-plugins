import { WebPlugin } from '@capacitor/core';
import type { UserIdentities, GDPRConsentState } from '@mparticle/web-sdk';
import mParticle from '@mparticle/web-sdk';

import type {
  Identifier,
  MparticlePlugin,
  Attribute,
  GDPRConsents,
  DefaultEvent,
} from './definitions';

export class MparticleWeb extends WebPlugin implements MparticlePlugin {
  init(key: string, config: Record<string, unknown>): Promise<void> {
    return new Promise(resolve => {
      const mParticleConfig = {
        ...config,
        identityCallback: () => resolve(),
      };

      mParticle.init(key, mParticleConfig);
    });
  }
  identifyUser(identifier: Identifier): Promise<any> {
    if (!identifier) return Promise.resolve();
    const { email, customerId, other } = identifier;

    return new Promise(resolve => {
      if (!mParticle.isInitialized()) return resolve(undefined);
      const userIdentities: UserIdentities = {};
      if (email) userIdentities.email = email;
      if (customerId) userIdentities.customerid = customerId;
      if (other) userIdentities.other = other;

      mParticle.Identity.identify(
        {
          userIdentities: userIdentities,
        },
        () => resolve(userIdentities.customerid),
      );
    });
  }
  setUserAttribute(attribute: Attribute): Promise<void> {
    if (!mParticle.isInitialized()) return Promise.resolve();

    const user = mParticle.Identity.getCurrentUser();
    if (!user) return Promise.resolve();

    user.setUserAttribute(attribute.name, attribute.value);
    return Promise.resolve();
  }
  setUserAttributes(attributes: Attribute[]): Promise<void> {
    if (!mParticle.isInitialized()) return Promise.resolve();

    const user = mParticle.Identity.getCurrentUser();
    if (!user) return Promise.resolve();

    attributes.forEach(attribute =>
      user.setUserAttribute(attribute.name, attribute.value),
    );

    return Promise.resolve();
  }
  setGDPRConsent(gdprConsents: GDPRConsents): void {
    if (!mParticle.isInitialized()) return;

    const user = mParticle.Identity.getCurrentUser();
    if (!user) return;

    const consentState = mParticle.Consent.createConsentState();

    for (const [key, value] of Object.entries(gdprConsents)) {
      consentState.addGDPRConsentState(
        key,
        mParticle.Consent.createGDPRConsent(
          value.consented || false,
          value.timestamp || Date.now(),
          value.consentDocument || '',
          value.location || '',
          value.hardwareId || '',
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

    const gdprConsentState: GDPRConsentState =
      consentState.getGDPRConsentState();

    return options.consents.reduce((consentsAcc: any, consent) => {
      const state = gdprConsentState[consent];
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
  trackEvent(event: DefaultEvent): void {
    if (!mParticle.isInitialized()) return;

    mParticle.logEvent(event.name, mParticle.EventType.Other, event.data);
  }
  trackPageView(event: DefaultEvent): void {
    if (!mParticle.isInitialized()) return;

    mParticle.logPageView(event.name, event.data);
  }
  loginUser(identifier?: Identifier): Promise<any> {
    if (!identifier) return Promise.resolve();
    const { customerId } = identifier;

    return new Promise(resolve => {
      if (!mParticle.isInitialized()) return Promise.resolve();
      mParticle.Identity.login(
        { userIdentities: { customerid: customerId } as UserIdentities },
        result => resolve(result.getUser().getMPID),
      );
    });
  }
  logoutUser(): Promise<any> {
    return new Promise(resolve => {
      if (!mParticle.isInitialized()) return Promise.resolve();
      mParticle.Identity.logout({}, result => resolve(result.getUser()));
    });
  }
}
