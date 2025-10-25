import { WebPlugin } from '@capacitor/core';

import type {
  AllWebviewsResult,
  CreateWebviewOptions,
  ExecuteJavaScriptOptions,
  ExecuteJavaScriptResult,
  FocusedWebviewResult,
  GetWebviewsByUrlOptions,
  ListWebviewsResult,
  LoadUrlOptions,
  MultiWebviewPlugin,
  SendMessageOptions,
  SetFocusedWebviewOptions,
  SetWebviewFrameOptions,
  WebviewIdentifier,
  WebviewInfo,
  WebviewsByUrlResult,
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

  async getWebviewInfo(_options: WebviewIdentifier): Promise<WebviewInfo> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getAllWebviews(): Promise<AllWebviewsResult> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getWebviewsByUrl(
    _options: GetWebviewsByUrlOptions,
  ): Promise<WebviewsByUrlResult> {
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
