import Foundation
import Capacitor
#if RGCFA_INCLUDE_GOOGLE
import GoogleSignIn
#endif

class GoogleAuthProviderHandler: NSObject {
    var pluginImplementation: LoginProvider
    var googleSignIn: GIDSignIn!;
    var googleSignInConfiguration: GIDConfiguration!;
    var forceAuthCode: Bool = false;
    var additionalScopes: [String]!;

    init(_ pluginImplementation: LoginProvider) {
        self.pluginImplementation = pluginImplementation
        super.init()
    }

    func initialize(call: CAPPluginCall) {
        call.resolve();
    }

    func signIn(call: CAPPluginCall) {
        #if RGCFA_INCLUDE_GOOGLE
        guard let clientId = FirebaseApp.app()?.options.clientID else { return }
        let config = GIDConfiguration(clientID: clientId)
        guard let controller = self.pluginImplementation.getPlugin().bridge?.viewController else { return }
        let scopes = call.getArray("scopes", String.self) ?? []

        signInCall = call;
        DispatchQueue.main.async {
            if self.googleSignIn.hasPreviousSignIn() && !self.forceAuthCode {
                self.googleSignIn.restorePreviousSignIn() { user, error in
                if let error = error {
                    self.signInCall?.reject(error.localizedDescription);
                    return;
                }
                self.resolveSignInCallWith(user: user!)
                }
            } else {
                let presentingVc = self.bridge!.viewController!;

                self.googleSignIn.signIn(with: self.googleSignInConfiguration, presenting: presentingVc) { user, error in
                    if let error = error {
                        self.signInCall?.reject(error.localizedDescription, "\(error._code)");
                        return;
                    }
                    if self.additionalScopes.count > 0 {
                        // requesting additional scopes in GoogleSignIn-iOS SDK 6.0 requires that you sign the user in and then request additional scopes,
                        // there's no method to include the additional scopes in the initial sign in request
                        self.googleSignIn.addScopes(self.additionalScopes, presenting: presentingVc) { user, error in
                            if let error = error {
                                self.signInCall?.reject(error.localizedDescription);
                                return;
                            }
                            self.resolveSignInCallWith(user: user!);
                        }
                    } else {
                        self.resolveSignInCallWith(user: user!);
                    }
                };
            }
        }
        #endif
    }

    /*func signOut() {
        #if RGCFA_INCLUDE_GOOGLE
        GIDSignIn.sharedInstance.signOut()
        #endif
    }*/

    func refresh(call: CAPPluginCall) {
        DispatchQueue.main.async {
            if self.googleSignIn.currentUser == nil {
                call.reject("User not logged in.");
                return
            }
            self.googleSignIn.currentUser!.authentication.do { (authentication, error) in
                guard let authentication = authentication else {
                    call.reject(error?.localizedDescription ?? "Something went wrong.");
                    return;
                }
                let authenticationData: [String: Any] = [
                    "accessToken": authentication.accessToken,
                    "idToken": authentication.idToken ?? NSNull(),
                    "refreshToken": authentication.refreshToken
                ]
                call.resolve(authenticationData);
            }
        }
    }

    func signOut(call: CAPPluginCall) {
        DispatchQueue.main.async {
            self.googleSignIn.signOut();
        }
        call.resolve();
    }

    func handleOpenUrl(notification: Notification) {
        guard let object = notification.object as? [String: Any] else {
            print("There is no object on handleOpenUrl");
            return;
        }
        guard let url = object["url"] as? URL else {
            print("There is no url on handleOpenUrl");
            return;
        }
        googleSignIn.handle(url);
    }


    func getClientIdValue() -> String? {
        if let clientId = getConfigValue("iosClientId") as? String {
            return clientId;
        }
        else if let clientId = getConfigValue("clientId") as? String {
            return clientId;
        }
        else if let path = Bundle.main.path(forResource: "GoogleService-Info", ofType: "plist"),
                let dict = NSDictionary(contentsOfFile: path) as? [String: AnyObject],
                let clientId = dict["CLIENT_ID"] as? String {
            return clientId;
        }
        return nil;
    }

    func getServerClientIdValue() -> String? {
        if let serverClientId = getConfigValue("serverClientId") as? String {
            return serverClientId;
        }
        return nil;
    }

    func resolveSignInCallWith(user: GIDGoogleUser) {
        var userData: [String: Any] = [
            "authentication": [
                "accessToken": user.authentication.accessToken,
                "idToken": user.authentication.idToken,
                "refreshToken": user.authentication.refreshToken
            ],
            "serverAuthCode": user.serverAuthCode ?? NSNull(),
            "email": user.profile?.email ?? NSNull(),
            "familyName": user.profile?.familyName ?? NSNull(),
            "givenName": user.profile?.givenName ?? NSNull(),
            "id": user.userID ?? NSNull(),
            "name": user.profile?.name ?? NSNull()
        ];
        if let imageUrl = user.profile?.imageURL(withDimension: 100)?.absoluteString {
            userData["imageUrl"] = imageUrl;
        }
        signInCall?.resolve(userData);
    }
}
