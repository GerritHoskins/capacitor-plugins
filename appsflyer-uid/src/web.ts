import { WebPlugin } from '@capacitor/core';

import type { AppsflyerUidPlugin } from './definitions';

export class AppsflyerUidWeb extends WebPlugin implements AppsflyerUidPlugin {
  async getUID(): Promise<{ uid: string }> {
    console.log('AppsflyerUidPlugin getUID');
    return this.getUID();
  }
}
