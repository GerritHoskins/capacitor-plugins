import Foundation
import Capacitor
import mParticle_Apple_SDK

public typealias JSObject = [String: Any]
public typealias JSArray = [JSObject]

@objc(MparticlePlugin)
public class MparticlePlugin: CAPPlugin {
    private let implementation = Mparticle()
    public let LOG_TAG = "bitburst.mparticle.plugin "

    override public func load() {
        let key = getConfig().getString("key") ?? ""
        let secret = getConfig().getString("secret") ?? ""
        implementation.start(MParticleOptions(key: key, secret: secret))
    }

    @objc func identifyUser(_ call: CAPPluginCall) {
        let data: JSObject = call.getObject("identifier") ?? [:]
        MParticle.sharedInstance().identity.identify(implementation.identityRequest(data)!, completion: {(identityResult: MPIdentityApiResult?, _: Error?) in

            call.resolve([
                "userId": identityResult?.user.userId
            ])
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
        let consents: JSObject = call.getObject("consents") ?? [:]
        let consented = call.getBool("consented") ?? false
        implementation.addGDPRConsentState(consented, consents)
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
        implementation.trackEvent(name, MPEventType.other, data)
        call.resolve()
    }

    @objc func trackPageView(_ call: CAPPluginCall) {
        let name = call.getString("name") ?? ""
        let data = call.getObject("data") ?? [:]
        implementation.trackPageView(name, MPEventType.other, data)
        call.resolve()
    }
}
