import Foundation
import Capacitor
import CleverTapSDK

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(ClevertapPlugin)
public class ClevertapPlugin: CAPPlugin, UNUserNotificationCenterDelegate, CleverTapPushNotificationDelegate {
    override public func load() {
        self.registerForPush()

        CleverTap.autoIntegrate()
        CleverTap.setDebugLevel(CleverTapLogLevel.debug.rawValue)
        NSLog("CleverTap Plugin loaded")
        CleverTap.sharedInstance()?.enableDeviceNetworkInfoReporting(true)

        super.load()
    }

    func registerForPush() {
        UNUserNotificationCenter.current().delegate = self
        UNUserNotificationCenter.current().requestAuthorization(options: [.sound, .badge, .alert], completionHandler: {granted, _ in
            if granted {
                DispatchQueue.main.async {
                    UIApplication.shared.registerForRemoteNotifications()
                }
            }
        })
    }
}
