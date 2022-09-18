import Foundation
import Capacitor
import AuthenticationServices

class AppleProvider: NSObject, ProviderHandler {
    var plugin: LoginProviderPlugin?
    var options: JSObject = [:]

    func initialize(plugin: LoginProviderPlugin, options: JSObject) {
        self.plugin = plugin
        self.options = options
    }

    @objc func login(call: CAPPluginCall) {
        if #available(iOS 13.0, *) {
            let appleIDProvider = ASAuthorizationAppleIDProvider()
            let request = appleIDProvider.createRequest()
            request.requestedScopes = getRequestedScopes(from: call)
            request.state = call.getString("state")
            request.nonce = call.getString("nonce")

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

    @objc func logout(all call: CAPPluginCall) {
        call.resolve()
    }

    func isAuthenticated() -> Bool {
        return false
    }

    @available(iOS 13.0, *)
    func getRequestedScopes(from call: CAPPluginCall) -> [ASAuthorization.Scope]? {
        var requestedScopes: [ASAuthorization.Scope] = []

        if let scopesStr = call.getString("scopes") {
            if scopesStr.contains("name") {
                requestedScopes.append(.fullName)
            }

            if scopesStr.contains("email") {
                requestedScopes.append(.email)
            }
        }

        if requestedScopes.count > 0 {
            return requestedScopes
        }

        return nil
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
        ))

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
