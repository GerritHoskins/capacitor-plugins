#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(MparticlePlugin, "Mparticle",
           CAP_PLUGIN_METHOD(echo, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(logMparticleEvent, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(mParticleInit, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(application, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(logMparticleEvent, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(logMparticlePageView, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(setUserAttribute, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(setUserAttributeList, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(loginMparticleUser, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(logoutMparticleUser, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getUserAttributeLists, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(registerMparticleUser, CAPPluginReturnPromise);
)
