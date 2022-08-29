import { WebPlugin } from '@capacitor/core';

import type { NativeMarketPlugin } from './definitions';

export class NativeMarketWeb extends WebPlugin implements NativeMarketPlugin {
  constructor() {
    super();
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  openStoreListing(_options?: { appId: string }): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  openDevPage(_options: { devId: string }): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  openCollection(_options: { name: string }): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  openEditorChoicePage(_options: { editorChoice: string }): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  search(_options: { terms: string }): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }
}
