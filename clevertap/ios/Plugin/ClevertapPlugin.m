#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(ClevertapPlugin, "Clevertap",
           CAP_PLUGIN_METHOD(cleverTap, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getClevertapId, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(registerFBM, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getDeliveredNotifications, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(removeDeliveredNotifications, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(options, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(options, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(options, CAPPluginReturnPromise);
)
