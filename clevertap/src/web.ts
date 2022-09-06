import { WebPlugin } from '@capacitor/core';
import clevertap from 'clevertap-web-sdk';

import type {
  ClevertapInstance,
  ClevertapPlugin,
  DeliveredNotifications,
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

  removeDeliveredNotifications(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  createChannel(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  onUserLogin(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  pushEvent(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }
}
