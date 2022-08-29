# @bitburst-gmbh/login-provider

capacitor plugin for managing authentication for multiple login providers

## Install

```bash
npm install @bitburst-gmbh/login-provider
npx cap sync
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
* [Enums](#enums)

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

| Prop        | Type                                                                                                                                                                                                                                                                     |
| ----------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **`init`**  | <code>{ apple?: <a href="#appleinitoptions">AppleInitOptions</a>; facebook?: <a href="#facebookinitoptions">FacebookInitOptions</a>; google?: <a href="#googleinitoptions">GoogleInitOptions</a>; custom?: <a href="#record">Record</a>&lt;string, unknown&gt;; }</code> |
| **`login`** | <code>{ facebook?: <a href="#facebookloginoptions">FacebookLoginOptions</a>; custom?: <a href="#record">Record</a>&lt;string, unknown&gt;; }</code>                                                                                                                      |


#### AppleInitOptions

| Prop              | Type                 |
| ----------------- | -------------------- |
| **`clientId`**    | <code>string</code>  |
| **`redirectURI`** | <code>string</code>  |
| **`state`**       | <code>string</code>  |
| **`scope`**       | <code>string</code>  |
| **`usePopup`**    | <code>boolean</code> |


#### FacebookInitOptions

| Prop                   | Type                 |
| ---------------------- | -------------------- |
| **`appId`**            | <code>string</code>  |
| **`autoLogAppEvents`** | <code>boolean</code> |
| **`xfbml`**            | <code>boolean</code> |
| **`version`**          | <code>string</code>  |
| **`locale`**           | <code>string</code>  |


#### GoogleInitOptions

| Prop                     | Type                 |
| ------------------------ | -------------------- |
| **`grantOfflineAccess`** | <code>boolean</code> |


#### FacebookLoginOptions

| Prop              | Type                  |
| ----------------- | --------------------- |
| **`permissions`** | <code>string[]</code> |


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

| Prop        | Type                        |
| ----------- | --------------------------- |
| **`user`**  | <code>string \| null</code> |
| **`email`** | <code>string \| null</code> |
| **`name`**  | <code>string \| null</code> |
| **`token`** | <code>string</code>         |
| **`code`**  | <code>string</code>         |


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


#### Record

Construct a type with a set of properties K of type T

<code>{ [P in K]: T; }</code>


#### AppStateChangeListener

<code>(change: <a href="#appstatechange">AppStateChange</a>): void</code>


### Enums


#### ProviderName

| Members        |
| -------------- |
| **`Facebook`** |
| **`Google`**   |
| **`Apple`**    |
| **`Twitter`**  |

</docgen-api>
