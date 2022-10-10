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
* [`setUserAttributes(...)`](#setuserattributes)
* [`setGDPRConsent(...)`](#setgdprconsent)
* [`getGDPRConsent(...)`](#getgdprconsent)
* [`getMPID()`](#getmpid)
* [`trackEvent(...)`](#trackevent)
* [`trackPageView(...)`](#trackpageview)
* [`trackPurchase(...)`](#trackpurchase)
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
identifyUser(identifier?: Identifier | undefined) => Promise<any>
```

| Param            | Type                                              |
| ---------------- | ------------------------------------------------- |
| **`identifier`** | <code><a href="#identifier">Identifier</a></code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### setUserAttribute(...)

```typescript
setUserAttribute(attribute: Attribute) => Promise<void>
```

| Param           | Type                                            |
| --------------- | ----------------------------------------------- |
| **`attribute`** | <code><a href="#attribute">Attribute</a></code> |

--------------------


### setUserAttributes(...)

```typescript
setUserAttributes(attributes: Attribute[]) => Promise<void>
```

| Param            | Type                     |
| ---------------- | ------------------------ |
| **`attributes`** | <code>Attribute[]</code> |

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


### trackPurchase(...)

```typescript
trackPurchase(product: Product) => Promise<void>
```

| Param         | Type                                        |
| ------------- | ------------------------------------------- |
| **`product`** | <code><a href="#product">Product</a></code> |

--------------------


### Interfaces


#### Product

| Prop                        | Type                                    |
| --------------------------- | --------------------------------------- |
| **`id`**                    | <code>string</code>                     |
| **`name`**                  | <code>string</code>                     |
| **`brand`**                 | <code>string</code>                     |
| **`category`**              | <code>string</code>                     |
| **`variant`**               | <code>string</code>                     |
| **`position`**              | <code>number</code>                     |
| **`price`**                 | <code>number</code>                     |
| **`quantity`**              | <code>number</code>                     |
| **`coupon_code`**           | <code>string</code>                     |
| **`added_to_cart_time_ms`** | <code>number</code>                     |
| **`total_product_amount`**  | <code>number</code>                     |
| **`custom_attributes`**     | <code>{ [key: string]: string; }</code> |


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


#### Product

<code>{ name: string; sku: string; price: number; quantity: number; transactionId: string; attributes?: any; }</code>

</docgen-api>
