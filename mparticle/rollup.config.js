export default {
  input: 'dist/esm/index.js',
  output: [
    {
      file: 'dist/plugin.js',
      format: 'iife',
      name: 'capacitorMparticle',
      globals: {
        '@capacitor/core': 'capacitorExports',
        '@capacitor/cli': 'capacitorExports',
        '@mparticle/web-sdk': 'capacitorExports',
      },
      sourcemap: true,
      inlineDynamicImports: true,
    },
    {
      file: 'dist/plugin.cjs.js',
      format: 'cjs',
      sourcemap: true,
      inlineDynamicImports: true,
    },
  ],
  external: ['@capacitor/core', '@capacitor/cli', '@mparticle/web-sdk'],
};
