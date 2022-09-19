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
        let appleOptions = getConfigValue("apple")
        let facebookOptions = getConfigValue("facebook")
        let googleOptions = getConfigValue("google")
        let twitterOptions = getConfigValue("twitter")

        self.providers["APPLE"] = AppleProvider()
        self.providers["APPLE"]?.initialize(plugin: self, options: appleOptions as! JSObject)

        self.providers["FACEBOOK"] = FacebookProvider()
        self.providers["FACEBOOK"]?.initialize(plugin: self, options: facebookOptions as! JSObject)

        self.providers["GOOGLE"] = GoogleProvider()
        self.providers["GOOGLE"]?.initialize(plugin: self, options: googleOptions as! JSObject)

        self.providers["TWITTER"] = TwitterProvider()
        self.providers["TWITTER"]?.initialize(plugin: self, options: twitterOptions as! JSObject)
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
        call.keepAlive = true

        DispatchQueue.main.async {
            if providerInstance.isAuthenticated() {
                self.buildResult(credential: nil)
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
            call.reject("provider name is required")
            return nil
        }

        guard let theProvider = self.providers[providerId] else {
            call.reject("provider not supported")
            return nil
        }

        return theProvider
    }

    func buildResult(credential: ProviderResponsePayload?) {
        guard let callbackId = self.callbackId else {
            NSLog("missing plugin callback id")
            return
        }

        guard let call = self.bridge?.savedCall(withID: callbackId) else {
            NSLog("missing plugin callback id")
            return
        }

        let jsResult: PluginCallResultData = [
            "callbackId": callbackId,
            "providerId": call.getString("provider") ?? ""
        ]

        guard let provider: ProviderHandler = self.getProvider(call: call) else {
            return
        }

        call.resolve(provider.fillResult(data: jsResult))
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
