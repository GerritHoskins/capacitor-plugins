#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(MparticlePlugin, "Mparticle",
           CAP_PLUGIN_METHOD(echo, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(initConfig, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(init, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(identifyUser, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(setUserAttribute, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(setGDPRConsent, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getGDPRConsent, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getMPID, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(logEvent, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(logPageView, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(loginUser, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(logoutUser, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(registerUser, CAPPluginReturnPromise);

)
