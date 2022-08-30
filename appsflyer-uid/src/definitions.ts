export interface AppsflyerUidPlugin {
  getUID(): Promise<{ uid: string }>;
}
