import { registerPlugin } from '@capacitor/core';

import type { ClevertapPlugin } from './definitions';

const Clevertap = registerPlugin<ClevertapPlugin>('Clevertap', {});

export * from './definitions';
export { Clevertap };
