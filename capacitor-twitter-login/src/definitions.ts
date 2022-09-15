export interface TwitterLoginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
