/// <reference types="@types/clevertap-web-sdk" />
import { WebPlugin } from '@capacitor/core';
import type clevertap from 'clevertap-web-sdk/clevertap';

import type {
  ClevertapPlugin,
  DeliveredNotifications,
  InitOptions,
} from './definitions';

declare let cleverTAP: clevertap;

export class ClevertapWeb extends WebPlugin implements ClevertapPlugin {
  private scriptUrl =
    'https://d2r1yp2w7bby2u.cloudfront.net/js/clevertap.min.js';
  private scriptLoaded = false;

  constructor() {
    super();
  }

  private async loadScript(): Promise<boolean> {
    return new Promise(resolve => {
      if (!this.scriptLoaded) {
        if (typeof window !== undefined) {
          const script = document.createElement('script');
          script.src = this.scriptUrl;
          script.type = 'text/javascript';
          script.defer = true;
          script.async = true;
          script.id = 'clevertap_web_sdk';
          script.onload = () => resolve(true);
          const s = document.getElementsByTagName('script')[0];
          s.parentNode && s
            ? s.parentNode.insertBefore(script, s)
            : document.head.appendChild(script);
        } else {
          resolve(false);
        }
      } else {
        resolve(true);
      }
    });
  }

  async init(options: InitOptions): Promise<void> {
    this.scriptLoaded = await this.loadScript();
    if (this.scriptLoaded) {
      cleverTAP = window.clevertap;
      cleverTAP.init(options.accountId, options.region, options.targetDomain);
    } else {
      return Promise.reject('failed to init clevertap web sdk.');
    }
  }

  async cleverTap(): Promise<any> {
    if (this.scriptLoaded) return cleverTAP;
    return undefined;
  }

  createChannel(): Promise<void> {
    return Promise.reject(this.unavailable('not implemented on web'));
  }

  getClevertapId(): Promise<string | null> {
    return Promise.reject(this.unavailable('not implemented on web'));
  }

  getDeliveredNotifications(): Promise<DeliveredNotifications> {
    return Promise.reject(this.unavailable('not implemented on web'));
  }

  onUserLogin(): Promise<void> {
    return Promise.reject(this.unavailable('not implemented on web'));
  }

  pushEvent(): Promise<void> {
    return Promise.reject(this.unavailable('not implemented on web'));
  }

  registerFBM(): Promise<void> {
    return Promise.reject(this.unavailable('not implemented on web'));
  }

  removeDeliveredNotifications(): Promise<void> {
    return Promise.reject(this.unavailable('not implemented on web'));
  }
}

/*
export class ClevertapWeb extends CleverTap {
  public clevertapClient: CleverTap;
  // enablePersonalization: any;
  // event: any;
  // notifications: any;
  // onUserLogin: any;
  // privacy: any;
  // profile: any;
  // session: any;
  // spa: any;
  // user: any;

  constructor() {
    super();
    this.clevertapClient = new CleverTap();
    // this.enablePersonalization = this.clevertapClient.enablePersonalization;
    // this.event = this.clevertapClient.event;
    // this.notifications = this.clevertapClient.notifications;
    // this.onUserLogin = this.clevertapClient.onUserLogin;
    // this.privacy = this.clevertapClient.privacy;
    // this.profile = this.clevertapClient.profile;
    // this.session = this.clevertapClient.session;
    // this.spa = this.clevertapClient.spa;
    // this.user = this.clevertapClient.user;
  }
  client(): CleverTap {
    return this.clevertapClient;
  }
  getCleverTapID(): string | null {
    return this.clevertapClient.getCleverTapID();
  }
  raiseNotificationClicked = (): void =>
    this.clevertapClient.raiseNotificationClicked();

  clear(): void {
    this.clevertapClient.clear();
  }
  init(accountId: string, region?: Region, targetDomain?: string): void {
    this.clevertapClient.init(accountId, region, targetDomain);
  }
  logout(): void {
    this.clevertapClient.logout();
  }
  pageChanged(): void {
    this.clevertapClient.pageChanged();
  }
  setLogLevel(logLevel: 0 | 1 | 2 | 3): void {
    this.clevertapClient.setLogLevel(logLevel);
  }
}*/
