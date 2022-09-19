import Foundation
import Capacitor
import AuthenticationServices
import CryptoKit

class AppleProvider: NSObject, ProviderHandler {
    var plugin: LoginProviderPlugin?
    var options: JSObject = [:]

    func initialize(plugin: LoginProviderPlugin, options: JSObject) {
        self.plugin = plugin
        self.options = options
    }

    func login(call: CAPPluginCall) {
        if #available(iOS 13.0, *) {
            let appleIDProvider = ASAuthorizationAppleIDProvider()
            let request = appleIDProvider.createRequest()
            request.requestedScopes = getRequestedScopes(from: call, configScopes: self.options["scopes"] as! String)
            request.state = self.options["state"] as? String
            request.nonce = self.options["nonce"] as? String

            let defaults = UserDefaults()
            defaults.setValue(call.callbackId, forKey: "callbackId")

            self.plugin?.bridge?.saveCall(call)

            let authorizationController = ASAuthorizationController(authorizationRequests: [request])
            authorizationController.delegate = self
            authorizationController.performRequests()
        } else {
            call.reject("Sign in with Apple is available on iOS 13.0+ only.")
        }
    }

    func logout(call: CAPPluginCall) {
        call.resolve()
    }

    func isAuthenticated() -> Bool {
        return false
    }

    func fillResult(data: PluginCallResultData) -> PluginCallResultData {
        var jsResult: PluginCallResultData = [:]

        data.forEach { (key, value) in
            jsResult[key] = value
        }

        return jsResult
    }

    @available(iOS 13.0, *)
    func getRequestedScopes(from call: CAPPluginCall, configScopes: String) -> [ASAuthorization.Scope]? {
        var requestedScopes: [ASAuthorization.Scope] = []
        requestedScopes.append(.fullName)
        requestedScopes.append(.email)

        return requestedScopes
    }
}

@available(iOS 13.0, *)
extension AppleProvider: ASAuthorizationControllerDelegate {
    public func authorizationController(controller: ASAuthorizationController, didCompleteWithAuthorization authorization: ASAuthorization) {
        guard let appleIDCredential = authorization.credential as? ASAuthorizationAppleIDCredential else { return }

        let defaults = UserDefaults()
        let id = defaults.string(forKey: "callbackId") ?? ""
        guard let call = self.plugin?.bridge?.savedCall(withID: id) else {
            return
        }
        call.resolve(LoginProviderHelper.createLoginProviderResponsePayload(
            provider: "APPLE",
            token: String(data: appleIDCredential.identityToken!, encoding: .utf8),
            secret: String(data: appleIDCredential.authorizationCode!, encoding: .utf8),
            email: appleIDCredential.email,
            avatarUrl: "",
            inviteCode: call.getString("inviteCode")
        )
        )

        self.plugin?.bridge?.releaseCall(call)
    }

    public func authorizationController(controller: ASAuthorizationController, didCompleteWithError error: Error) {
        let defaults = UserDefaults()
        let id = defaults.string(forKey: "callbackId") ?? ""
        guard let call = self.plugin?.bridge?.savedCall(withID: id) else {
            return
        }
        call.reject(error.localizedDescription)
        self.plugin?.bridge?.releaseCall(call)
    }
}
