import Foundation
import mParticle_Apple_SDK

import AppTrackingTransparency
import AdSupport
import Capacitor

@objc public class Mparticle: NSObject {
    var identityRequest: MPIdentityApiRequest?

    @objc public func start(_ options: MParticleOptions) {
        options.environment = MPEnvironment.autoDetect
        options.dataPlanId = "bitcode_frontend_plan"
        options.dataPlanVersion = 4

        options.identifyRequest = self.identityRequest([:])!
        options.onIdentifyComplete = identityCallback

        if #available(iOS 14, *) {
            options.attStatus = NSNumber.init(value: ATTrackingManager.trackingAuthorizationStatus.rawValue)
            ATTrackingManager.requestTrackingAuthorization(completionHandler: { [self] status in
                switch status {
                case .authorized:
                    MParticle.sharedInstance().setATTStatus(MPATTAuthorizationStatus(rawValue: 3)!, withATTStatusTimestampMillis: nil)
                    self.identityRequest!.setIdentity(ASIdentifierManager.shared().advertisingIdentifier.uuidString, identityType: MPIdentity.iosAdvertiserId)
                    MParticle.sharedInstance().identity.modify(identityRequest!, completion: self.identityCallback)
                case .denied:
                    MParticle.sharedInstance().setATTStatus(MPATTAuthorizationStatus(rawValue: 2)!, withATTStatusTimestampMillis: nil)
                case .notDetermined:
                    MParticle.sharedInstance().setATTStatus(MPATTAuthorizationStatus(rawValue: 0)!, withATTStatusTimestampMillis: nil)
                case .restricted:
                    MParticle.sharedInstance().setATTStatus(MPATTAuthorizationStatus(rawValue: 1)!, withATTStatusTimestampMillis: nil)
                @unknown default:
                    MParticle.sharedInstance().setATTStatus(MPATTAuthorizationStatus(rawValue: 0)!, withATTStatusTimestampMillis: nil)
                }
            })
        }
        MParticle.sharedInstance().start(with: options)
    }

    @objc public func currentUser() -> MParticleUser? {
        return MParticle.sharedInstance().identity.currentUser
    }

    @objc public func identityRequest(_ data: JSObject) -> MPIdentityApiRequest? {
        self.identityRequest = MPIdentityApiRequest.withEmptyUser()
        for (key, value) in data {
            self.identityRequest!.setIdentity(value as? String, identityType: self.convertIdentityType(key))
        }

        return self.identityRequest
    }

    public func setUserAttribute(_ userId: String, _ name: String, _ value: String) {
        let mpid = Int64(userId) ?? self.currentUser()?.userId as! Int64
        let user = MParticle.sharedInstance().identity.getUser(NSNumber(value: mpid))
        user?.setUserAttribute(name, value: value)
    }

    public func setUserAttributes(_ userId: String, _ data: JSArray) {
        let mpid = Int64(userId) ?? self.currentUser()?.userId as! Int64
        let user = MParticle.sharedInstance().identity.getUser(NSNumber(value: mpid))
        data.forEach { (attribute) in
            user?.setUserAttribute(attribute["name"] as! String, value: attribute["value"] as Any)
        }
    }

    public func addGDPRConsentState(_ consented: Bool, _ consents: JSObject) {
        let user = self.currentUser()
        let consentGDPR = MPGDPRConsent()
        consentGDPR.consented = consented
        for (key, _) in consents {
            user?.consentState()?.addGDPRConsentState(consents[key] as! MPGDPRConsent, purpose: key)

        }
    }

    @objc public func getGDPRConsent() -> [String: MPGDPRConsent]? {
        return self.currentUser()?.consentState()?.gdprConsentState()
    }

    @objc public func trackEvent(_ name: String, _ data: JSObject) {
        if let mpEvent = MPEvent(name: name, type: MPEventType.other ) {
            mpEvent.customAttributes = data
            MParticle.sharedInstance().logEvent(mpEvent)
        }
    }

    @objc public func trackPageView(_ name: String, _ data: JSObject) {
        if MPEvent(name: name, type: MPEventType.other ) != nil {
            MParticle.sharedInstance().logScreen(name, eventInfo: data)
        }
    }

    @objc public func trackPurchaseEvent(_ data: AnyObject) {
        let productData = data as! [String: Any]
        let product = MPProduct.init(
            name: productData["name"] as! String,
            sku: "\(productData["sku"] ?? 0)",
            quantity: productData["quantity"] as! NSNumber,
            price: (productData["price"] as? NSNumber) ?? nil
        )

        if let productAttributes = productData["attributes"] as? [String: JSValue] {
            for productAttribute in productAttributes {
                product[productAttribute.key] = "\(productAttribute.value)"
            }
        }

        let transactionAttributes = MPTransactionAttributes.init()
        transactionAttributes.transactionId = productData["transactionId"] as? String
        transactionAttributes.revenue = (product.totalAmount) as NSNumber

        let event = MPCommerceEvent.init(action: MPCommerceEventAction.purchase, product: product)
        event.customAttributes = productData["attributes"] as? [String: Any]
        event.transactionAttributes = transactionAttributes
        MParticle.sharedInstance().logEvent(event)
    }

    private func convertIdentityType(_ val: String) -> MPIdentity {
        if val == "customerId" {
            return MPIdentity.customerId
        } else if val == "email" {
            return MPIdentity.email
        } else {
            return MPIdentity.other
        }
    }

    var identityCallback = {(result: MPIdentityApiResult?, error: Error?) in
        if result?.user != nil {
            // IDSync request succeeded, identityCallback returns MPID here
        } else {
            NSLog(error!.localizedDescription)
            let resultCode = MPIdentityErrorResponseCode(rawValue: UInt((error! as NSError).code))
            switch resultCode! {
            case .clientNoConnection,
                 .clientSideTimeout:
                // retry the identityCallback and return MPID
                break
            case .requestInProgress,
                 .retry:
                break
            default:
                break
            }
        }
    }
}
