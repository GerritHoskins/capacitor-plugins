import Foundation
import Capacitor
import mParticle_Apple_SDK

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(MparticlePlugin)
public class MparticlePlugin: CAPPlugin {
    private let implementation = Mparticle()

    override public func load() {
        implementation.start(MParticleOptions(key: "eu1-f4d0f1212b62a64589e30e1d9d852e17", secret: "kl_hrnSPIlQeMobS3ZV_BlywiiNnNJLpxokrdSfv3siwQ0yctPIdtYHr9MNEXEq7"))
    }

    @objc func identifyUser(_ call: CAPPluginCall) {
        let email = call.getString("email") ?? ""
        let customerId = call.getString("customerId") ?? ""
        MParticle.sharedInstance().identity.login(implementation.identityRequest(email, customerId)!, completion: implementation.identityCallback)
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

    @objc func logEvent(_ call: CAPPluginCall) {
        let name = call.getString("eventName") ?? ""
        let type =  UInt(call.getInt("eventType") ?? 0)
        let props = call.getObject("eventProperties") ?? [:]
        if let event = MPEvent(name: name, type: MPEventType.init(rawValue: type) ?? MPEventType.other) {
            event.customAttributes = props
            MParticle.sharedInstance().logEvent(event)
        }
        call.resolve()
    }

    @objc func logPageView(_ call: CAPPluginCall) {
        let name = call.getString("pageName") ?? ""
        let screenInfo = ["page": call.getString("pageLink") ?? ""]

        MParticle.sharedInstance().logScreen(name, eventInfo: screenInfo)
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
                    // this typically means an implementation issue
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
