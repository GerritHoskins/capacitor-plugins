import Foundation
import Capacitor

import TwitterLoginKit

@objc(TwitterPlugin)
public class TwitterPlugin: CAPPlugin {
    var window: UIWindow?
    var viewControl: UIViewController?

    override public func load() {
        let consumerKey =  "oDIRqTkT1NaNjwXxqzFrxSWFh" // getConfig("consumerKey") as? String ?? "ADD_IN_CAPACITOR_CONFIG_JSON"
        let consumerSecret = "B2L4Oc4bdSbS9YMdqdI192oSj542meqSbNGZS9vzQQgdCAHwyR" // getConfig("consumerSecret") as? String ?? "ADD_IN_CAPACITOR_CONFIG_JSON"

        // TwitterLoginKit.shared.start(withConsumerKey: consumerKey, consumerSecret: consumerSecret)

        //  NotificationCenter.default.addObserver(self, selector: #selector(self.didTwitterRespond(notification:)), name: //Notification.Name(Notification.Name.capacitorOpenURL.rawValue), object: nil)
    }

    @objc func didTwitterRespond(notification: NSNotification) {
        let app = UIApplication.shared

        guard let object = notification.object as? [String: Any?] else {
            return
        }
        TwitterLoginKit.shared.application(app, open: object["url"]  as! URL, options: object["options"] as! [UIApplication.OpenURLOptionsKey: Any])
        // TwitterLoginKit.shared.application(app, open: object["url"] as! URL, options: object["options"] as! [AnyHashable : Any])
    }

    @objc func login(_ call: CAPPluginCall) {
        TwitterLoginKit.shared.login(withViewController: self.bridge!.viewController!) { (state) in
            switch state {
            case .failure(let error):
                print(error.localizedDescription)
            case .success(let session):
                print(session)
                call.resolve([
                    "authToken": session.authToken as Any,
                    "authTokenSecret": session.authTokenSecret as Any,
                    "userName": session.userName as Any,
                    "userID": session.userID as Any
                ])
            }
        }
    }
}
