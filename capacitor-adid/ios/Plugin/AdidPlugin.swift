import Foundation
import Capacitor
import AdSupport
import AppTrackingTransparency

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(AdidPlugin)
public class AdidPlugin: CAPPlugin {

    @objc func getId(_ call: CAPPluginCall) {
        if #available(iOS 15.0, *) {
            let delayInSeconds: TimeInterval = 1.0
            DispatchQueue.main.asyncAfter(deadline: .now() + delayInSeconds) {
                ATTrackingManager.requestTrackingAuthorization { status in
                    switch status {
                    case .authorized:
                        call.resolve([
                            "id": ASIdentifierManager.shared().advertisingIdentifier.uuidString,
                            "isDummy": false
                        ])
                    case .denied:
                        call.reject("denied")
                    case .notDetermined:
                        call.reject("not determined")
                    case .restricted:
                        call.reject("restricted")
                    @unknown default:
                        call.reject("unknown")
                    }
                }
            }
        } else if #available(iOS 14, *) {
            ATTrackingManager.requestTrackingAuthorization { status in
                switch status {
                case .authorized:
                    call.resolve([
                        "id": ASIdentifierManager.shared().advertisingIdentifier.uuidString,
                        "isDummy": false
                    ])
                case .denied:
                    call.reject("denied")
                case .notDetermined:
                    call.reject("not determined")
                case .restricted:
                    call.reject("restricted")
                @unknown default:
                    call.reject("unknown")
                }
            }
        } else {
            call.resolve([
                "id": 11111111111,
                "isDummy": true
            ])
        }
    }
}
