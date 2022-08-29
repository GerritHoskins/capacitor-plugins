# @bitburst-gmbh/mparticle

capacitor plugin for mparticle.

## Install

```bash
npm install @bitburst-gmbh/mparticle
npx cap sync
```

## API

<docgen-index>

* [`echo(...)`](#echo)
* [`mParticleConfig(...)`](#mparticleconfig)
* [`mParticleInit(...)`](#mparticleinit)
* [`loginMparticleUser(...)`](#loginmparticleuser)
* [`logoutMparticleUser(...)`](#logoutmparticleuser)
* [`logMparticleEvent(...)`](#logmparticleevent)
* [`logMparticlePageView(...)`](#logmparticlepageview)
* [`setUserAttribute(...)`](#setuserattribute)
* [`setUserAttributeList(...)`](#setuserattributelist)
* [`registerMparticleUser(...)`](#registermparticleuser)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### echo(...)

```typescript
echo(options: { value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### mParticleConfig(...)

```typescript
mParticleConfig(call: { isDevelopmentMode?: boolean; planID?: string; planVer?: number; logLevel?: string; identifyRequest?: any; identityCallback?: () => void; }) => Promise<MparticleConfigType>
```

| Param      | Type                                                                                                                                                           |
| ---------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`call`** | <code>{ isDevelopmentMode?: boolean; planID?: string; planVer?: number; logLevel?: string; identifyRequest?: any; identityCallback?: (() =&gt; void); }</code> |

**Returns:** <code>Promise&lt;<a href="#mparticleconfigtype">MparticleConfigType</a>&gt;</code>

--------------------


### mParticleInit(...)

```typescript
mParticleInit(call: { key: string; mParticleConfig: any; }) => Promise<any>
```

| Param      | Type                                                |
| ---------- | --------------------------------------------------- |
| **`call`** | <code>{ key: string; mParticleConfig: any; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### loginMparticleUser(...)

```typescript
loginMparticleUser(call: { email: string; customerId: string; }) => Promise<any>
```

| Param      | Type                                                |
| ---------- | --------------------------------------------------- |
| **`call`** | <code>{ email: string; customerId: string; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### logoutMparticleUser(...)

```typescript
logoutMparticleUser(call?: any) => Promise<any>
```

| Param      | Type             |
| ---------- | ---------------- |
| **`call`** | <code>any</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### logMparticleEvent(...)

```typescript
logMparticleEvent(call: { eventName: string; eventType: any; eventProperties: any; }) => Promise<any>
```

| Param      | Type                                                                      |
| ---------- | ------------------------------------------------------------------------- |
| **`call`** | <code>{ eventName: string; eventType: any; eventProperties: any; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### logMparticlePageView(...)

```typescript
logMparticlePageView(call: { pageName: string; pageLink: any; }) => Promise<any>
```

| Param      | Type                                              |
| ---------- | ------------------------------------------------- |
| **`call`** | <code>{ pageName: string; pageLink: any; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### setUserAttribute(...)

```typescript
setUserAttribute(call: { attributeName: string; attributeValue: string; }) => Promise<any>
```

| Param      | Type                                                            |
| ---------- | --------------------------------------------------------------- |
| **`call`** | <code>{ attributeName: string; attributeValue: string; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### setUserAttributeList(...)

```typescript
setUserAttributeList(call: { attributeName: string; attributeValues: any; }) => Promise<any>
```

| Param      | Type                                                          |
| ---------- | ------------------------------------------------------------- |
| **`call`** | <code>{ attributeName: string; attributeValues: any; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### registerMparticleUser(...)

```typescript
registerMparticleUser(call: { email: string; customerId: string; userAttributes: any; }) => Promise<any>
```

| Param      | Type                                                                     |
| ---------- | ------------------------------------------------------------------------ |
| **`call`** | <code>{ email: string; customerId: string; userAttributes: any; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### Type Aliases


#### MparticleConfigType

<code>{ isDevelopmentMode?: boolean; dataPlan?: { planId?: string; planVersion?: number; }; identifyRequest?: any; logLevel?: string; identityCallback?: () =&gt; void; }</code>

</docgen-api>
