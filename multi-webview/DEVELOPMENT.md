# Development Guide

## Setup for iOS Development

If you're getting an `UNIMPLEMENTED` error on iOS, follow these steps:

### 1. Install Dependencies

```bash
cd multi-webview
npm install
```

### 2. Build the Plugin

```bash
npm run build
```

### 3. Set up iOS

```bash
cd ios
pod install
```

### 4. Integration in a Capacitor App

If you're testing this in a Capacitor app:

```bash
# In your app directory
npm install /path/to/multi-webview
npx cap sync ios
```

### 5. Open in Xcode

```bash
npx cap open ios
```

### 6. Clean and Rebuild

In Xcode:
- Product → Clean Build Folder (Cmd+Shift+K)
- Product → Build (Cmd+B)

### 7. Verify Plugin Registration

Check that the plugin is registered in your `AppDelegate.swift` or that Capacitor auto-discovers it.

## Troubleshooting UNIMPLEMENTED Error

The `UNIMPLEMENTED` error typically means:

1. **Plugin not synced**: Run `npx cap sync ios` in your app
2. **Pod not installed**: Run `pod install` in your app's `ios/App` directory
3. **Stale build**: Clean build folder in Xcode
4. **Wrong plugin name**: Verify you're using `MultiWebview` (not `multiWebview` or `multi-webview`)

## Testing

### Check Plugin is Loaded

```typescript
import { MultiWebview } from '@gerrithoskins/multi-webview';

// Try a simple method first
try {
  const result = await MultiWebview.listWebviews();
  console.log('Plugin loaded successfully:', result);
} catch (error) {
  console.error('Plugin error:', error);
}
```

### Verify Native Methods

In Xcode console, you should see Capacitor loading the plugin:
```
⚡️  To Native ->  MultiWebview methodName
```

## Common Issues

### Issue: UNIMPLEMENTED on new methods (getWebviewsByUrl, etc.)

**Solution**: Make sure you have the latest version (1.1.0+) and have run:
```bash
cd ios
pod install
pod update
```

Then clean and rebuild in Xcode.

### Issue: Plugin not found

**Solution**: Check package.json in your app includes:
```json
{
  "dependencies": {
    "@gerrithoskins/multi-webview": "^1.1.0"
  }
}
```

Run `npx cap sync ios` after installation.

### Issue: Swift compilation errors

**Solution**: Ensure your iOS deployment target is at least iOS 13.0 in your app's Podfile:
```ruby
platform :ios, '13.0'
```

## Manual Verification

### Check if plugin methods are exposed:

```bash
# In the multi-webview directory
grep -r "CAP_PLUGIN_METHOD" ios/
```

Should show all methods including:
- createWebview
- getWebviewInfo
- getAllWebviews
- getWebviewsByUrl
- (and others)

### Verify Swift files compile:

```bash
cd ios
xcodebuild -workspace Plugin.xcworkspace -scheme Plugin -destination 'platform=iOS Simulator,name=iPhone 14' clean build
```
