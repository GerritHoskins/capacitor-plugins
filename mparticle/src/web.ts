import { WebPlugin } from '@capacitor/core';

import type { MparticlePlugin } from './definitions';

export class MparticleWeb extends WebPlugin implements MparticlePlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
