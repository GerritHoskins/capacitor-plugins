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

    @objc func mParticleInit(_ call: CAPPluginCall) {
        call.unimplemented("Moved compatability")
        // let key = call.getString("key") ?? ""
        // let secret = call.getString("secret") ?? ""
        // let options = MParticleOptions(key: key,
        //                             secret: secret)
        // options.logLevel = MPILogLevel.verbose
        // options.proxyAppDelegate = false
        // MParticle.sharedInstance().start(with: options)
        // call.resolve([
        //     "value": "ios mparticle initialized"
        // ])
    }

    @objc func loginMparticleUser(_ call: CAPPluginCall) {
        let email = call.getString("email") ?? ""
        let customerId = call.getString("customerId") ?? ""
        MParticle.sharedInstance().identity.login(implementation.identityRequest(email,customerId)!, completion: implementation.identityCallback)
        call.resolve([
            "value":"success",
        ])
    }

    @objc func logoutMparticleUser(_ call: CAPPluginCall) {
        // call.unimplemented("Not implemented on iOS.")
        MParticle.sharedInstance().identity.logout(completion: implementation.identityCallback)
        call.resolve([
            "value":"success",
        ])
    }

    @objc func registerMparticleUser(_ call: CAPPluginCall) {
        let email = call.getString("email") ?? ""
        let customerId = call.getString("customerId") ?? ""
        let userAttributes = call.getObject("userAttributes") ?? [:]

        MParticle.sharedInstance().identity.login(implementation.identityRequest(email,customerId)!, completion: { (result: MPIdentityApiResult?, error: Error?) -> () in
            if (result?.user != nil) {
                for (key,value) in userAttributes {
                    result?.user.setUserAttribute(key, value: value)
                }
            } else {
                NSLog(error!.localizedDescription)
                let resultCode = MPIdentityErrorResponseCode(rawValue: UInt((error! as NSError).code))
                switch (resultCode!) {
                case .clientNoConnection,
                    .clientSideTimeout:
                    //retry the IDSync request
                    break;
                case .requestInProgress,
                    .retry:
                    //inspect your implementation if this occurs frequency
                    //otherwise retry the IDSync request
                    break;
                default:
                    // inspect error.localizedDescription to determine why the request failed
                    // this typically means an implementation issue
                    break;
                }
            }
        })
        call.resolve([
            "value":"success",
        ])
    }

    @objc func logMparticleEvent(_ call: CAPPluginCall) {
        let name = call.getString("eventName") ?? ""
        let type =  UInt(call.getInt("eventType") ?? 0)
        let props = call.getObject("eventProperties") ?? [:]
        if let event = MPEvent(name: name, type: MPEventType.init(rawValue:type) ?? MPEventType.other) {
            event.customAttributes = props
            MParticle.sharedInstance().logEvent(event)
        }
        call.resolve([
            "value":"success",
        ])
    }

    @objc func logMparticlePageView(_ call: CAPPluginCall) {
        let name = call.getString("pageName") ?? ""
        let screenInfo = ["page": call.getString("pageLink") ?? ""];

        MParticle.sharedInstance().logScreen(name, eventInfo: screenInfo)
        call.resolve([
            "value":"success",
        ])
    }

    @objc func setUserAttribute(_ call: CAPPluginCall) {
        let name = call.getString("attributeName") ?? ""
        let value = call.getString("attributeValue") ?? ""

        implementation.currentUser()?.setUserAttribute(name, value: value)
        call.resolve([
            "value":"success",
        ])
    }

    @objc func setUserAttributeList(_ call: CAPPluginCall) {
        let name:String = call.getString("attributeName") ?? ""
        let list:[String] = call.getArray("attributeValues") as? [String] ?? []
        implementation.currentUser()?.setUserAttributeList(name, values: list)
        call.resolve([
            "value":"success",
        ])
    }

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.resolve([
            "value": implementation.echo(value)
        ])
    }
}
