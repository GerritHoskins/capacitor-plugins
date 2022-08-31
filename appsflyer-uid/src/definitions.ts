import type { CapacitorException } from '@capacitor/core';

export interface AppsflyerUidPlugin {
  getUID(): Promise<{ uid: string }> | CapacitorException;
}
