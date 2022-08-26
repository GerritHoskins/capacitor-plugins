import Foundation
import Capacitor

@objc(LoginProviderPlugin)
public class LoginProviderPlugin: CAPPlugin {
    private let implementation = LoginProvider()

   override public func load() {
       self.implementation = LoginProvider(plugin: self, config: loginProviderConfig())
       self.implementation?.appStateObserver = { [weak self] in
       self?.handleAuthStateChange()
    }

    @objc func signInWithApple(_ call: CAPPluginCall) {
        implementation?.signInWithApple(call)
    }

    @objc func signInWithFacebook(_ call: CAPPluginCall) {
        implementation?.signInWithFacebook(call)
    }

    @objc func signInWithGoogle(_ call: CAPPluginCall) {
        implementation?.signInWithGoogle(call)
    }

     @objc func signInWithTwitter(_ call: CAPPluginCall) {
        implementation?.signInWithTwitter(call)
    }

    @objc func handleAuthStateChange() {
        let user = implementation?.getCurrentUser()
        let userResult = LoginProviderHelper.createUserResult(user)
        var result = JSObject()
        result["user"] = userResult
        notifyListeners(appStateChangeEvent, data: result)
    }

    private func loginProviderConfig() -> LoginProviderConfig {
        var config = LoginProviderConfig()

        if let skipNativeAuth = getConfigValue("skipNativeAuth") as? Bool {
            config.skipNativeAuth = skipNativeAuth
        }
        if let providers = getConfigValue("providers") as? [String] {
            config.providers = providers
        }

        return config
    }
}
