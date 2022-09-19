import Foundation
import Capacitor
import CleverTapSDK

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(ClevertapPlugin)
public class ClevertapPlugin: CAPPlugin {
    override public func load() {
        NSLog("clevertap plugin loaded")
    }
}
