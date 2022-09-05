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
* [`loginWithTwitter(...)`](#loginwithtwitter)
* [`logoutFromProvider(...)`](#logoutfromprovider)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### loginWithProvider(...)

```typescript
loginWithProvider(options: LoginProviderInitOptions) => Promise<LoginProviderPayload>
```

| Param         | Type                                                                          |
| ------------- | ----------------------------------------------------------------------------- |
| **`options`** | <code><a href="#loginproviderinitoptions">LoginProviderInitOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#loginproviderpayload">LoginProviderPayload</a>&gt;</code>

--------------------


### loginWithApple(...)

```typescript
loginWithApple(options: { loginOptions: LoginProviderOptions; inviteCode?: string; }) => Promise<LoginProviderPayload>
```

| Param         | Type                                                                                                          |
| ------------- | ------------------------------------------------------------------------------------------------------------- |
| **`options`** | <code>{ loginOptions: <a href="#loginprovideroptions">LoginProviderOptions</a>; inviteCode?: string; }</code> |

**Returns:** <code>Promise&lt;<a href="#loginproviderpayload">LoginProviderPayload</a>&gt;</code>

--------------------


### loginWithFacebook(...)

```typescript
loginWithFacebook(options: { loginOptions: LoginProviderOptions; inviteCode?: string; }) => Promise<LoginProviderPayload>
```

| Param         | Type                                                                                                          |
| ------------- | ------------------------------------------------------------------------------------------------------------- |
| **`options`** | <code>{ loginOptions: <a href="#loginprovideroptions">LoginProviderOptions</a>; inviteCode?: string; }</code> |

**Returns:** <code>Promise&lt;<a href="#loginproviderpayload">LoginProviderPayload</a>&gt;</code>

--------------------


### loginWithGoogle(...)

```typescript
loginWithGoogle(options: { loginOptions: LoginProviderOptions; inviteCode?: string; }) => Promise<LoginProviderPayload>
```

| Param         | Type                                                                                                          |
| ------------- | ------------------------------------------------------------------------------------------------------------- |
| **`options`** | <code>{ loginOptions: <a href="#loginprovideroptions">LoginProviderOptions</a>; inviteCode?: string; }</code> |

**Returns:** <code>Promise&lt;<a href="#loginproviderpayload">LoginProviderPayload</a>&gt;</code>

--------------------


### loginWithTwitter(...)

```typescript
loginWithTwitter(options: { loginOptions: LoginProviderOptions; inviteCode?: string; }) => Promise<LoginProviderPayload>
```

| Param         | Type                                                                                                          |
| ------------- | ------------------------------------------------------------------------------------------------------------- |
| **`options`** | <code>{ loginOptions: <a href="#loginprovideroptions">LoginProviderOptions</a>; inviteCode?: string; }</code> |

**Returns:** <code>Promise&lt;<a href="#loginproviderpayload">LoginProviderPayload</a>&gt;</code>

--------------------


### logoutFromProvider(...)

```typescript
logoutFromProvider(options: { provider: ProviderName; }) => Promise<void | any>
```

| Param         | Type                                                                 |
| ------------- | -------------------------------------------------------------------- |
| **`options`** | <code>{ provider: <a href="#providername">ProviderName</a>; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### Interfaces


#### LoginProviderInitOptions

| Prop               | Type                                                                  |
| ------------------ | --------------------------------------------------------------------- |
| **`provider`**     | <code><a href="#providername">ProviderName</a></code>                 |
| **`loginOptions`** | <code><a href="#loginprovideroptions">LoginProviderOptions</a></code> |
| **`inviteCode`**   | <code>string</code>                                                   |


#### LoginProviderOptions

| Prop                     | Type                                                             |
| ------------------------ | ---------------------------------------------------------------- |
| **`grantOfflineAccess`** | <code>boolean</code>                                             |
| **`custom`**             | <code><a href="#record">Record</a>&lt;string, unknown&gt;</code> |


### Type Aliases


#### LoginProviderPayload

<code>{ provider: <a href="#providername">ProviderName</a>; token: string; secret: string; email: string; avatarUrl: string; inviteCode?: string; }</code>


#### ProviderName

<code>'GOOGLE' | 'APPLE' | 'FACEBOOK' | 'TWITTER' | 'EMAIL'</code>


#### Record

Construct a type with a set of properties K of type T

<code>{ [P in K]: T; }</code>

</docgen-api>
