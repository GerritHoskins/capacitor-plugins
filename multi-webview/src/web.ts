import { WebPlugin } from '@capacitor/core';

import type {
  CreateWebviewOptions,
  ExecuteJavaScriptOptions,
  ExecuteJavaScriptResult,
  FocusedWebviewResult,
  ListWebviewsResult,
  LoadUrlOptions,
  MultiWebviewPlugin,
  SendMessageOptions,
  SetFocusedWebviewOptions,
  SetWebviewFrameOptions,
  WebviewIdentifier,
} from './definitions';

export class MultiWebviewWeb extends WebPlugin implements MultiWebviewPlugin {
  async createWebview(_options: CreateWebviewOptions): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async setFocusedWebview(_options: SetFocusedWebviewOptions): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getFocusedWebview(): Promise<FocusedWebviewResult> {
    throw this.unimplemented('Not implemented on web.');
  }

  async hideWebview(_options: WebviewIdentifier): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async showWebview(_options: WebviewIdentifier): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async destroyWebview(_options: WebviewIdentifier): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async loadUrl(_options: LoadUrlOptions): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async listWebviews(): Promise<ListWebviewsResult> {
    throw this.unimplemented('Not implemented on web.');
  }

  async setWebviewFrame(_options: SetWebviewFrameOptions): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async executeJavaScript(
    _options: ExecuteJavaScriptOptions,
  ): Promise<ExecuteJavaScriptResult> {
    throw this.unimplemented('Not implemented on web.');
  }

  async sendMessage(_options: SendMessageOptions): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }
}
