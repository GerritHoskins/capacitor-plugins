#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

CAP_PLUGIN(MultiWebviewPlugin, "MultiWebview",
           CAP_PLUGIN_METHOD(createWebview, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(setFocusedWebview, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getFocusedWebview, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(hideWebview, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(showWebview, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(destroyWebview, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(loadUrl, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(listWebviews, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getWebviewInfo, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getAllWebviews, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getWebviewsByUrl, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(setWebviewFrame, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(executeJavaScript, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(sendMessage, CAPPluginReturnPromise);
)
