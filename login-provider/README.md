# @bitburst-gmbh/login-provider

capacitor plugin for managing authentication for multiple login providers

## Install

```bash
npm install @bitburst-gmbh/login-provider
npx cap sync
```

## Example implementation

```typescript
onLoginWith(provider: ProviderName, inviteCode: string): Promise<void> {
      let options: LoginProviderOptions | undefined;
      
      // ideally retrieve these settings from the capacitor.config.ts (or .json)
      switch (provider) {
        case 'APPLE':
          options = {
            clientId: process.env.VUE_APP_APPLE_CLIENT_ID,
            redirectURI: `${window.location.origin}/surveys`,
            scope: 'email name',
          };
          break;
        case 'FACEBOOK':
          options = {
            appId: process.env.VUE_APP_FACEBOOK_APP_ID,
            xfbml: true,
            version: 'v5.0',
            permissions: ['email'],
          };
          break;
        case 'GOOGLE':
          options = {
            scope: 'profile email',
            clientId: process.env.VUE_APP_GOOGLE_CLIENT_ID,
            forceCodeForRefreshToken: true,
          };
          break;
        case 'TWITTER':
          options = {
            consumerKey: process.env.VUE_APP_TWITTER_KEY,
            consumerSecret: process.env.VUE_APP_TWITTER_CONSUMER_SECRET,
          };
          break;
        default:
          break;
      }

      if (!options) return Promise.reject();

      return LoginProvider.loginWithProvider(
        provider as ProviderName,
        options as LoginProviderOptions,
        inviteCode
      ).then(response => this.login(response));
    }
```

## API

<docgen-index>

