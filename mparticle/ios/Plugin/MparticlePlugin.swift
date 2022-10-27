import Foundation
import Capacitor
import mParticle_Apple_SDK
import AppTrackingTransparency

public typealias JSObject = [String: Any]
public typealias JSArray = [JSObject]

@objc(MparticlePlugin)
public class MparticlePlugin: CAPPlugin {
    private let implementation = Mparticle()
    public let LOG_TAG = "bitburst.mparticle.plugin "

    override public func load() {
        // for iOS we need to delay initializing for ATT status response
        // initial load moved to init()
    }

    @objc func `init`(_ call: CAPPluginCall) {
        implementation.start(getConfig())
        call.resolve()
    }

    @objc func identifyUser(_ call: CAPPluginCall) {
        call.keepAlive = true
        let email = call.getString("email", "not set")
        let customerId = call.getString("customerId", "not set")
        let other = call.getString("other", "not set")

        MParticle.sharedInstance().identity.identify(implementation.identityRequest(email, customerId, other)!, completion: {(identityResult: MPIdentityApiResult?, _: Error?) in
            if identityResult?.user.userId != nil {
                call.resolve([
                    "userId": identityResult?.user.userId ?? ""
                ])
            } else {
                print(self.LOG_TAG + "failed to identify user.")
                call.reject("failed to identify user.")
            }
        })
    }

    @objc func setUserAttribute(_ call: CAPPluginCall) {
        let userId = call.getString("userId") ?? ""
        let name = call.getString("name") ?? ""
        let value = call.getString("value") ?? ""
        implementation.setUserAttribute(userId, name, value)
        call.resolve()
    }

    @objc func setUserAttributes(_ call: CAPPluginCall) {
        let userId = call.getString("userId") ?? ""
        let attributes = (call.getArray("attributes") ?? []) as! JSArray
        implementation.setUserAttributes(userId, attributes)
        call.resolve()
    }

    @objc func setGDPRConsent(_ call: CAPPluginCall) {
        let analytics = call.getObject("analytics") ?? [:]
        let advertising = call.getObject("advertising") ?? [:]
        let general = call.getObject("general") ?? [:]
        implementation.addGDPRConsentState(analytics, advertising, general)
        call.resolve()
    }

    @objc func getGDPRConsent(_ call: CAPPluginCall) {
        let consents = implementation.getGDPRConsent()
        call.resolve([
            "consents": consents!
        ])
    }

    @objc func getMPID(_ call: CAPPluginCall) {
        let mpid = implementation.currentUser()!.userId.stringValue
        call.resolve([
            "userId": mpid
        ])
    }

    @objc func trackEvent(_ call: CAPPluginCall) {
        let name = call.getString("name") ?? ""
        let data = call.getObject("data") ?? [:]
        implementation.trackEvent(name, data)
        call.resolve()
    }

    @objc func trackPageView(_ call: CAPPluginCall) {
        let name = call.getString("name") ?? ""
        let data = call.getObject("data") ?? [:]
        implementation.trackPageView(name, data)
        call.resolve()
    }

    @objc func trackPurchase(_ call: CAPPluginCall) {
        let name = call.getString("name", "not set")
        let sku = call.getString("sku", "not set")
        let quantity = call.getInt("quantity", -1)
        let price = call.getFloat("price", -1)
        let transactionId = call.getString("transactionId", "not set")
        let customAttributes = call.getObject("customAttributes")

        implementation.trackPurchaseEvent(name, sku, quantity, price, transactionId, customAttributes)
        call.resolve()
    }
}
