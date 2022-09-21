import Foundation
import AdSupport
import AppTrackingTransparency

@objc public class Adid: NSObject {
    var delayInSeconds: TimeInterval = 0
    var advertisingIdentifier = ""
    var status: String = "authorized"

    @objc public func requestTrackingAuth (delay: NSNumber, returnCompletion: @escaping (String) -> Void ) {
        var data: String = ""

        if #available(iOS 15.0, *) {
            self.delayInSeconds = TimeInterval(truncating: delay)
        }
        if #available(iOS 14.0, *) {
            DispatchQueue.main.asyncAfter(deadline: .now() + self.delayInSeconds) {
                ATTrackingManager.requestTrackingAuthorization { status in
                    switch status {
                    case .authorized:
                        self.status =  "authorized"
                        data = ASIdentifierManager.shared().advertisingIdentifier.uuidString
                    case .denied:
                        self.status =  "denied"
                    case .notDetermined:
                        self.status =  "not determined"
                    case .restricted:
                        self.status =  "restricted"
                    @unknown default:
                        self.status =  "unknown"
                    }
                }
                returnCompletion(data as String)
            }
        } else {
            DispatchQueue.main.async {
                data = ASIdentifierManager.shared().advertisingIdentifier.uuidString
                self.status =  "authorized"
                returnCompletion(data as String)
            }
        }

    }

    @objc public func getStatus(returnCompletion: @escaping (Int) -> Void ) {
        var data: Int = 0
        if #available(iOS 14.0, *) {
            DispatchQueue.main.async {
                let authStatus: ATTrackingManager.AuthorizationStatus = ATTrackingManager.trackingAuthorizationStatus
                data = Int(authStatus.rawValue)
                if authStatus.rawValue != 3 {
                    self.status = "denied"
                }
                returnCompletion(data as Int)
            }
        }
    }
}
