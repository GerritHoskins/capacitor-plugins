import Foundation
import Capacitor

typealias JSObject = [String: Any]
typealias JSArray = [JSObject]
typealias ProvidersMap = [String: ProviderHandler]

@objc(LoginProviderPlugin)
public class LoginProviderPlugin: CAPPlugin {

    var appleProvider: AppleProvider?
    var facebookProvider: FacebookProvider?
    var googleProvider: GoogleProvider?
    var twitterProvider: TwitterProvider?

    var providers: ProvidersMap = [:]
    var callbackId: String?

    var mOptions: JSObject?

    override public func load() {
        let appleOptions = self.convertOptions(options: getConfigValue("apple"))
        let facebookOptions = getConfigValue("facebook")
        let googleOptions = getConfigValue("google")
        let twitterOptions =  self.convertOptions(options: getConfigValue("twitter"))

        self.providers["APPLE"] = AppleProvider()
        self.providers["APPLE"]?.initialize(plugin: self, options: appleOptions)

        self.providers["FACEBOOK"] = FacebookProvider()
        self.providers["FACEBOOK"]?.initialize(plugin: self, options: facebookOptions as! JSObject)

        self.providers["GOOGLE"] = GoogleProvider()
        self.providers["GOOGLE"]?.initialize(plugin: self, options: googleOptions as! JSObject)

        self.providers["TWITTER"] = TwitterProvider()
        self.providers["TWITTER"]?.initialize(plugin: self, options: twitterOptions)

        // self.implementation?.appStateObserver = { [weak self] in
    }

    func convertOptions(options: Any?) -> JSObject {
        if let res = options as? [String: Any] {
            mOptions = res
        }
        return mOptions!
    }

    @objc func loginWithProvider(_ call: CAPPluginCall) {
        guard let providerInstance: ProviderHandler = self.getProvider(call: call) else {
            // call.reject inside getProvider
            return
        }

        guard let callbackId = call.callbackId else {
            call.reject("The call has no callbackId")
            return
        }

        self.callbackId = callbackId
        call.save()

        DispatchQueue.main.async {
            if providerInstance.isAuthenticated() {
                return
            }

            providerInstance.login(call: call)
        }
    }

    @objc func logoutFromProvider(call: CAPPluginCall) {
        do {
            for provider in self.providers.values {
                try provider.logout(call: call)
            }
            call.resolve()
        } catch let logoutError as NSError {
            NSLog("logout error: %@", logoutError)
            call.reject("logout error: \(logoutError)")
        }
    }

    func getProvider(call: CAPPluginCall) -> ProviderHandler? {
        guard let providerId = call.getString("provider") else {
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

    @objc override public func addListener(_ call: CAPPluginCall) {
        call.unimplemented()
    }

    @objc override public func removeAllListeners(_ call: CAPPluginCall) {
        call.unimplemented()
    }
}
