import { WebPlugin } from '@capacitor/core';
import type CleverTap from 'clevertap-web-sdk/clevertap';

import type {
  ClevertapPlugin,
  DeliveredNotifications,
  InitOptions,
  NotificationData,
  PrivacyData,
  EventNameOrData,
} from './definitions';

export class ClevertapWeb extends WebPlugin implements ClevertapPlugin {
  private scriptUrl =
    'https://d2r1yp2w7bby2u.cloudfront.net/js/clevertap.min.js';
  private scriptLoaded = false;
  cleverTAP: CleverTap = {} as CleverTap;

  constructor() {
    super();
  }

  private async loadScript(options: InitOptions): Promise<boolean> {
    return new Promise(resolve => {
      if (typeof window === undefined) resolve(false);
      if (this.scriptLoaded) resolve(true);
      const script = document.createElement('script');
      script.src = this.scriptUrl;
      script.type = 'text/javascript';
      script.defer = true;
      script.async = true;
      script.id = 'clevertap_web_sdk';
      script.onload = () => {
        this.cleverTAP = window.clevertap;
        this.cleverTAP.init(
          options.accountId,
          options.region,
          options.targetDomain,
        );
        resolve(true);
      };
      const s = document.getElementsByTagName('script')[0];
      s.parentNode && s
        ? s.parentNode.insertBefore(script, s)
        : document.head.appendChild(script);
    });
  }

  async init(options: InitOptions): Promise<any> {
    this.scriptLoaded = await this.loadScript(options);
    if (this.scriptLoaded) {
      return this.cleverTap();
    } else {
      return Promise.reject('failed to init clevertap web sdk.');
    }
  }

  cleverTap(): CleverTap {
    return this.cleverTAP;
  }

  pushEvent(options: {
    evtName: string;
    evtNameOrData: EventNameOrData[];
  }): void {
    this.cleverTAP.event.push(options.evtName, options.evtNameOrData);
  }

  pushNotification(options: { notificationData: NotificationData }): void {
    this.cleverTAP.notifications.push(options.notificationData);
  }

  pushPrivacy(options: { privacyArr: PrivacyData[] }): void {
    this.cleverTAP.privacy.push(...options.privacyArr);
  }

  pushUser(options: { profileData: any[] }): void {
    this.cleverTAP.profile.push(...options.profileData);
  }

  createChannel(): Promise<void> {
    return Promise.reject(this.unavailable('not implemented on web'));
  }

  getClevertapId(): Promise<string | null> {
    return Promise.resolve(this.cleverTAP.getCleverTapID());
  }

  getDeliveredNotifications(): Promise<DeliveredNotifications> {
    return Promise.reject(this.unavailable('not implemented on web'));
  }

  onUserLogin(): Promise<void> {
    return Promise.reject(this.unavailable('not implemented on web'));
  }

  registerFBM(): Promise<void> {
    return Promise.reject(this.unavailable('not implemented on web'));
  }

  removeDeliveredNotifications(): Promise<void> {
    return Promise.reject(this.unavailable('not implemented on web'));
  }
}
