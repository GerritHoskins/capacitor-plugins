/// <reference types="@capacitor/cli" />
declare module '@capacitor/cli' {
  export interface PluginsConfig {
    Clevertap?: InitOptions;
  }
}

export interface ClevertapPlugin {
  init(options?: InitOptions): Promise<any>;
  getCleverTapID(): Promise<string | null>;
}

export interface ClevertapNativePlugin {
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

export type Region = 'eu1' | 'in1' | 'sg1' | 'us1' | 'sk1';
export interface InitOptions {
  accountId: string;
  token?: string;
  region?: Region;
  targetDomain?: string;
}
export interface UserProfile {
  uid: string;
  internalId: string;
  email: string;
  clientProduct?: string;
}
export interface PushNotificationSchema {
  title?: string;
  subtitle?: string;
  body?: string;
  id: string;
  tag?: string;
  badge?: number;
  data: any;
  click_action?: string;
  link?: string;
  group?: string;
  groupSummary?: boolean;
}
export interface DeliveredNotifications {
  notifications: PushNotificationSchema[];
}
export interface Channel {
  id: string;
  name: string;
  description?: string;
  sound?: string;
  importance?: Importance;
  visibility?: Visibility;
  lights?: boolean;
  lightColor?: string;
  vibration?: boolean;
}
export interface PrivacyData {
  optOut?: boolean;
  useIP?: boolean;
}
export interface NotificationData {
  titleText: string;
  bodyText: string;
  okButtonText: string;
  rejectButtonText: string;
  okButtonColor?: string;
  skipDialog?: boolean;
  askAgainTimeInSeconds?: number;
  okCallback?: () => void;
  rejectCallback?: () => void;
  subscriptionCallback?: () => void;
  hidePoweredByCT?: boolean;
  serviceWorkerPath?: string;
  httpsPopupPath?: string;
  httpsIframePath?: string;
  apnsWebPushId?: string;
  apnsWebPushServiceUrl?: string;
}

export type Importance = 1 | 2 | 3 | 4 | 5;
export type Visibility = -1 | 0 | 1;
export type EventData = Record<string, unknown>;
export type EventName = string;
export type EventNameOrData = EventName | EventData;
