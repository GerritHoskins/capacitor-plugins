import { WebPlugin } from '@capacitor/core';

import type {
  ClevertapInstance, ClevertapPlugin, DeliveredNotifications, UserProfile,
  Channel, PushEvent,
} from './definitions';

export declare class Clevertap extends WebPlugin implements ClevertapPlugin {
  /**
   * Get activity state of plugin and Clevertap SDK
   */
  isReady(): Promise<ClevertapInstance>;
  /**
   * Get Clevertap generated user id
   */
  getClevertapId(): Promise<ClevertapInstance>;
  /**
   * Register the app to receive push notifications.
   */
  registerFBM(): Promise<void>;
  /**
   * Get a list of notifications that are visible on the notifications screen.
   */
  getDeliveredNotifications(): Promise<DeliveredNotifications>;
  /**
   * Remove the specified notifications from the notifications screen.
   */
  removeDeliveredNotifications(delivered: DeliveredNotifications): Promise<void>;
  /**
   * Create a notification channel.
   */
  createChannel(channel: Channel): Promise<void>;
  /**
   * Push user profile on logon
   */
  onUserLogin(profile: UserProfile): Promise<void>;
  /**
   * Push user profile on logon
   */
  pushEvent(event: PushEvent): Promise<void>
}
