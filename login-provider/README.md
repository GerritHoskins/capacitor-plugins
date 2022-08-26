# @bitburst-gmbh/login-provider

capacitor plugin for managing authentication for multiple login providers

## Install

```bash
npm install @bitburst-gmbh/login-provider
npx cap sync
```

## API

<docgen-index>

* [`initializeProvider(...)`](#initializeprovider)
* [`signInWithProvider(...)`](#signinwithprovider)
* [`signOutFromProvider(...)`](#signoutfromprovider)
* [`addListener('appStateChange', ...)`](#addlistenerappstatechange)
* [`removeAllListeners()`](#removealllisteners)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)
* [Enums](#enums)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### initializeProvider(...)

```typescript
initializeProvider(provider: ProviderName, options: GoogleInitOptions | FacebookInitOptions | AppleInitOptions) => Promise<void>
```

| Param          | Type                                                                                                                                                                          |
| -------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`provider`** | <code><a href="#providername">ProviderName</a></code>                                                                                                                         |
| **`options`**  | <code><a href="#googleinitoptions">GoogleInitOptions</a> \| <a href="#facebookinitoptions">FacebookInitOptions</a> \| <a href="#appleinitoptions">AppleInitOptions</a></code> |

--------------------


### signInWithProvider(...)

```typescript
signInWithProvider(provider: ProviderName, options?: FacebookLoginOptions | undefined, inviteCode?: string | undefined) => Promise<ProviderResponse>
```

| Param            | Type                                                                  |
| ---------------- | --------------------------------------------------------------------- |
| **`provider`**   | <code><a href="#providername">ProviderName</a></code>                 |
| **`options`**    | <code><a href="#facebookloginoptions">FacebookLoginOptions</a></code> |
| **`inviteCode`** | <code>string</code>                                                   |

**Returns:** <code>Promise&lt;<a href="#providerresponse">ProviderResponse</a>&gt;</code>

--------------------


### signOutFromProvider(...)

```typescript
signOutFromProvider(provider: ProviderName) => Promise<void | any>
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


#### GoogleInitOptions

| Prop                     | Type                 |
| ------------------------ | -------------------- |
| **`grantOfflineAccess`** | <code>boolean</code> |


#### FacebookInitOptions

| Prop                   | Type                 |
| ---------------------- | -------------------- |
| **`appId`**            | <code>string</code>  |
| **`autoLogAppEvents`** | <code>boolean</code> |
| **`xfbml`**            | <code>boolean</code> |
| **`version`**          | <code>string</code>  |
| **`locale`**           | <code>string</code>  |


#### AppleInitOptions

| Prop              | Type                 |
| ----------------- | -------------------- |
| **`clientId`**    | <code>string</code>  |
| **`redirectURI`** | <code>string</code>  |
| **`state`**       | <code>string</code>  |
| **`scope`**       | <code>string</code>  |
| **`usePopup`**    | <code>boolean</code> |


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
| **`user`** | <code><a href="#facebookloginresponse">FacebookLoginResponse</a> \| <a href="#googleloginresponse">GoogleLoginResponse</a> \| <a href="#appleloginresponse">AppleLoginResponse</a> \| <a href="#twitterloginresponse">TwitterLoginResponse</a> \| null</code> |


#### FacebookInterface

| Method                    | Signature                                                                                                                                           |
| ------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------- |
| **initialize**            | (options: <a href="#partial">Partial</a>&lt;<a href="#facebookinitoptions">FacebookInitOptions</a>&gt;) =&gt; Promise&lt;void&gt;                   |
| **login**                 | (options: <a href="#facebookloginoptions">FacebookLoginOptions</a>) =&gt; Promise&lt;<a href="#facebookloginresponse">FacebookLoginResponse</a>&gt; |
| **logout**                | () =&gt; Promise&lt;void&gt;                                                                                                                        |
| **reauthorize**           | () =&gt; Promise&lt;<a href="#facebookloginresponse">FacebookLoginResponse</a>&gt;                                                                  |
| **getCurrentAccessToken** | () =&gt; Promise&lt;<a href="#facebookcurrentaccesstokenresponse">FacebookCurrentAccessTokenResponse</a>&gt;                                        |
| **getProfile**            | &lt;T extends <a href="#record">Record</a>&lt;string, unknown&gt;&gt;(options: { fields: readonly string[]; }) =&gt; Promise&lt;T&gt;               |


#### FacebookCurrentAccessTokenResponse

| Prop              | Type                                                          |
| ----------------- | ------------------------------------------------------------- |
| **`accessToken`** | <code><a href="#facebookauth">FacebookAuth</a> \| null</code> |


#### GoogleInterface

| Method         | Signature                                                                                                                     |
| -------------- | ----------------------------------------------------------------------------------------------------------------------------- |
| **initialize** | (options: <a href="#partial">Partial</a>&lt;<a href="#googleinitoptions">GoogleInitOptions</a>&gt;) =&gt; Promise&lt;void&gt; |
| **login**      | () =&gt; Promise&lt;<a href="#googleloginresponse">GoogleLoginResponse</a>&gt;                                                |
| **refresh**    | () =&gt; Promise&lt;<a href="#googleauth">GoogleAuth</a>&gt;                                                                  |
| **logout**     | () =&gt; Promise&lt;any&gt;                                                                                                   |


#### AppleInterface

| Prop                    | Type                 |
| ----------------------- | -------------------- |
| **`appleScriptUrl`**    | <code>string</code>  |
| **`appleScriptLoaded`** | <code>unknown</code> |

| Method         | Signature                                                                             |
| -------------- | ------------------------------------------------------------------------------------- |
| **login**      | () =&gt; Promise&lt;<a href="#appleloginresponse">AppleLoginResponse</a>&gt;          |
| **initialize** | (options: <a href="#appleinitoptions">AppleInitOptions</a>) =&gt; Promise&lt;void&gt; |


#### TwitterInterface

| Method       | Signature                                                                                  |
| ------------ | ------------------------------------------------------------------------------------------ |
| **isLogged** | () =&gt; Promise&lt;<a href="#twitteruserstatusresponse">TwitterUserStatusResponse</a>&gt; |
| **login**    | () =&gt; Promise&lt;<a href="#twitterloginresponse">TwitterLoginResponse</a>&gt;           |
| **logout**   | () =&gt; Promise&lt;void&gt;                                                               |


#### TwitterUserStatusResponse

| Prop      | Type                 |
| --------- | -------------------- |
| **`in`**  | <code>boolean</code> |
| **`out`** | <code>boolean</code> |


### Type Aliases


#### ProviderResponse

<code><a href="#facebookloginresponse">FacebookLoginResponse</a> | <a href="#googleloginresponse">GoogleLoginResponse</a> | <a href="#appleloginresponse">AppleLoginResponse</a> | <a href="#twitterloginresponse">TwitterLoginResponse</a></code>


#### AppStateChangeListener

<code>(change: <a href="#appstatechange">AppStateChange</a>): void</code>


#### Provider

<code><a href="#facebookinterface">FacebookInterface</a> | <a href="#googleinterface">GoogleInterface</a> | <a href="#appleinterface">AppleInterface</a> | <a href="#twitterinterface">TwitterInterface</a></code>


#### Partial

Make all properties in T optional

<code>{ [P in keyof T]?: T[P]; }</code>


#### Record

Construct a type with a set of properties K of type T

<code>{ [P in K]: T; }</code>


### Enums


#### ProviderName

| Members        |
| -------------- |
| **`Facebook`** |
| **`Google`**   |
| **`Apple`**    |
| **`Twitter`**  |

</docgen-api>
