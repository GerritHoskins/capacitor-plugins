import { WebPlugin } from '@capacitor/core';
import clevertap from 'clevertap-web-sdk';

import type { ClevertapPlugin, InitOptions } from './definitions';

export class ClevertapWeb extends WebPlugin implements ClevertapPlugin {
  private scriptUrl =
    'https://d2r1yp2w7bby2u.cloudfront.net/js/clevertap.min.js';
  private scriptLoaded = false;

  protected privacy = {};
  protected event = {};
  protected profile = {};
  protected onUserLogin = {};
  protected notifications = {};

  constructor() {
    super();
    this.privacy = clevertap.privacy;
    this.event = clevertap.event;
    this.profile = clevertap.profile;
    this.onUserLogin = clevertap.user;
    this.notifications = clevertap.notifications;
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
        clevertap.init(options.accountId, options.region, options.targetDomain);
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
      return Promise.resolve();
    } else {
      return Promise.reject('failed to load clevertap web sdk.');
    }
  }
  async getCleverTapID(): Promise<string | null> {
    return clevertap.getCleverTapID();
  }
  async setLogLevel(logLevel: 0 | 1 | 2 | 3): Promise<void> {
    return clevertap.setLogLevel(logLevel);
  }
  async logout(): Promise<void> {
    return clevertap.logout();
    // cleverTap(): CleverTap;
  }
  async clear(): Promise<void> {
    return clevertap.clear();
  }
  async pageChanged(): Promise<void> {
    return clevertap.pageChanged();
  }
  async raiseNotificationClicked(): Promise<void> {
    return clevertap.raiseNotificationClicked();
  }
}
