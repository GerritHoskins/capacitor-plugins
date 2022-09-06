import CleverTap from 'clevertap-web-sdk/clevertap';

import type { ClevertapPlugin, Region } from './definitions';

export declare class ClevertapWeb extends CleverTap implements ClevertapPlugin {
  getRegion(): Region;

  getCleverTapID(): string | null;

  raiseNotificationClicked: () => void;

  clear(): void;

  init(accountId: string, region?: Region, targetDomain?: string): void;

  logout(): void;

  pageChanged(): void;

  setLogLevel(logLevel: 0 | 1 | 2 | 3): void;
}
