import { WebPlugin } from '@capacitor/core';

import type { AdidPlugin } from './definitions';

export class AdidWeb extends WebPlugin implements AdidPlugin {
  getId(): Promise<{ id: string; isDummy: boolean }> {
    return Promise.reject('web not implemented');
  }
}