* [`loginWithProvider(...)`](#loginwithprovider)
* [`loginWithApple(...)`](#loginwithapple)
* [`loginWithFacebook(...)`](#loginwithfacebook)
* [`loginWithGoogle(...)`](#loginwithgoogle)
* [`loginWithTwitter()`](#loginwithtwitter)
* [`logoutFromProvider(...)`](#logoutfromprovider)
* [`addListener('appStateChange', ...)`](#addlistenerappstatechange)
* [`removeAllListeners()`](#removealllisteners)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### loginWithProvider(...)

```typescript
loginWithProvider(provider: ProviderName, options?: LoginProviderOptions | undefined, inviteCode?: string | undefined) => Promise<LoginProviderPayload>
```

| Param            | Type                                                                  |
| ---------------- | --------------------------------------------------------------------- |
| **`provider`**   | <code><a href="#providername">ProviderName</a></code>                 |
| **`options`**    | <code><a href="#loginprovideroptions">LoginProviderOptions</a></code> |
| **`inviteCode`** | <code>string</code>                                                   |

**Returns:** <code>Promise&lt;<a href="#loginproviderpayload">LoginProviderPayload</a>&gt;</code>

--------------------


### loginWithApple(...)

```typescript
loginWithApple(options: LoginProviderOptions, inviteCode?: string | undefined) => Promise<LoginProviderPayload>
```

| Param            | Type                                                                  |
| ---------------- | --------------------------------------------------------------------- |
| **`options`**    | <code><a href="#loginprovideroptions">LoginProviderOptions</a></code> |
| **`inviteCode`** | <code>string</code>                                                   |

**Returns:** <code>Promise&lt;<a href="#loginproviderpayload">LoginProviderPayload</a>&gt;</code>

--------------------


### loginWithFacebook(...)

```typescript
loginWithFacebook(options: LoginProviderOptions, inviteCode?: string | undefined) => Promise<LoginProviderPayload>
```

| Param            | Type                                                                  |
| ---------------- | --------------------------------------------------------------------- |
| **`options`**    | <code><a href="#loginprovideroptions">LoginProviderOptions</a></code> |
| **`inviteCode`** | <code>string</code>                                                   |

**Returns:** <code>Promise&lt;<a href="#loginproviderpayload">LoginProviderPayload</a>&gt;</code>

--------------------


### loginWithGoogle(...)

```typescript
loginWithGoogle(options: LoginProviderOptions, inviteCode?: string | undefined) => Promise<LoginProviderPayload>
```

| Param            | Type                                                                  |
| ---------------- | --------------------------------------------------------------------- |
| **`options`**    | <code><a href="#loginprovideroptions">LoginProviderOptions</a></code> |
| **`inviteCode`** | <code>string</code>                                                   |

**Returns:** <code>Promise&lt;<a href="#loginproviderpayload">LoginProviderPayload</a>&gt;</code>

--------------------


### loginWithTwitter()

```typescript
loginWithTwitter() => Promise<LoginProviderPayload>
```

**Returns:** <code>Promise&lt;<a href="#loginproviderpayload">LoginProviderPayload</a>&gt;</code>

--------------------


### logoutFromProvider(...)

```typescript
logoutFromProvider(provider: ProviderName) => Promise<void | any>
```

| Param          | Type                                                  |
| -------------- | ----------------------------------------------------- |
| **`provider`** | <code><a href="#providername">ProviderName</a></code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### addListener('appStateChange', ...)

```typescript
addListener(eventName: 'appStateChange', listenerFunc: AppStateChangeListener) => Promise<PluginListenerHandle> & PluginListenerHandle
```

| Param              | Type                                                                      |
| ------------------ | ------------------------------------------------------------------------- |
| **`eventName`**    | <code>'appStateChange'</code>                                             |
| **`listenerFunc`** | <code><a href="#appstatechangelistener">AppStateChangeListener</a></code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt; & <a href="#pluginlistenerhandle">PluginListenerHandle</a></code>

--------------------


### removeAllListeners()

```typescript
removeAllListeners() => Promise<void>
```

--------------------


### Interfaces


#### LoginProviderOptions

| Prop                     | Type                                                             |
| ------------------------ | ---------------------------------------------------------------- |
| **`grantOfflineAccess`** | <code>boolean</code>                                             |
| **`custom`**             | <code><a href="#record">Record</a>&lt;string, unknown&gt;</code> |


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


#### AppStateChange

| Prop       | Type                                                                                                                                                                                                                                                          |
| ---------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`user`** | <code><a href="#googleloginresponse">GoogleLoginResponse</a> \| <a href="#facebookloginresponse">FacebookLoginResponse</a> \| <a href="#appleloginresponse">AppleLoginResponse</a> \| <a href="#twitterloginresponse">TwitterLoginResponse</a> \| null</code> |


#### GoogleLoginResponse

| Prop                 | Type                                              |
| -------------------- | ------------------------------------------------- |
| **`id`**             | <code>string</code>                               |
| **`email`**          | <code>string</code>                               |
| **`name`**           | <code>string</code>                               |
| **`familyName`**     | <code>string</code>                               |
| **`givenName`**      | <code>string</code>                               |
| **`imageUrl`**       | <code>string</code>                               |
| **`serverAuthCode`** | <code>string</code>                               |
| **`authentication`** | <code><a href="#googleauth">GoogleAuth</a></code> |


#### GoogleAuth

| Prop               | Type                |
| ------------------ | ------------------- |
| **`accessToken`**  | <code>string</code> |
| **`idToken`**      | <code>string</code> |
| **`refreshToken`** | <code>string</code> |


#### FacebookLoginResponse

| Prop                             | Type                                                          |
| -------------------------------- | ------------------------------------------------------------- |
| **`accessToken`**                | <code><a href="#facebookauth">FacebookAuth</a> \| null</code> |
| **`recentlyGrantedPermissions`** | <code>string[]</code>                                         |
| **`recentlyDeniedPermissions`**  | <code>string[]</code>                                         |


#### FacebookAuth

| Prop                      | Type                  |
| ------------------------- | --------------------- |
| **`applicationId`**       | <code>string</code>   |
| **`declinedPermissions`** | <code>string[]</code> |
| **`expires`**             | <code>string</code>   |
| **`isExpired`**           | <code>boolean</code>  |
| **`lastRefresh`**         | <code>string</code>   |
| **`permissions`**         | <code>string[]</code> |
| **`token`**               | <code>string</code>   |
| **`userId`**              | <code>string</code>   |


#### AppleLoginResponse

| Prop                    | Type                        |
| ----------------------- | --------------------------- |
| **`email`**             | <code>string \| null</code> |
| **`identityToken`**     | <code>string</code>         |
| **`authorizationCode`** | <code>string</code>         |


#### TwitterLoginResponse

| Prop                  | Type                |
| --------------------- | ------------------- |
| **`authToken`**       | <code>string</code> |
| **`authTokenSecret`** | <code>string</code> |
| **`userName`**        | <code>string</code> |
| **`userID`**          | <code>string</code> |


### Type Aliases


#### LoginProviderPayload

<code>{ provider: <a href="#providername">ProviderName</a>; token: string; secret: string; email: string; avatarUrl: string; inviteCode?: string; }</code>


#### ProviderName

<code>'GOOGLE' | 'APPLE' | 'FACEBOOK' | 'TWITTER' | 'EMAIL'</code>


#### Record

Construct a type with a set of properties K of type T

<code>{ [P in K]: T; }</code>


#### AppStateChangeListener

<code>(change: <a href="#appstatechange">AppStateChange</a>): void</code>

</docgen-api>
