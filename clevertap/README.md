# @bitburst-gmbh/clevertap

capacitor plugin for clevertap.

## Install

```bash
npm install @bitburst-gmbh/clevertap
npx cap sync
```

## API

<docgen-index>

* [`getClevertapId()`](#getclevertapid)
* [`isReady()`](#isready)
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

### getClevertapId()

```typescript
getClevertapId() => Promise<ClevertapInstance>
```

**Returns:** <code>Promise&lt;<a href="#clevertapinstance">ClevertapInstance</a>&gt;</code>

--------------------


### isReady()

```typescript
isReady() => Promise<ClevertapInstance>
```

**Returns:** <code>Promise&lt;<a href="#clevertapinstance">ClevertapInstance</a>&gt;</code>

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
removeDeliveredNotifications(delivered: DeliveredNotifications) => Promise<void>
```

| Param           | Type                                                                      |
| --------------- | ------------------------------------------------------------------------- |
| **`delivered`** | <code><a href="#deliverednotifications">DeliveredNotifications</a></code> |

--------------------


### createChannel(...)

```typescript
createChannel(channel: Channel) => Promise<void>
```

| Param         | Type                                        |
| ------------- | ------------------------------------------- |
| **`channel`** | <code><a href="#channel">Channel</a></code> |

--------------------


### onUserLogin(...)

```typescript
onUserLogin(profile: UserProfile) => Promise<void>
```

| Param         | Type                                                |
| ------------- | --------------------------------------------------- |
| **`profile`** | <code><a href="#userprofile">UserProfile</a></code> |

--------------------


### pushEvent(...)

```typescript
pushEvent(event: PushEvent) => Promise<void>
```

| Param       | Type                                            |
| ----------- | ----------------------------------------------- |
| **`event`** | <code><a href="#pushevent">PushEvent</a></code> |

--------------------


### Interfaces


#### ClevertapInstance

| Prop              | Type                 |
| ----------------- | -------------------- |
| **`clevertapId`** | <code>string</code>  |
| **`isReady`**     | <code>boolean</code> |


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
