export interface AdidPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
