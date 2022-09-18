import Foundation
import Capacitor

import TwitterKit
import TwitterCore

class TwitterProvider: NSObject, ProviderHandler {

    var window: UIWindow?
    var plugin: LoginProviderPlugin?
    var options: JSObject = [:]

    func initialize(plugin: LoginProviderPlugin, options: JSObject) {
        self.plugin = plugin
        let consumerKey = options["consumerKey"] as? String ?? "ADD_IN_CAPACITOR_CONFIG_TS"
        let consumerSecret = options["consumerSecret"] as? String ?? "ADD_IN_CAPACITOR_CONFIG_TS"

        TWTRTwitter.sharedInstance().start(withConsumerKey: consumerKey, consumerSecret: consumerSecret)

        NotificationCenter.default.addObserver(self, selector: #selector(self.didTwitterRespond(notification:)), name: Notification.Name(Notification.Name.capacitorOpenURL.rawValue), object: nil)
    }

    func isAuthenticated() -> Bool {
        return false
    }

    @objc func didTwitterRespond(notification: NSNotification) {
        let app = UIApplication.shared

        guard let object = notification.object as? [String: Any?] else {
            return
        }

        TWTRTwitter.sharedInstance().application(app, open: object["url"] as! URL, options: object["options"] as! [AnyHashable: Any])
    }

    func isLogged(call: CAPPluginCall) {
        DispatchQueue.main.async {
            if TWTRTwitter.sharedInstance().sessionStore.hasLoggedInUsers() {
                call.resolve(["in": true, "out": false])
            } else {
                call.resolve(["in": false, "out": true])
            }
        }
    }

    func login(call: CAPPluginCall) {
        DispatchQueue.main.async {
            TWTRTwitter.sharedInstance().logIn(completion: { (session, error) in
                if session != nil { // Log in succeeded
                    TWTRTwitter.sharedInstance().sessionStore.saveSession(withAuthToken: session!.authToken, authTokenSecret: session!.authTokenSecret) { _, _ in
                    }
                    call.resolve(LoginProviderHelper.createLoginProviderResponsePayload(
                        provider: "TWITTER",
                        token: session?.authToken,
                        secret: session?.authTokenSecret,
                        email: "",
                        avatarUrl: "",
                        inviteCode: call.getString("inviteCode")
                    ))
                } else {
                    NSLog("logIn ERROR: \(String(describing: error))")
                    call.reject("error")
                }
            })
        }
    }

    func logout(call: CAPPluginCall) {
        DispatchQueue.main.async {
            let store = TWTRTwitter.sharedInstance().sessionStore

            if let userId = store.session()?.userID {
                store.logOutUserID(userId)
                call.resolve()
            }
        }
    }
}
