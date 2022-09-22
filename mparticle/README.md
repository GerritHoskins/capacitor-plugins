# @bitburst-gmbh/mparticle

capacitor plugin for mparticle.

## Install

```bash
npm install @bitburst-gmbh/mparticle
npx cap sync
```

## API

<docgen-index>

* [`init(...)`](#init)
* [`identifyUser(...)`](#identifyuser)
* [`setUserAttribute(...)`](#setuserattribute)
* [`setGDPRConsent(...)`](#setgdprconsent)
* [`getGDPRConsent(...)`](#getgdprconsent)
* [`getMPID()`](#getmpid)
* [`getInstance()`](#getinstance)
* [`loginUser(...)`](#loginuser)
* [`logoutUser(...)`](#logoutuser)
* [`registerUser(...)`](#registeruser)
* [`addListener('mParticleReady', ...)`](#addlistenermparticleready)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### init(...)

```typescript
init(key: string, config: Record<string, unknown>, secret?: string | undefined) => Promise<void>
```

| Param        | Type                                                             |
| ------------ | ---------------------------------------------------------------- |
| **`key`**    | <code>string</code>                                              |
| **`config`** | <code><a href="#record">Record</a>&lt;string, unknown&gt;</code> |
| **`secret`** | <code>string</code>                                              |

--------------------


### identifyUser(...)

```typescript
identifyUser(identifier?: Identifier | undefined) => Promise<void>
```

| Param            | Type                                              |
| ---------------- | ------------------------------------------------- |
| **`identifier`** | <code><a href="#identifier">Identifier</a></code> |

--------------------


### setUserAttribute(...)

```typescript
setUserAttribute(attributeName: string, attributeValue: string) => Promise<void>
```

| Param                | Type                |
| -------------------- | ------------------- |
| **`attributeName`**  | <code>string</code> |
| **`attributeValue`** | <code>string</code> |

--------------------


### setGDPRConsent(...)

```typescript
setGDPRConsent(options: { consents: Record<string, PrivacyConsentState>; }) => void
```

| Param         | Type                                                                                                                           |
| ------------- | ------------------------------------------------------------------------------------------------------------------------------ |
| **`options`** | <code>{ consents: <a href="#record">Record</a>&lt;string, <a href="#privacyconsentstate">PrivacyConsentState</a>&gt;; }</code> |

--------------------


### getGDPRConsent(...)

```typescript
getGDPRConsent(consents: string[]) => Record<string, boolean> | void
```

| Param          | Type                  |
| -------------- | --------------------- |
| **`consents`** | <code>string[]</code> |

**Returns:** <code>void | <a href="#record">Record</a>&lt;string, boolean&gt;</code>

--------------------


### getMPID()

```typescript
getMPID() => Promise<string | void>
```

**Returns:** <code>Promise&lt;string | void&gt;</code>

--------------------


### getInstance()

```typescript
getInstance() => mParticleInstanceType
```

**Returns:** <code>typeof import("/Users/gerrithoskins/BitBurst/gh-capacitor-plugins/mparticle/node_modules/@types/mparticle__web-sdk/index")</code>

--------------------


### loginUser(...)

```typescript
loginUser(options?: { email: string; customerId: string; } | undefined) => Promise<any>
```

| Param         | Type                                                |
| ------------- | --------------------------------------------------- |
| **`options`** | <code>{ email: string; customerId: string; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### logoutUser(...)

```typescript
logoutUser(options?: any) => Promise<any>
```

| Param         | Type             |
| ------------- | ---------------- |
| **`options`** | <code>any</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### registerUser(...)

```typescript
registerUser(options?: { email: string; customerId: string; userAttributes: any; } | undefined) => Promise<any>
```

| Param         | Type                                                                     |
| ------------- | ------------------------------------------------------------------------ |
| **`options`** | <code>{ email: string; customerId: string; userAttributes: any; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### addListener('mParticleReady', ...)

```typescript
addListener(eventName: 'mParticleReady', listenerFunc: mParticleReadyListener) => Promise<PluginListenerHandle> & PluginListenerHandle
```

| Param              | Type                                                                      |
| ------------------ | ------------------------------------------------------------------------- |
| **`eventName`**    | <code>'mParticleReady'</code>                                             |
| **`listenerFunc`** | <code><a href="#mparticlereadylistener">mParticleReadyListener</a></code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt; & <a href="#pluginlistenerhandle">PluginListenerHandle</a></code>

--------------------


### Interfaces


#### PrivacyConsentState

| Prop                  | Type                 |
| --------------------- | -------------------- |
| **`Consented`**       | <code>boolean</code> |
| **`Timestamp`**       | <code>number</code>  |
| **`ConsentDocument`** | <code>string</code>  |
| **`Location`**        | <code>string</code>  |
| **`HardwareId`**      | <code>string</code>  |


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


#### MparticleReadyEvent

| Prop        | Type                 |
| ----------- | -------------------- |
| **`ready`** | <code>boolean</code> |


### Type Aliases


#### Record

Construct a type with a set of properties K of type T

<code>{ [P in K]: T; }</code>


#### Identifier

<code>{ email?: string; customerId?: string; other?: string; }</code>


#### mParticleInstanceType

<code>typeof mParticle</code>


#### mParticleReadyListener

<code>(event: <a href="#mparticlereadyevent">MparticleReadyEvent</a>): void</code>

</docgen-api>
