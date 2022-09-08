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
* [`logEvent(...)`](#logevent)
* [`logPageView(...)`](#logpageview)
* [`loginUser(...)`](#loginuser)
* [`logoutUser(...)`](#logoutuser)
* [`registerUser(...)`](#registeruser)
* [`addListener('mParticleReady', ...)`](#addlistenermparticleready)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)
* [Enums](#enums)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### init(...)

```typescript
init(options: { key?: string; config: any; }) => Promise<any>
```

| Param         | Type                                        |
| ------------- | ------------------------------------------- |
| **`options`** | <code>{ key?: string; config: any; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### identifyUser(...)

```typescript
identifyUser(options: { identifier: Identifier; }) => Promise<void>
```

| Param         | Type                                                               |
| ------------- | ------------------------------------------------------------------ |
| **`options`** | <code>{ identifier: <a href="#identifier">Identifier</a>; }</code> |

--------------------


### setUserAttribute(...)

```typescript
setUserAttribute(options: { attributeName: string; attributeValue: string; }) => Promise<void>
```

| Param         | Type                                                            |
| ------------- | --------------------------------------------------------------- |
| **`options`** | <code>{ attributeName: string; attributeValue: string; }</code> |

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


### logEvent(...)

```typescript
logEvent(options: { eventName: string; eventType: EventType | number; eventProperties: any; }) => Promise<any>
```

| Param         | Type                                                                         |
| ------------- | ---------------------------------------------------------------------------- |
| **`options`** | <code>{ eventName: string; eventType: number; eventProperties: any; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### logPageView(...)

```typescript
logPageView(options: { pageName: string; pageLink: any; }) => Promise<any>
```

| Param         | Type                                              |
| ------------- | ------------------------------------------------- |
| **`options`** | <code>{ pageName: string; pageLink: any; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### loginUser(...)

```typescript
loginUser(options: { email: string; customerId: string; }) => Promise<any>
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
registerUser(options: { email: string; customerId: string; userAttributes: any; }) => Promise<any>
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


#### Identifier

<code>{ email?: string; customerId?: string; other?: string; }</code>


#### Record

Construct a type with a set of properties K of type T

<code>{ [P in K]: T; }</code>


#### EventType

<code><a href="#eventtypeenum">EventTypeEnum.unknown</a> | <a href="#eventtypeenum">EventTypeEnum.sessionStart</a> | <a href="#eventtypeenum">EventTypeEnum.sessionEnd</a> | <a href="#eventtypeenum">EventTypeEnum.screenView</a> | <a href="#eventtypeenum">EventTypeEnum.customEvent</a> | <a href="#eventtypeenum">EventTypeEnum.crashReport</a> | <a href="#eventtypeenum">EventTypeEnum.optOut</a> | <a href="#eventtypeenum">EventTypeEnum.firstRun</a> | <a href="#eventtypeenum">EventTypeEnum.preAttribution</a> | <a href="#eventtypeenum">EventTypeEnum.pushRegistration</a> | <a href="#eventtypeenum">EventTypeEnum.applicationStateTransition</a> | <a href="#eventtypeenum">EventTypeEnum.pushMessage</a> | <a href="#eventtypeenum">EventTypeEnum.networkPerformance</a> | <a href="#eventtypeenum">EventTypeEnum.breadcrumb</a> | <a href="#eventtypeenum">EventTypeEnum.profile</a> | <a href="#eventtypeenum">EventTypeEnum.pushReaction</a> | <a href="#eventtypeenum">EventTypeEnum.commerceEvent</a> | <a href="#eventtypeenum">EventTypeEnum.userAttributeChange</a> | <a href="#eventtypeenum">EventTypeEnum.userIdentityChange</a> | <a href="#eventtypeenum">EventTypeEnum.uninstall</a> | <a href="#eventtypeenum">EventTypeEnum.validationResult</a></code>


#### mParticleReadyListener

<code>(event: <a href="#mparticlereadyevent">MparticleReadyEvent</a>): void</code>


### Enums


#### EventType

| Members              | Value          |
| -------------------- | -------------- |
| **`Unknown`**        | <code>0</code> |
| **`Navigation`**     | <code>1</code> |
| **`Location`**       | <code>2</code> |
| **`Search`**         | <code>3</code> |
| **`Transaction`**    | <code>4</code> |
| **`UserContent`**    | <code>5</code> |
| **`UserPreference`** | <code>6</code> |
| **`Social`**         | <code>7</code> |
| **`Other`**          | <code>8</code> |
| **`Media`**          | <code>9</code> |


#### EventTypeEnum

| Members                          | Value                                       |
| -------------------------------- | ------------------------------------------- |
| **`unknown`**                    | <code>"unknown"</code>                      |
| **`sessionStart`**               | <code>"session_start"</code>                |
| **`sessionEnd`**                 | <code>"session_end"</code>                  |
| **`screenView`**                 | <code>"screen_view"</code>                  |
| **`customEvent`**                | <code>"custom_event"</code>                 |
| **`crashReport`**                | <code>"crash_report"</code>                 |
| **`optOut`**                     | <code>"opt_out"</code>                      |
| **`firstRun`**                   | <code>"first_run"</code>                    |
| **`preAttribution`**             | <code>"pre_attribution"</code>              |
| **`pushRegistration`**           | <code>"push_registration"</code>            |
| **`applicationStateTransition`** | <code>"application_state_transition"</code> |
| **`pushMessage`**                | <code>"push_message"</code>                 |
| **`networkPerformance`**         | <code>"network_performance"</code>          |
| **`breadcrumb`**                 | <code>"breadcrumb"</code>                   |
| **`profile`**                    | <code>"profile"</code>                      |
| **`pushReaction`**               | <code>"push_reaction"</code>                |
| **`commerceEvent`**              | <code>"commerce_event"</code>               |
| **`userAttributeChange`**        | <code>"user_attribute_change"</code>        |
| **`userIdentityChange`**         | <code>"user_identity_change"</code>         |
| **`uninstall`**                  | <code>"uninstall"</code>                    |
| **`validationResult`**           | <code>"validation_result"</code>            |

</docgen-api>
