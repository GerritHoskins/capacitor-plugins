import type { ITwitterPlugin } from './definitions';

export declare class ITwitter implements ITwitterPlugin {
  isLogged(): Promise<{ in: boolean; out: boolean }>;
  login(): Promise<{
    authToken: string;
    authTokenSecret: string;
    userName: string;
    userID: string;
  }>;
  logout(): Promise<void>;
}
