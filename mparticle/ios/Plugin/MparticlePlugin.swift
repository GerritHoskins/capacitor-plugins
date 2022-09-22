import Foundation
import Capacitor
import mParticle_Apple_SDK

@objc(MparticlePlugin)
public class MparticlePlugin: CAPPlugin {
    private let implementation = Mparticle()

    override public func load() {
        implementation.start(MParticleOptions(key: "", secret: ""))
    }

    @objc func identifyUser(_ call: CAPPluginCall) {
        let email = call.getString("email") ?? ""
        let customerId = call.getString("customerId") ?? ""
        MParticle.sharedInstance().identity.identify(implementation.identityRequest(email, customerId)!, completion: implementation.identityCallback)
    }

    @objc func setUserAttribute(_ call: CAPPluginCall) {
        let name = call.getString("attributeName") ?? ""
        let value = call.getString("attributeValue") ?? ""

        implementation.currentUser()?.setUserAttribute(name, value: value)
        call.resolve()
    }

    @objc func setGDPRConsent(_ call: CAPPluginCall) {
        call.unimplemented()
    }

    @objc func getGDPRConsent(_ call: CAPPluginCall) {
        call.unimplemented()
    }

    @objc func getMPID(_ call: CAPPluginCall) {
        let mpid = implementation.currentUser()!.userId.stringValue
        call.resolve([
            "MPID": mpid
        ])
    }

    @objc func trackEvent(_ call: CAPPluginCall) {
        let name = call.getString("name") ?? ""
        let type =  UInt(call.getInt("eventType") ?? 8) // EventType 8:Other
        let props = call.getObject("data") ?? [:]
        if let event = MPEvent(name: name, type: MPEventType.init(rawValue: type) ?? MPEventType.other) {
            event.customAttributes = props
            MParticle.sharedInstance().logEvent(event)
        }
        call.resolve()
    }

    @objc func trackPageView(_ call: CAPPluginCall) {
        let name = call.getString("name") ?? ""
        let data = call.getAny("data")

        MParticle.sharedInstance().logScreen(name, eventInfo: data as? [String: Any])
        call.resolve()
    }

    @objc func loginUser(_ call: CAPPluginCall) {
        call.resolve()
    }

    @objc func logoutUser(_ call: CAPPluginCall) {
        MParticle.sharedInstance().identity.logout(completion: implementation.identityCallback)
        call.resolve()
    }

    @objc func registerUser(_ call: CAPPluginCall) {
        let email = call.getString("email") ?? ""
        let customerId = call.getString("customerId") ?? ""
        let userAttributes = call.getObject("userAttributes") ?? [:]

        MParticle.sharedInstance().identity.login(implementation.identityRequest(email, customerId)!, completion: { (result: MPIdentityApiResult?, error: Error?) -> Void in
            if result?.user != nil {
                for (key, value) in userAttributes {
                    result?.user.setUserAttribute(key, value: value)
                }
            } else {
                NSLog(error!.localizedDescription)
                let resultCode = MPIdentityErrorResponseCode(rawValue: UInt((error! as NSError).code))
                switch resultCode! {
                case .clientNoConnection,
                     .clientSideTimeout:
                    // retry the IDSync request
                    break
                case .requestInProgress,
                     .retry:
                    break
                default:
                    break
                }
            }
        })
        call.resolve()
    }

    @objc override public func addListener(_ call: CAPPluginCall) {
        call.unimplemented()
    }
}
