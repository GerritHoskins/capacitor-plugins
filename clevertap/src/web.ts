import { WebPlugin } from '@capacitor/core';
import CleverTap from 'clevertap-web-sdk/clevertap';

import type { ClevertapPlugin, DeliveredNotifications } from './definitions';

export class ClevertapWeb extends WebPlugin implements ClevertapPlugin {
  constructor() {
    super();
  }

  cleverTap(): CleverTap {
    return new CleverTap();
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
