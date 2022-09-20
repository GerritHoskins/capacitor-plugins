export interface AdidPlugin {
  getId(): Promise<{ id: string; isDummy: boolean }>;
}
