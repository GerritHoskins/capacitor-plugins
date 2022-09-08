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
* [`pushNotification(...)`](#pushnotification)
* [`pushPrivacy(...)`](#pushprivacy)
* [`pushUser(...)`](#pushuser)
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
cleverTap() => CleverTap
```

**Returns:** <code>CleverTap</code>

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
pushEvent(options: { evtName: string; evtNameOrData: EventNameOrData[]; }) => void
```

| Param         | Type                                                                |
| ------------- | ------------------------------------------------------------------- |
| **`options`** | <code>{ evtName: string; evtNameOrData: EventNameOrData[]; }</code> |

--------------------


### pushNotification(...)

```typescript
pushNotification(options: { notificationData: NotificationData; }) => void
```

| Param         | Type                                                                                 |
| ------------- | ------------------------------------------------------------------------------------ |
| **`options`** | <code>{ notificationData: <a href="#notificationdata">NotificationData</a>; }</code> |

--------------------


### pushPrivacy(...)

```typescript
pushPrivacy(options: { privacyArr: PrivacyData[]; }) => void
```

| Param         | Type                                        |
| ------------- | ------------------------------------------- |
| **`options`** | <code>{ privacyArr: PrivacyData[]; }</code> |

--------------------


### pushUser(...)

```typescript
pushUser(options: { profileData: any[]; }) => void
```

| Param         | Type                                 |
| ------------- | ------------------------------------ |
| **`options`** | <code>{ profileData: any[]; }</code> |

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


#### NotificationData

| Prop                        | Type                         |
| --------------------------- | ---------------------------- |
| **`titleText`**             | <code>string</code>          |
| **`bodyText`**              | <code>string</code>          |
| **`okButtonText`**          | <code>string</code>          |
| **`rejectButtonText`**      | <code>string</code>          |
| **`okButtonColor`**         | <code>string</code>          |
| **`skipDialog`**            | <code>boolean</code>         |
| **`askAgainTimeInSeconds`** | <code>number</code>          |
| **`okCallback`**            | <code>(() =&gt; void)</code> |
| **`rejectCallback`**        | <code>(() =&gt; void)</code> |
| **`subscriptionCallback`**  | <code>(() =&gt; void)</code> |
| **`hidePoweredByCT`**       | <code>boolean</code>         |
| **`serviceWorkerPath`**     | <code>string</code>          |
| **`httpsPopupPath`**        | <code>string</code>          |
| **`httpsIframePath`**       | <code>string</code>          |
| **`apnsWebPushId`**         | <code>string</code>          |
| **`apnsWebPushServiceUrl`** | <code>string</code>          |


#### PrivacyData

| Prop         | Type                 |
| ------------ | -------------------- |
| **`optOut`** | <code>boolean</code> |
| **`useIP`**  | <code>boolean</code> |


### Type Aliases


#### Region

<code>'eu1' | 'in1' | 'sg1' | 'us1' | 'sk1'</code>


#### Importance

<code>1 | 2 | 3 | 4 | 5</code>


#### Visibility

<code>-1 | 0 | 1</code>


#### EventNameOrData

<code><a href="#eventname">EventName</a> | <a href="#eventdata">EventData</a></code>


#### EventName

<code>string</code>


#### EventData

<code>object</code>

</docgen-api>
