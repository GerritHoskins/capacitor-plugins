import Foundation
import Capacitor
import AppleProvider

@objc(LoginProviderPlugin)
public class LoginProviderPlugin: CAPPlugin {

    var appleProvider: AppleProvider?
    var facebookProvider: FacebookProvider?
    var googleProvider: GoogleProvider?
    var twitterProvider: TwitterProvider?

    var providers: ProvidersMap = [:]

   override public func load() {
       let appleOptions = getConfigValue("apple") as? JSObject ?? "CHECK CAPACITOR CONFIG TS"
       let facebookOptions = getConfigValue("facebook") as? JSObject ?? "CHECK CAPACITOR CONFIG TS"
       let googleOptions = getConfigValue("google") as? JSObject ?? "CHECK CAPACITOR CONFIG TS"
       let twitterOptions = getConfigValue("twitter") as? JSObject ?? "CHECK CAPACITOR CONFIG TS"

       self.providers["APPLE"] = AppleProvider()
       self.providers["APPLE"]?.initialize(plugin: self, options: appleOptions)

       self.providers["FACEBOOK"] = FacebookProvider()
       self.providers["FACEBOOK"]?.initialize(plugin: self, options: appleOptions)

       self.providers["GOOGLE"] = GoogleProvider()
       self.providers["GOOGLE"]?.initialize(plugin: self, options: appleOptions)

       self.providers["TWITTER"] = TwitterProvider()
       self.providers["TWITTER"]?.initialize(plugin: self, options: appleOptions)

       //self.implementation?.appStateObserver = { [weak self] in
    }

    @objc func loginWithProvider(_ call: CAPPluginCall) {
        guard let theProvider : ProviderHandler = self.getProvider(call: call) else {
            // call.reject inside getProvider
            return
        }

        guard let callbackId = call.callbackId else {
            call.reject("The call has no callbackId")
            return
        }

        self.callbackId = callbackId
        call.save()

       let providerName = call.getString("provider")
        switch (providerName){
        case "APPLE":
            self.providers["APPLE"]?.login(call)
        case "GOOGLE":
            self.providers["GOOGLE"]?.login(call)
        case "FACEBOOK":
            self.providers["FACEBOOK"]?.login(call)
        case "TWITTER":
            self.providers["TWITTER"]?.login(call)
        }

    }

    @objc func logoutFromProvider(_ call: CAPPluginCall) {
      let providerName = call.getString("provider")
        switch (providerName){
        case "APPLE":
            self.providers["APPLE"]?.logout(call)
        case "GOOGLE":
            self.providers["GOOGLE"]?.logout(call)
        case "FACEBOOK":
            self.providers["FACEBOOK"]?.logout(call)
        case "TWITTER":
            self.providers["TWITTER"]?.logout(call)
        }

    }

    func getProvider(call: CAPPluginCall) -> ProviderHandler? {
        guard let providerId = call.getString("providerName") else {
            call.reject("The provider name is required")
            return nil
        }

        guard let theProvider = self.providers[providerId] else {
            call.reject("The provider is disable or unsupported")
            return nil
        }

        return theProvider
    }

    @objc func loginWithApple(_ call: CAPPluginCall) {
        call.unimplemented()
    }

    @objc func loginWithFacebook(_ call: CAPPluginCall) {
        call.unimplemented()
    }

    @objc func loginWithGoogle(_ call: CAPPluginCall) {
        call.unimplemented()
    }

     @objc func loginWithTwitter(_ call: CAPPluginCall) {
        call.unimplemented()
    }

    @objc func addListener(_ call: CAPPluginCall) {
        call.unimplemented()
    }

    @objc func removeAllListeners(_ call: CAPPluginCall) {
        call.unimplemented()
    }
}
