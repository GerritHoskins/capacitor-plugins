import { WebPlugin } from '@capacitor/core';

import type { AdidPlugin } from './definitions';

export class AdidWeb extends WebPlugin implements AdidPlugin {
  getId(): Promise<{ id: string }> {
    return Promise.reject('web not implemented');
  }
  getStatus(): Promise<{ status: string; statusId: number }> {
    return Promise.reject('web not implemented');
  }
}
