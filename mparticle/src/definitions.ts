export interface MparticlePlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
