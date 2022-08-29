import { WebPlugin } from '@capacitor/core';
import clevertap from 'clevertap-web-sdk';

import type {
  ClevertapInstance,
  ClevertapPlugin,
  DeliveredNotifications,
  UserProfile,
  Channel,
  PushEvent,
} from './definitions';

export class ClevertapWeb extends WebPlugin implements ClevertapPlugin {
  isReady(): Promise<ClevertapInstance> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getClevertapId(): Promise<ClevertapInstance> {
    return { clevertapId: clevertap.getCleverTapID() || '' };
  }

  registerFBM(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  getDeliveredNotifications(): Promise<DeliveredNotifications> {
    throw this.unimplemented('Not implemented on web.');
  }

  removeDeliveredNotifications(
    // eslint-disable-next-line @typescript-eslint/no-unused-vars,@typescript-eslint/ban-ts-comment
    // @ts-ignore
    delivered: DeliveredNotifications,
  ): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars,@typescript-eslint/ban-ts-comment
  // @ts-ignore
  createChannel(channel: Channel): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars,@typescript-eslint/ban-ts-comment
  // @ts-ignore
  onUserLogin(profile: UserProfile): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars,@typescript-eslint/ban-ts-comment
  // @ts-ignore
  pushEvent(event: PushEvent): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }
}
