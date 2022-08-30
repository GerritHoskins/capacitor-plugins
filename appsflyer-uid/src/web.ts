import { WebPlugin } from '@capacitor/core';

import type { AppsflyerUidPlugin } from './definitions';

export class AppsflyerUidWeb extends WebPlugin implements AppsflyerUidPlugin {
  async getUID(options: { uid: string }): Promise<{ uid: string }> {
    return options;
  }
}
