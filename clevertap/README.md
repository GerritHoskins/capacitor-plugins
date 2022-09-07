# @bitburst-gmbh/clevertap

capacitor plugin for clevertap.

## Install

```bash
npm install @bitburst-gmbh/clevertap
npx cap sync
```

## API

<docgen-index>

* [`init(...)`](#init)
* [`cleverTap()`](#clevertap)
* [`getClevertapId()`](#getclevertapid)
* [`registerFBM()`](#registerfbm)
* [`getDeliveredNotifications()`](#getdeliverednotifications)
* [`removeDeliveredNotifications(...)`](#removedeliverednotifications)
* [`createChannel(...)`](#createchannel)
* [`onUserLogin(...)`](#onuserlogin)
* [`pushEvent(...)`](#pushevent)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### init(...)

```typescript
init(options?: InitOptions | undefined) => Promise<any>
```

| Param         | Type                                                |
| ------------- | --------------------------------------------------- |
| **`options`** | <code><a href="#initoptions">InitOptions</a></code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### cleverTap()

```typescript
cleverTap() => Promise<any>
```

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### getClevertapId()

```typescript
getClevertapId() => Promise<string | null>
```

**Returns:** <code>Promise&lt;string | null&gt;</code>

--------------------


### registerFBM()

```typescript
registerFBM() => Promise<void>
```

--------------------


### getDeliveredNotifications()

```typescript
getDeliveredNotifications() => Promise<DeliveredNotifications>
```

**Returns:** <code>Promise&lt;<a href="#deliverednotifications">DeliveredNotifications</a>&gt;</code>

--------------------


### removeDeliveredNotifications(...)

```typescript
removeDeliveredNotifications(options: { delivered: DeliveredNotifications; }) => Promise<void>
```

| Param         | Type                                                                                      |
| ------------- | ----------------------------------------------------------------------------------------- |
| **`options`** | <code>{ delivered: <a href="#deliverednotifications">DeliveredNotifications</a>; }</code> |

--------------------


### createChannel(...)

```typescript
createChannel(options: { channel: Channel; }) => Promise<void>
```

| Param         | Type                                                      |
| ------------- | --------------------------------------------------------- |
| **`options`** | <code>{ channel: <a href="#channel">Channel</a>; }</code> |

--------------------


### onUserLogin(...)

```typescript
onUserLogin(options: { profile: UserProfile; }) => Promise<void>
```

| Param         | Type                                                              |
| ------------- | ----------------------------------------------------------------- |
| **`options`** | <code>{ profile: <a href="#userprofile">UserProfile</a>; }</code> |

--------------------


### pushEvent(...)

```typescript
pushEvent(options: { event: PushEvent; }) => Promise<void>
```

| Param         | Type                                                        |
| ------------- | ----------------------------------------------------------- |
| **`options`** | <code>{ event: <a href="#pushevent">PushEvent</a>; }</code> |

--------------------


### Interfaces


#### InitOptions

| Prop               | Type                                      |
| ------------------ | ----------------------------------------- |
| **`accountId`**    | <code>string</code>                       |
| **`region`**       | <code><a href="#region">Region</a></code> |
| **`targetDomain`** | <code>string</code>                       |


#### DeliveredNotifications

| Prop                | Type                                  |
| ------------------- | ------------------------------------- |
| **`notifications`** | <code>PushNotificationSchema[]</code> |


#### PushNotificationSchema

| Prop               | Type                 |
| ------------------ | -------------------- |
| **`title`**        | <code>string</code>  |
| **`subtitle`**     | <code>string</code>  |
| **`body`**         | <code>string</code>  |
| **`id`**           | <code>string</code>  |
| **`tag`**          | <code>string</code>  |
| **`badge`**        | <code>number</code>  |
| **`data`**         | <code>any</code>     |
| **`click_action`** | <code>string</code>  |
| **`link`**         | <code>string</code>  |
| **`group`**        | <code>string</code>  |
| **`groupSummary`** | <code>boolean</code> |


#### Channel

| Prop              | Type                                              |
| ----------------- | ------------------------------------------------- |
| **`id`**          | <code>string</code>                               |
| **`name`**        | <code>string</code>                               |
| **`description`** | <code>string</code>                               |
| **`sound`**       | <code>string</code>                               |
| **`importance`**  | <code><a href="#importance">Importance</a></code> |
| **`visibility`**  | <code><a href="#visibility">Visibility</a></code> |
| **`lights`**      | <code>boolean</code>                              |
| **`lightColor`**  | <code>string</code>                               |
| **`vibration`**   | <code>boolean</code>                              |


#### UserProfile

| Prop                | Type                |
| ------------------- | ------------------- |
| **`uid`**           | <code>string</code> |
| **`internalId`**    | <code>string</code> |
| **`email`**         | <code>string</code> |
| **`clientProduct`** | <code>string</code> |


#### PushEvent

| Prop        | Type                                            |
| ----------- | ----------------------------------------------- |
| **`name`**  | <code>string</code>                             |
| **`value`** | <code><a href="#eventdata">EventData</a></code> |


### Type Aliases


#### Region

<code>'eu1' | 'in1' | 'sg1' | 'us1' | 'sk1'</code>


#### Importance

<code>1 | 2 | 3 | 4 | 5</code>


#### Visibility

<code>-1 | 0 | 1</code>


#### EventData

<code><a href="#record">Record</a>&lt;string, unknown&gt;</code>


#### Record

Construct a type with a set of properties K of type T

<code>{ [P in K]: T; }</code>

</docgen-api>
