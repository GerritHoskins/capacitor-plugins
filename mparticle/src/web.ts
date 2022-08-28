import { WebPlugin } from '@capacitor/core';
import mParticle from '@mparticle/web-sdk';

import type { MparticlePlugin, MparticleConfigType } from './definitions';

export class MparticleWeb extends WebPlugin implements MparticlePlugin {
  async mParticleConfig(call: {
    isDevelopmentMode?: boolean;
    planID?: string;
    planVer?: number;
    logLevel?: string;
    identifyRequest?: any;
    identityCallback?: () => void;
  }): Promise<MparticleConfigType> {
    const mParticleConfig = {
      isDevelopmentMode: call.isDevelopmentMode || true,
      dataPlan: {
        planId: call.planID || 'master_data_plan',
        planVersion: call.planVer || 2,
      },
      identifyRequest: call.identifyRequest || undefined,
      logLevel:
        call.logLevel == 'verbose' || 'warning' || 'none'
          ? call.logLevel
          : 'verbose',
      identityCallback: call.identityCallback || undefined,
    };
    return mParticleConfig;
  }

  async mParticleInit(call: {
    key: string;
    mParticleConfig: any;
  }): Promise<any> {
    return mParticle.init(call.key, call.mParticleConfig as any);
  }

  async loginMparticleUser(call: {
    email: string;
    customerId: string;
  }): Promise<any> {
    return mParticle.Identity.login(
      this.identityRequest(call.email, call.customerId),
    );
  }

  async logoutMparticleUser(_call: any): Promise<any> {
    const identityCallback = (result: any) => {
      if (result.getUser()) {
        console.log('logging out of mParticle', _call);
      }
    };
    return mParticle.Identity.logout({} as any, identityCallback);
  }

  async registerMparticleUser(call: {
    email: string;
    customerId: string;
    userAttributes: any;
  }): Promise<any> {
    return mParticle.Identity.login(
      this.identityRequest(call.email, call.customerId),
      function (result: any) {
        if (!result) return;
        const currentUser = result.getUser();
        for (const [key, value] of Object.entries(call.userAttributes)) {
          if (key && value) currentUser.setUserAttribute(key, value);
        }
      },
    );
  }

  async logMparticleEvent(call: {
    eventName: string;
    eventType: any;
    eventProperties: any;
  }): Promise<any> {
    return mParticle.logEvent(
      call.eventName,
      call.eventType,
      call.eventProperties,
    );
  }

  async logMparticlePageView(call: {
    pageName: string;
    pageLink: string;
  }): Promise<any> {
    return mParticle.logPageView(call.pageName, { page: call.pageLink });
  }

  async setUserAttribute(call: {
    attributeName: string;
    attributeValue: string;
  }): Promise<any> {
    return this.currentUser?.setUserAttribute(
      call.attributeName,
      call.attributeValue,
    );
  }

  async setUserAttributeList(call: {
    attributeName: string;
    attributeValues: any;
  }): Promise<any> {
    return this.currentUser.setUserAttributeList(
      call.attributeName,
      call.attributeValues,
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

  async echo(options: { value: string }): Promise<{ value: string }> {
    return options;
  }
}
