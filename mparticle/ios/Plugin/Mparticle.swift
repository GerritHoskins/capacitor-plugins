import Foundation
import mParticle_Apple_SDK

import AppTrackingTransparency
import AdSupport
import Capacitor


@objc public class Mparticle: NSObject {
    var identityRequest: MPIdentityApiRequest?
    /*
    if let appDelegate = UIApplication.shared.delegate as? AppDelegate {
        appDelegate.test()
    }*/

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

    @objc public func identityRequest(_ data: JSObject) -> MPIdentityApiRequest? {
        self.identityRequest = MPIdentityApiRequest.withEmptyUser()
        for (key,value) in data {
            self.identityRequest!.setIdentity(value as? String, identityType: self.convertIdentityType(key))
        }

        return self.identityRequest
    }

    public func setUserAttribute(_ userId: String, _ name: String, _ value: JSObject) -> Bool {
        guard let mpid = Int64(userId) else { return false }
        let user = MParticle.sharedInstance().identity.getUser(NSNumber(value:mpid))
        user?.setUserAttribute(name, value: value)
        return true
    }

    public func setUserAttributes(_ userId: String, _ data: JSArray) -> Bool {
       guard let mpid = Int64(userId) else { return false }
       let user = MParticle.sharedInstance().identity.getUser(NSNumber(value:mpid))
       data.forEach { (attribute) in
           user?.setUserAttribute(attribute["name"] as! String, value: attribute["value"] as Any)
       }
       return true
    }

    @objc public func trackEvent() -> MPEvent? {
        if let event = MPEvent(name: "Video Watched", type: MPEventType.navigation) {
            event.customAttributes = ["category": "Destination Intro", "title": "Paris"]
            MParticle.sharedInstance().logEvent(event)
        }
    }
    /*
       if let callArguments = call.arguments as? [String: Any],
                  let eventName = callArguments["eventName"] as? String,
                  let rawEventType = callArguments["eventType"] as? NSNumber,
                  let eventType = MPEventType(rawValue:UInt(truncating: rawEventType)),
                  let event = MPEvent(name: eventName, type: eventType) {
                   let customAttributes = callArguments["customAttributes"] as? [String: Any]
                   event.customAttributes = customAttributes
                   if let customFlags = callArguments["customFlags"] as? [String: String] {
                       for (key, value) in customFlags {
                           event.addCustomFlag(value, withKey: key)
                       }
                   }
                   if let shouldUploadEvent = callArguments["shouldUploadEvent"] as? Bool {
                       event.shouldUploadEvent = shouldUploadEvent
                   }
                   MParticle.sharedInstance().logEvent(event)
               } else {
                   print("Incorrect argument for \(call.method) iOS method")
               }

    }*/

    @objc public func trackPageView() -> MPEvent? {
    /*
          if let callArguments = call.arguments as? [String: Any],
                   let eventName = callArguments["eventName"] as? String,
                   let type = MPEventType(rawValue:1),
                   let event = MPEvent(name: eventName, type: type) {
                    let customAttributes = callArguments["customAttributes"] as? [String: Any];
                    event.customAttributes = customAttributes
                    if let customFlags = callArguments["customFlags"] as? [String: String] {
                        for (key, value) in customFlags {
                            event.addCustomFlag(value, withKey: key)
                        }
                    }

                    MParticle.sharedInstance().logScreenEvent(event)
                } else {
                    print("Incorrect argument for \(call.method) iOS method")
                }
    */
    }

    public func addGDPRConsentState(_ consents: JSObject) {
      let user = self.currentUser()
      for key in consents.keys {
           let consentState = user.consentState() {
                consentState.addGDPRConsentState(consents[key], purpose: key)
            }
      }
      /*if let callArguments = call.arguments as? [String: Any],
                 let consented = callArguments["consented"] as? Bool,
                 let purpose = callArguments["purpose"] as? String,
                 let mpidString = callArguments["mpid"] as? String,
                 let mpid = Int64(mpidString),
                 let user = MParticle.sharedInstance().identity.getUser(NSNumber(value:mpid)) {
                     let consentGDPR = MPGDPRConsent()
                     consentGDPR.consented = consented
                     consentGDPR.document = callArguments["document"] as? String
                     if let timestampNumber = callArguments["timestamp"] as? NSNumber {
                         consentGDPR.timestamp = Date(timeIntervalSince1970: timestampNumber.doubleValue/1000)
                     }
                     consentGDPR.location = callArguments["location"] as? String
                     consentGDPR.hardwareId = callArguments["hardwareId"] as? String

                     let newConsentState = MPConsentState()
                     if let existingConsentState = user.consentState() {
                         if let priorCCPA = existingConsentState.ccpaConsentState() {
                             newConsentState.setCCPA(priorCCPA)
                         }
                         newConsentState.setGDPR(existingConsentState.gdprConsentState())
                     }

                     newConsentState.addGDPRConsentState(consentGDPR, purpose: purpose)
                     user.setConsentState(newConsentState)
              } else {
                  print("Incorrect argument for \(call.method) iOS method")
              }*/
    }

    @objc public func getGDPRConsent() {
        if let callArguments = call.arguments as? [String: Any],
                  let mpidString = callArguments["mpid"] as? String,
                  let mpid = Int(mpidString),
                  let user = MParticle.sharedInstance().identity.getUser(NSNumber(value:mpid)) {
                   var gdprConsentDictionary = [String: Any]()
                   if let gdprConsent = user.consentState()?.gdprConsentState() {
                       for purpose in gdprConsent.keys {
                           let consentObject = gdprConsent[purpose]
                           var consentDictionary = [String: Any]()
                           consentDictionary["consented"] = consentObject?.consented
                           consentDictionary["document"] = consentObject?.document
                           consentDictionary["timestamp"] = consentObject?.timestamp.timeIntervalSince1970
                           consentDictionary["location"] = consentObject?.location
                           consentDictionary["hardwareId"] = consentObject?.hardwareId
                           gdprConsentDictionary[purpose] = consentDictionary
                       }
                   }
                   result(asStringForStringKey(jsonDictionary: gdprConsentDictionary))
               } else {
                   result("")
               }
    }

    private func convertIdentityType(_ val: String) -> MPIdentity {
        if (val == "customerId") {
            return MPIdentity.customerId;
        } else if (val == "email") {
            return MPIdentity.email;
        } else {
            return MPIdentity.other;
        }
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
