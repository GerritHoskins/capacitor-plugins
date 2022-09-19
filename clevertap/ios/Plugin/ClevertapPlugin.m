#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(ClevertapPlugin, "ClevertapPlugin",
           CAP_PLUGIN_METHOD(cleverTap, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getClevertapId, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(registerFBM, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getDeliveredNotifications, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(removeDeliveredNotifications, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(createChannel, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(onUserLogin, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(pushEvent, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(pushNotification, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(pushPrivacy, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(pushUser, CAPPluginReturnPromise);
)
