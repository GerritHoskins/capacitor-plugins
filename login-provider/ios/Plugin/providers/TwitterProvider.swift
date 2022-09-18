import Foundation
import Capacitor

import TwitterKit
import TwitterCore

class TwitterProvider: NSObject {
    {
    var window: UIWindow?
    var plugin: LoginProviderPlugin

    func initialize(plugin: LoginProviderPlugin, options: JSObject) {
    self.plugin = plugin
    let consumerKey = options.getConfigValue("consumerKey") as? String ?? "ADD_IN_CAPACITOR_CONFIG_TS"
    let consumerSecret = options.getConfigValue("consumerSecret") as? String ?? "ADD_IN_CAPACITOR_CONFIG_TS"

    TWTRTwitter.sharedInstance().start(withConsumerKey: consumerKey, consumerSecret: consumerSecret)

    NotificationCenter.default.addObserver(self, selector: #selector(self.didTwitterRespond(notification:)), name: Notification.Name(Notification.Name.capacitorOpenURL.rawValue), object: nil)
    }

    @objc func didTwitterRespond(notification: NSNotification) {
    let app = UIApplication.shared

    guard let object = notification.object as? [String:Any?] else {
    return
    }

    TWTRTwitter.sharedInstance().application(app, open: object["url"] as! URL, options: object["options"] as! [AnyHashable : Any])
    }

    @objc func isLogged(_ call: CAPPluginCall) {
    DispatchQueue.main.async {
    if (TWTRTwitter.sharedInstance().sessionStore.hasLoggedInUsers()) {
    call.resolve(["in": true, "out": false])
    } else {
    call.resolve(["in": false, "out": true])
    }
    }
    }

    @objc func login(_ call: CAPPluginCall) {
    DispatchQueue.main.async {
    TWTRTwitter.sharedInstance().logIn(completion: { (session, error) in
    if session != nil { // Log in succeeded
    TWTRTwitter.sharedInstance().sessionStore.saveSession(withAuthToken: session!.authToken, authTokenSecret: session!.authTokenSecret) { session, error in
    }
    let result = LoginProviderHelper.createLoginProviderResponsePayload(
    "TWITTER",
    session?.authToken as Any,
    session?.authTokenSecret as Any,
    null,
    null,
    call.getData().getString("inviteCode")
    )
    call.resolve(result)
    } else {
    print("logIn ERROR: \(String(describing: error))")
    call.reject("error")
    }
    })
    }
    }

    @objc func logout(_ call: CAPPluginCall) {
    DispatchQueue.main.async {
    let store = TWTRTwitter.sharedInstance().sessionStore

    if let userId = store.session()?.userID {
    store.logOutUserID(userId)
    call.resolve()
    }
    }
    }
}
