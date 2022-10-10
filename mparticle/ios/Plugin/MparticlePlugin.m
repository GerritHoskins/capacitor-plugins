#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(MparticlePlugin, "Mparticle",
           CAP_PLUGIN_METHOD(identifyUser, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(setUserAttribute, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(setUserAttributes, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(setGDPRConsent, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getGDPRConsent, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getMPID, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(trackEvent, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(trackPageView, CAPPluginReturnPromise);
)
