export default {
  input: 'dist/esm/index.js',
  output: [
    {
      file: 'dist/plugin.js',
      format: 'iife',
      name: 'capacitorClevertap',
      globals: {
        '@capacitor/core': 'capacitorExports',
        '@capacitor/cli': 'capacitorExports',
        'clevertap-web-sdk/clevertap': 'CleverTap',
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
  external: [
    '@capacitor/core',
    '@capacitor/cli',
    '@types/clevertap-web-sdk',
    'clevertap-web-sdk/clevertap',
  ],
};
