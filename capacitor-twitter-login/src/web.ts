import { WebPlugin } from '@capacitor/core';

import type { TwitterLoginPlugin } from './definitions';

export class TwitterLoginWeb extends WebPlugin implements TwitterLoginPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
