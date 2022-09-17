#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(LoginProviderPlugin, "LoginProvider",
             CAP_PLUGIN_METHOD(loginWithProvider, CAPPluginReturnPromise);
              CAP_PLUGIN_METHOD(loginWithApple, CAPPluginReturnPromise);
              CAP_PLUGIN_METHOD(loginWithFacebook, CAPPluginReturnPromise);
              CAP_PLUGIN_METHOD(loginWithGoogle, CAPPluginReturnPromise);
              CAP_PLUGIN_METHOD(loginWithTwitter, CAPPluginReturnPromise);
              CAP_PLUGIN_METHOD(logoutFromProvider, CAPPluginReturnPromise);
              CAP_PLUGIN_METHOD(addListener, CAPPluginReturnNone);
              CAP_PLUGIN_METHOD(removeAllListeners, CAPPluginReturnNone);
)
