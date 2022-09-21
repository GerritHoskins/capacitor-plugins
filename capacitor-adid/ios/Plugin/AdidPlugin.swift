import Foundation
import Capacitor

@objc(AdidPlugin)
public class AdidPlugin: CAPPlugin {
    private let adid = Adid()

    @objc func getId(_ call: CAPPluginCall) {
        call.keepAlive = true

        var delay = 0
        if (call.getString("delay")) != nil {
            delay = Int(call.getString("delay")!)!
        }

        adid.requestTrackingAuth(delay: delay as NSNumber) { (id) in
            DispatchQueue.main.async {
                if id != "" {
                    call.resolve([
                        "id": id
                    ])
                } else {
                    call.reject(self.adid.status)
                }
            }
        }
    }

    @objc func getStatus(_ call: CAPPluginCall) {
        call.keepAlive = true
        adid.getStatus { (status) in
            DispatchQueue.main.async {
                call.resolve([
                    "status": self.adid.status,
                    "statusId": status
                ])
            }
        }

    }
}
