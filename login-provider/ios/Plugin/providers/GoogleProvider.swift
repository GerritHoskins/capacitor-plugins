import Foundation
import Capacitor
import GoogleSignIn

class GoogleProvider: NSObject, ProviderHandler {

    var plugin: LoginProviderPlugin?
    var options: JSObject = [:]

    var googleSignIn: GIDSignIn!
    var googleSignInConfiguration: GIDConfiguration!
    var forceAuthCode: Bool = false
    var additionalScopes: [String]!

    func initialize(plugin: LoginProviderPlugin, options: JSObject) {
        self.plugin = plugin
        self.options = options
    }

    func login(call: CAPPluginCall) {
        #if RGCFA_INCLUDE_GOOGLE
        guard let clientId = FirebaseApp.app()?.options.clientID else { return }
        let config = GIDConfiguration(clientID: clientId)
        guard let controller = self.pluginImplementation.getPlugin().bridge?.viewController else { return }
        let scopes = self.options.getString("scopes", String.self) ?? []

        signInCall = call
        DispatchQueue.main.async {
            if self.googleSignIn.hasPreviousSignIn() && !self.forceAuthCode {
                self.googleSignIn.restorePreviousSignIn { user, error in
                    if let error = error {
                        self.signInCall?.reject(error.localizedDescription)
                        return
                    }
                    self.resolveSignInCallWith(call: call, user: user!)
                }
            } else {
                let presentingVc = self.bridge!.viewController!

                self.googleSignIn.signIn(with: self.googleSignInConfiguration, presenting: presentingVc) { user, error in
                    if let error = error {
                        self.signInCall?.reject(error.localizedDescription, "\(error._code)")
                        return
                    }
                    if self.additionalScopes.count > 0 {
                        // requesting additional scopes in GoogleSignIn-iOS SDK 6.0 requires that you sign the user in and then request additional scopes,
                        // there's no method to include the additional scopes in the initial sign in request
                        self.googleSignIn.addScopes(self.additionalScopes, presenting: presentingVc) { user, error in
                            if let error = error {
                                self.signInCall?.reject(error.localizedDescription)
                                return
                            }
                            self.resolveSignInCallWith(call: call, user: user!)
                        }
                    } else {
                        self.resolveSignInCallWith(call: call, user: user!)
                    }
                }
            }
        }
        #endif
    }

    func refresh(call: CAPPluginCall) {
        DispatchQueue.main.async {
            if self.googleSignIn.currentUser == nil {
                call.reject("User not logged in.")
                return
            }
            self.googleSignIn.currentUser!.authentication.do { (authentication, error) in
                guard let authentication = authentication else {
                    call.reject(error?.localizedDescription ?? "Something went wrong.")
                    return
                }
                let authenticationData: [String: Any] = [
                    "accessToken": authentication.accessToken,
                    "idToken": authentication.idToken ?? NSNull(),
                    "refreshToken": authentication.refreshToken
                ]
                call.resolve(authenticationData)
            }
        }
    }

    func logout(call: CAPPluginCall) {
        DispatchQueue.main.async {
            self.googleSignIn.signOut()
        }
        call.resolve()
    }

    func isAuthenticated() -> Bool {
        return self.googleSignIn.currentUser != nil
    }

    func handleOpenUrl(notification: Notification) {
        guard let object = notification.object as? [String: Any] else {
            print("There is no object on handleOpenUrl")
            return
        }
        guard let url = object["url"] as? URL else {
            print("There is no url on handleOpenUrl")
            return
        }
        googleSignIn.handle(url)
    }

    func getClientIdValue() -> String? {
        if let clientId = self.options["iosClientId"] as? String {
            return clientId
        } else if let clientId = self.options["clientId"] as? String {
            return clientId
        } else if let path = Bundle.main.path(forResource: "GoogleService-Info", ofType: "plist"),
                  let dict = NSDictionary(contentsOfFile: path) as? [String: AnyObject],
                  let clientId = dict["CLIENT_ID"] as? String {
            return clientId
        }
        return nil
    }

    func getServerClientIdValue() -> String? {
        if let serverClientId = self.options["serverClientId"] as? String {
            return serverClientId
        }
        return nil
    }

    func resolveSignInCallWith(call: CAPPluginCall, user: GIDGoogleUser) {
        call.resolve(LoginProviderHelper.createLoginProviderResponsePayload(
            provider: "GOOGLE",
            token: user.authentication.idToken,
            secret: "",
            email: user.profile?.email ?? "",
            avatarUrl: user.profile?.imageURL(withDimension: 100)?.absoluteString,
            inviteCode: call.getString("inviteCode")
        ))
    }
}
