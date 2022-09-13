# @bitburst-gmbh/in-app-browser

capacitor plugin for delegating native web views

## Install

```bash
npm install @bitburst-gmbh/in-app-browser
npx cap sync
```

## API

<docgen-index>

* [`echo(...)`](#echo)
* [`open(...)`](#open)
* [Interfaces](#interfaces)
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


### open(...)

```typescript
open(options: IabOptions) => Promise<void>
```

| Param         | Type                                              |
| ------------- | ------------------------------------------------- |
| **`options`** | <code><a href="#iaboptions">IabOptions</a></code> |

--------------------


### Interfaces


#### IabOptions

| Prop                        | Type                                                                                                                                     |
| --------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------- |
| **`url`**                   | <code>string</code>                                                                                                                      |
| **`target`**                | <code>'_blank' \| '_parent' \| '_self'</code>                                                                                            |
| **`browserType`**           | <code>'DEFAULT' \| 'SYSTEM' \| 'SURVEY'</code>                                                                                           |
| **`browserEventCallbacks`** | <code>{ type: 'error' \| 'start' \| 'stop' \| 'exit' \| 'custom'; action: (event: <a href="#iabevent">IabEvent</a>) =&gt; void; }</code> |
| **`platformOptions`**       | <code><a href="#iabplatformoptions">IabPlatformOptions</a></code>                                                                        |


#### IabEvent

| Prop          | Type                |
| ------------- | ------------------- |
| **`type`**    | <code>string</code> |
| **`url`**     | <code>string</code> |
| **`code`**    | <code>number</code> |
| **`message`** | <code>string</code> |


#### IabPlatformOptions

| Prop                        | Type                                                             |
| --------------------------- | ---------------------------------------------------------------- |
| **`hidden`**                | <code>boolean</code>                                             |
| **`showUrlBar`**            | <code>boolean</code>                                             |
| **`zoom`**                  | <code>boolean</code>                                             |
| **`toolbarPosition`**       | <code>'TOP' \| 'BOTTOM'</code>                                   |
| **`toolbarColor`**          | <code>string</code>                                              |
| **`closeButtonColor`**      | <code>string</code>                                              |
| **`hardwareBackButton`**    | <code>boolean</code>                                             |
| **`hideNavigationButtons`** | <code>boolean</code>                                             |
| **`android`**               | <code><a href="#record">Record</a>&lt;string, unknown&gt;</code> |
| **`ios`**                   | <code><a href="#record">Record</a>&lt;string, unknown&gt;</code> |


### Type Aliases


#### Record

Construct a type with a set of properties K of type T

<code>{ [P in K]: T; }</code>

</docgen-api>
