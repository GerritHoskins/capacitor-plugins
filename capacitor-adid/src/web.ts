import { WebPlugin } from '@capacitor/core';

import type { AdidPlugin } from './definitions';

export class AdidWeb extends WebPlugin implements AdidPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
