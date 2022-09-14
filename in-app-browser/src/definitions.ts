import type { PluginListenerHandle } from '@capacitor/core';

export interface InAppBrowserPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  open(options: IabOptions): Promise<void>;
  close(): Promise<void>;
  show(): Promise<void>;
  hide(): Promise<void>;
  handleNavigationEvent(options: { allow: boolean }): Promise<void>;
  addListener(
    eventName:
      | 'pageLoaded'
      | 'updateSnapshot'
      | 'progress'
      | 'navigationHandler',
    listenerFunc: (...args: any[]) => void,
  ): PluginListenerHandle;
}
export interface IabOptions {
  url?: string;
  target?: '_blank' | '_parent' | '_self';
  browserType?: 'DEFAULT' | 'SYSTEM' | 'SURVEY';
  browserEventCallbacks?: {
    type: 'start' | 'stop' | 'error' | 'exit' | 'custom';
    action: (event: IabEvent) => void;
  };
  webViewDimensions?: IabWebViewDimensions;
  platformOptions?: IabPlatformOptions;
}
export interface IabEvent extends Event {
  type: string;
  url: string;
  code: number;
  message: string;
}
export interface IabPlatformOptions {
  hidden: boolean;
  showUrlBar: boolean;
  zoom: boolean;
  toolbarPosition: 'TOP' | 'BOTTOM';
  toolbarColor: string;
  closeButtonColor: string;
  hardwareBackButton: boolean;
  hideNavigationButtons: boolean;
  android?: Record<string, unknown>;
  ios?: Record<string, unknown>;
}
interface IabWebViewDimensions {
  width: number;
  height: number;
  x: number;
  y: number;
}
