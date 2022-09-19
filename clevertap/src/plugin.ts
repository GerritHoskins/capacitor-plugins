import type {
  Channel,
  ClevertapNativePlugin,
  ClevertapPlugin,
  DeliveredNotifications,
  EventNameOrData,
  InitOptions,
  NotificationData,
  PrivacyData,
  UserProfile,
} from './definitions';

export declare class ClevertapNative
  implements ClevertapPlugin, ClevertapNativePlugin
{
  init(options?: InitOptions): Promise<any>;
  getCleverTapID(): Promise<string | null>;
  getDeliveredNotifications(): Promise<DeliveredNotifications>;
  removeDeliveredNotifications(options: {
    delivered: DeliveredNotifications;
  }): Promise<void>;
  createChannel(options: { channel: Channel }): Promise<void>;
  onUserLogin(options: { profile: UserProfile }): Promise<void>;
  pushEvent(evtName: string, evtNameOrData: EventNameOrData): void;
  pushNotification(notificationData: NotificationData): void;
  pushPrivacy(privacyArr: PrivacyData): void;
  pushUser(profileData: any): void;
}
