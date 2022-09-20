import Foundation
import Capacitor

import TwitterKit
import TwitterCore

@objc(TwitterPlugin)
public class TwitterPlugin: CAPPlugin
{
    var window: UIWindow?

    public override func load() {
        let consumerKey = getConfig().getString("consumerKey", "consumerKey is missing")
        let consumerSecret = getConfig().getString("consumerSecret", "consumerSecret is missing" )

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
                    call.resolve([
                        "authToken": session?.authToken as Any,
                        "authTokenSecret": session?.authTokenSecret as Any,
                        "userName":session?.userName as Any,
                        "userID": session?.userID as Any
                        ])
                } else {
                    print("logIn ERROR: \(String(describing: error))")
                    call.reject("error");
                }
            })
        }
    }

    @objc func logout(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            let store = TWTRTwitter.sharedInstance().sessionStore

            if let userId = store.session()?.userID {
                store.logOutUserID(userId)
                call.resolve();
            }
        }
    }
}
