import { WebPlugin } from '@capacitor/core';

import type { InAppBrowserPlugin, IabOptions } from './definitions';

export class InAppBrowserWeb extends WebPlugin implements InAppBrowserPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    return options;
  }
  async open(options: IabOptions): Promise<void> {
    window.open(options.url, options.target);
  }
}
