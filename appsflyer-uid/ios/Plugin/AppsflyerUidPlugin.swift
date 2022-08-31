import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(AppsflyerUidPlugin)
public class AppsflyerUidPlugin: CAPPlugin {
    private let implementation = AppsflyerUid()

    @objc func getUID(_ call: CAPPluginCall) {
        call.unimplemented();
    }
}
