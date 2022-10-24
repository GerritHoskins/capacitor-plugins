import { WebPlugin } from '@capacitor/core';
import type { GDPRConsentState, UserIdentities } from '@mparticle/web-sdk';
import mParticle from '@mparticle/web-sdk';

import type {
  Attribute,
  DefaultEvent,
  GDPRConsents,
  Identifier,
  MparticlePlugin,
  Product,
} from './definitions';

export class MparticleWeb extends WebPlugin implements MparticlePlugin {
  init(webKey: string, config: Record<string, unknown>): Promise<void> {
    return new Promise(resolve => {
      const mParticleConfig = {
        ...config,
        identityCallback: () => resolve(),
      };

      mParticle.init(webKey, mParticleConfig);
    });
  }
  identifyUser(identifier: Identifier): Promise<string> {
    if (!identifier) return Promise.resolve('');
    const { email, customerId, other } = identifier;

    return new Promise(resolve => {
      if (!mParticle.isInitialized()) return resolve('');
      const userIdentities: UserIdentities = {};
      if (email) userIdentities.email = email;
      if (customerId) userIdentities.customerid = customerId;
      if (other) userIdentities.other = other;

      mParticle.Identity.identify(
        {
          userIdentities: userIdentities,
        },
        () => resolve(userIdentities.customerid || ''),
      );
    });
  }
  setUserAttribute(options: {
    attribute: Attribute;
    userId?: string;
  }): Promise<void> {
    if (!mParticle.isInitialized()) return Promise.resolve();

    const user: mParticle.User = this.getMParticleUser(options.userId);
    if (!user) return Promise.resolve();

    const { name, value } = options.attribute;

    user.setUserAttribute(name, value);
    return Promise.resolve();
  }
  setUserAttributes(options: {
    attributes: Attribute[];
    userId?: string;
  }): Promise<void> {
    if (!mParticle.isInitialized()) return Promise.resolve();

    const user: mParticle.User = this.getMParticleUser(options.userId);
    if (!user) return Promise.resolve();

    options.attributes.forEach(attribute =>
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
  async trackPurchase(product: Product): Promise<void> {
    if (!mParticle.isInitialized()) return;

    const productToLog = this.createMParticleProduct(product);

    const transactionAttributes = {
      Id: product.transactionId,
      Revenue: product.price,
    };

    return mParticle.eCommerce.logProductAction(
      mParticle.ProductActionType.Purchase,
      productToLog,
      product.customAttributes || undefined,
      undefined,
      transactionAttributes,
      undefined,
    );
  }
  private createMParticleProduct(productData: Product) {
    return mParticle.eCommerce.createProduct(
      productData.name,
      productData.sku,
      productData.price,
      productData.quantity,
      undefined,
      undefined,
      undefined,
      undefined,
      productData.customAttributes,
    );
  }
  private getMParticleUser(userId?: string): mParticle.User {
    if (userId) return mParticle.Identity.getUser(userId);
    return mParticle.Identity.getCurrentUser();
  }
}
