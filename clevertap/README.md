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
* [`getCleverTapID()`](#getclevertapid)
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


### getCleverTapID()

```typescript
getCleverTapID() => Promise<string | null>
```

**Returns:** <code>Promise&lt;string | null&gt;</code>

--------------------


### Interfaces


#### InitOptions

| Prop               | Type                                      |
| ------------------ | ----------------------------------------- |
| **`accountId`**    | <code>string</code>                       |
| **`token`**        | <code>string</code>                       |
| **`region`**       | <code><a href="#region">Region</a></code> |
| **`targetDomain`** | <code>string</code>                       |


### Type Aliases


#### Region

<code>'eu1' | 'in1' | 'sg1' | 'us1' | 'sk1'</code>

</docgen-api>
