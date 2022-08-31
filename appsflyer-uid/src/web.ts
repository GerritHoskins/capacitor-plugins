import { WebPlugin } from '@capacitor/core';

import type { AppsflyerUidPlugin } from './definitions';

export class AppsflyerUidWeb extends WebPlugin implements AppsflyerUidPlugin {
  getUID(): Promise<{ uid: string }> {
    return Promise.reject(this.unimplemented(
        'AppsflyerUidPlugin currently not implemented for web',
    ));
  }
}
