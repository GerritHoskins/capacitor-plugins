# @gerrithoskins/capacitor-twitter-login

capacitor plugin for twitter user authentication

## Install

```bash
npm install @gerrithoskins/capacitor-twitter-login
npx cap sync
```

## API

<docgen-index>

* [`isLogged()`](#islogged)
* [`login()`](#login)
* [`logout()`](#logout)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### isLogged()

```typescript
isLogged() => Promise<TwitterLoggedResponse>
```

**Returns:** <code>Promise&lt;<a href="#twitterloggedresponse">TwitterLoggedResponse</a>&gt;</code>

--------------------


### login()

```typescript
login() => Promise<TwitterLoginResponse>
```

**Returns:** <code>Promise&lt;<a href="#twitterloginresponse">TwitterLoginResponse</a>&gt;</code>

--------------------


### logout()

```typescript
logout() => Promise<void>
```

--------------------


### Interfaces


#### TwitterLoggedResponse

| Prop      | Type                 |
| --------- | -------------------- |
| **`in`**  | <code>boolean</code> |
| **`out`** | <code>boolean</code> |


#### TwitterLoginResponse

| Prop                  | Type                |
| --------------------- | ------------------- |
| **`authToken`**       | <code>string</code> |
| **`authTokenSecret`** | <code>string</code> |
| **`userName`**        | <code>string</code> |
| **`userID`**          | <code>string</code> |

</docgen-api>
