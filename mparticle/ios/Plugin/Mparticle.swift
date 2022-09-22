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

        options.identifyRequest = self.identityRequest("", "")!
        options.onIdentifyComplete = identityCallback

        if #available(iOS 14, *) {
            options.attStatus = NSNumber.init(value: ATTrackingManager.trackingAuthorizationStatus.rawValue)
            ATTrackingManager.requestTrackingAuthorization(completionHandler: { [self] status in
                switch status {
                case .authorized:
                    MParticle.sharedInstance().setATTStatus(MPATTAuthorizationStatus(rawValue: 3)!, withATTStatusTimestampMillis: nil)
                    print(ASIdentifierManager.shared().advertisingIdentifier.uuidString)
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

    @objc public func identifyUser() -> MParticleUser? {
        return MParticle.sharedInstance().identity.currentUser
    }

    @objc public func identityRequest(_ email: String, _ customerId: String) -> MPIdentityApiRequest? {
        self.identityRequest = MPIdentityApiRequest.withEmptyUser()
        self.identityRequest?.email = email
        self.identityRequest!.customerId = customerId
        return self.identityRequest
    }

    var identityCallback = {(result: MPIdentityApiResult?, error: Error?) in
        if result?.user != nil {
            // IDSync request succeeded, get MPID here
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
    }
}
