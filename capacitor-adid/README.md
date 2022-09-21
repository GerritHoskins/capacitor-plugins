# @gerrithoskins/capacitor-adid

capacitor plugin for fetching the advertisement id

## Install

```bash
npm install @gerrithoskins/capacitor-adid
npx cap sync
```

## API

<docgen-index>

* [`getId(...)`](#getid)
* [`getStatus()`](#getstatus)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### getId(...)

```typescript
getId(delay?: string | number | undefined) => Promise<{ id: string; limitedAdTracking?: boolean; }>
```

| Param       | Type                          |
| ----------- | ----------------------------- |
| **`delay`** | <code>string \| number</code> |

**Returns:** <code>Promise&lt;{ id: string; limitedAdTracking?: boolean; }&gt;</code>

--------------------


### getStatus()

```typescript
getStatus() => Promise<{ status: string; statusId: number; }>
```

**Returns:** <code>Promise&lt;{ status: string; statusId: number; }&gt;</code>

--------------------

</docgen-api>
