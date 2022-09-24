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
* [`trackEvent(...)`](#trackevent)
* [`trackPageView(...)`](#trackpageview)
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
setUserAttribute(attribute: Attribute) => Promise<void>
```

| Param           | Type                                            |
| --------------- | ----------------------------------------------- |
| **`attribute`** | <code><a href="#attribute">Attribute</a></code> |

--------------------


### setGDPRConsent(...)

```typescript
setGDPRConsent(gdprConsents: GDPRConsents) => void
```

| Param              | Type                                                  |
| ------------------ | ----------------------------------------------------- |
| **`gdprConsents`** | <code><a href="#gdprconsents">GDPRConsents</a></code> |

--------------------


### getGDPRConsent(...)

```typescript
getGDPRConsent(options: { consents: string[]; }) => Record<string, boolean> | void
```

| Param         | Type                                 |
| ------------- | ------------------------------------ |
| **`options`** | <code>{ consents: string[]; }</code> |

**Returns:** <code>void | <a href="#record">Record</a>&lt;string, boolean&gt;</code>

--------------------


### getMPID()

```typescript
getMPID() => Promise<string | void>
```

**Returns:** <code>Promise&lt;string | void&gt;</code>

--------------------


### trackEvent(...)

```typescript
trackEvent(event: DefaultEvent) => void
```

| Param       | Type                                                  |
| ----------- | ----------------------------------------------------- |
| **`event`** | <code><a href="#defaultevent">DefaultEvent</a></code> |

--------------------


### trackPageView(...)

```typescript
trackPageView(event: DefaultEvent) => void
```

| Param       | Type                                                  |
| ----------- | ----------------------------------------------------- |
| **`event`** | <code><a href="#defaultevent">DefaultEvent</a></code> |

--------------------


### loginUser(...)

```typescript
loginUser(identifier?: Identifier | undefined) => Promise<any>
```

| Param            | Type                                              |
| ---------------- | ------------------------------------------------- |
| **`identifier`** | <code><a href="#identifier">Identifier</a></code> |

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
registerUser(identifier?: Identifier | undefined) => Promise<any>
```

| Param            | Type                                              |
| ---------------- | ------------------------------------------------- |
| **`identifier`** | <code><a href="#identifier">Identifier</a></code> |

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


#### Attribute

<code>{ name: string; value: string; }</code>


#### GDPRConsents

<code><a href="#record">Record</a>&lt;string, <a href="#consent">Consent</a>&gt;</code>


#### Consent

<code>{ consented?: boolean; timestamp?: number; consentDocument?: string; location?: string; hardwareId?: string; }</code>


#### DefaultEvent

<code>{ name: string; data?: any }</code>


#### mParticleReadyListener

<code>(event: <a href="#mparticlereadyevent">MparticleReadyEvent</a>): void</code>

</docgen-api>
