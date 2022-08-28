/// <reference types="@capacitor/cli" />

export type PresentationOption = 'badge' | 'sound' | 'alert';

declare module '@capacitor/cli' {
  export interface PluginsConfig {
    PushNotifications?: {
      /**
       *   - `badge`: badge count on the app icon is updated (default value)
       *   - `sound`: the device will ring/vibrate when the push notification is received
       *   - `alert`: the push notification is displayed in a native dialog
       *
       * badge is only available for iOS.
       */
      presentationOptions: PresentationOption[];
    };
  }
}

export interface ClevertapPlugin {
  getClevertapId(): Promise<ClevertapInstance>;
  isReady(): Promise<ClevertapInstance>;
  registerFBM(): Promise<void>;
  getDeliveredNotifications(): Promise<DeliveredNotifications>;
  removeDeliveredNotifications(
    delivered: DeliveredNotifications,
  ): Promise<void>;
  createChannel(channel: Channel): Promise<void>;
  onUserLogin(profile: UserProfile): Promise<void>;
  pushEvent(event: PushEvent): Promise<void>;
}

export interface ClevertapInstance {
  clevertapId?: string;
  isReady?: boolean;
}
export interface UserProfile {
  uid: string;
  internalId: string;
  email: string;
  clientProduct?: string;
}
export interface PushEvent {
  name: string;
  value?: EventData;
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
export interface Token {
  value: string;
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
export type Importance = 1 | 2 | 3 | 4 | 5;
export type Visibility = -1 | 0 | 1;
export type EventData = Record<string, unknown>;
