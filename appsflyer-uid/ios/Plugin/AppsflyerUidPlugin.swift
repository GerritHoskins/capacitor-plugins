import Foundation
import Capacitor

@objc(AppsflyerUidPlugin)
public class AppsflyerUidPlugin: CAPPlugin {
    private let implementation = AppsflyerUid()

    @objc func getUID(_ call: CAPPluginCall) {
        let uid = implementation.getUID();
        if(uid == "") {
            call.reject("AppsflyerUidPlugin: failed to retrieve uid.")
            return
        }

        call.resolve([
            "uid": uid
        ])
    }
}
