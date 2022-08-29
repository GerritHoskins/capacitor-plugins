# @bitburst-gmbh/native-market

capacitor plugin for linking to google play or app store.

## Install

```bash
npm install @bitburst-gmbh/native-market
npx cap sync
```

## API

<docgen-index>

* [`openStoreListing(...)`](#openstorelisting)
* [`openDevPage(...)`](#opendevpage)
* [`openCollection(...)`](#opencollection)
* [`openEditorChoicePage(...)`](#openeditorchoicepage)
* [`search(...)`](#search)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### openStoreListing(...)

```typescript
openStoreListing(options: { appId: string; }) => Promise<void>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ appId: string; }</code> |

--------------------


### openDevPage(...)

```typescript
openDevPage(options: { devId: string; }) => Promise<void>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ devId: string; }</code> |

--------------------


### openCollection(...)

```typescript
openCollection(options: { name: string; }) => Promise<void>
```

| Param         | Type                           |
| ------------- | ------------------------------ |
| **`options`** | <code>{ name: string; }</code> |

--------------------


### openEditorChoicePage(...)

```typescript
openEditorChoicePage(options: { editorChoice: string; }) => Promise<void>
```

| Param         | Type                                   |
| ------------- | -------------------------------------- |
| **`options`** | <code>{ editorChoice: string; }</code> |

--------------------


### search(...)

```typescript
search(options: { terms: string; }) => Promise<void>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ terms: string; }</code> |

--------------------

</docgen-api>
