export interface AppsflyerUidPlugin {
  getUID(options: { uid: string }): Promise<{ uid: string }>;
}
