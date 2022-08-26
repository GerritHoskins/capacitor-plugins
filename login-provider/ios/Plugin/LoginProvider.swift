import Foundation
import Capacitor

public typealias AppStateChangedObserver = () -> Void

@objc public class LoginProvider: NSObject {
    public var appStateObserver: AppStateChangedObserver?
    private let plugin: LoginProviderPlugin
    private let config: LoginProviderConfig
    private var appleAuthProviderHandler: AppleAuthProviderHandler?
    private var facebookAuthProviderHandler: FacebookAuthProviderHandler?
    private var googleAuthProviderHandler: GoogleAuthProviderHandler?
    private var oAuthProviderHandler: OAuthProviderHandler?
    private var savedCall: CAPPluginCall?

    init(plugin: LoginProviderPlugin, config: LoginProviderConfig) {
        self.plugin = plugin
        self.config = config
        super.init()
        if LoginProvider.app() == nil {
            LoginProvider.configure()
        }
        //self.initAuthProviderHandlers(config: config)
        //App.addStateDidChangeListener {_, _ in
        //    self.appStateObserver?()
        //}
    }

    @objc func signInWithApple(_ call: CAPPluginCall) {
        self.savedCall = call
        self.appleAuthProviderHandler?.signIn(call: call)
    }

    @objc func signInWithFacebook(_ call: CAPPluginCall) {
        self.savedCall = call
        self.facebookAuthProviderHandler?.signIn(call: call)
    }

    @objc func signInWithGoogle(_ call: CAPPluginCall) {
        self.savedCall = call
        self.googleAuthProviderHandler?.signIn(call: call)
    }

    @objc func signInWithTwitter(_ call: CAPPluginCall) {
        self.savedCall = call
        self.oAuthProviderHandler?.signIn(call: call, providerId: "TWITTER")
    }

    @objc func signOut(_ call: CAPPluginCall) {
        do {
            try Auth.auth().signOut()
            googleAuthProviderHandler?.signOut()
            facebookAuthProviderHandler?.signOut()
            call.resolve()
        } catch let signOutError as NSError {
            call.reject("Error signing out")
        }
    }

    func handleSuccessfulSignIn(credential: AuthCredential, idToken: String?, nonce: String?, accessToken: String?) {
        if config.skipNativeAuth == true {
            guard let savedCall = self.savedCall else {
                return
            }
            let result = LoginProviderHelper.createSignInResult(credential: credential, user: nil, idToken: idToken, nonce: nonce, accessToken: accessToken, additionalUserInfo: nil)
            savedCall.resolve(result)
            return
        }
        Auth.auth().signIn(with: credential) { (authResult, error) in
            if let error = error {
                self.handleFailedSignIn(message: nil, error: error)
                return
            }
            guard let savedCall = self.savedCall else {
                return
            }
            let result = LoginProviderHelper.createSignInResult(credential: authResult?.credential, user: authResult?.user, idToken: idToken, nonce: nonce, accessToken: accessToken,
                                                                         additionalUserInfo: authResult?.additionalUserInfo)
            savedCall.resolve(result)
        }
    }

    func handleFailedSignIn(message: String?, error: Error?) {
        guard let savedCall = self.savedCall else {
            return
        }
        let errorMessage = message ?? error?.localizedDescription ?? ""
        savedCall.reject(errorMessage, nil, error)
    }

    func getPlugin() -> LoginProviderPlugin {
        return self.plugin
    }

    private func initAuthProviderHandlers(config: LoginProviderConfig) {
        if config.providers.contains("APPLE") {
            self.appleAuthProviderHandler = AppleAuthProviderHandler(self)
        }
        if config.providers.contains("FACEBOOK") {
            self.facebookAuthProviderHandler = FacebookAuthProviderHandler(self)
        }
        if config.providers.contains("GOOGLE") {
            self.googleAuthProviderHandler = GoogleAuthProviderHandler(self)
        }
        self.oAuthProviderHandler = OAuthProviderHandler(self)
    }
}
