import Foundation
import AppsFlyerLib

@objc public class AppsflyerUid: NSObject {
    @objc public func getUID() -> String {
        var uid = ""
        if(AppsFlyerLib.shared().getAppsFlyerUID() != "") {
            uid = AppsFlyerLib.shared().getAppsFlyerUID()
        }

        return uid
    }
}
