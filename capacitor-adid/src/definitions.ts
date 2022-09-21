export interface AdidPlugin {
  getId(
    delay?: string | number,
  ): Promise<{ id: string; limitedAdTracking?: boolean }>;
  getStatus(): Promise<{ status: string; statusId: number }>;
}
