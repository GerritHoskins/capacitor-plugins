import Foundation
import Capacitor
import FBSDKLoginKit

class FacebookProvider: NSObject {
       private let loginManager = LoginManager()
       private let dateFormatter = ISO8601DateFormatter()

       var plugin: LoginProviderPlugin
       var options: JSObject

       func initialize(plugin: LoginProviderPlugin, options: JSObject) {
           self.plugin = plugin
           self.options = options

           if #available(iOS 11.2, *) {
                dateFormatter.formatOptions = [.withInternetDateTime, .withFractionalSeconds]
            } else {
                dateFormatter.formatOptions = [.withInternetDateTime]
            }
       }

       private func dateToJS(_ date: Date) -> String {
           return dateFormatter.string(from: date)
       }

       @objc func initialize(_ call: CAPPluginCall) {
           call.resolve()
       }

       @objc func login(_ call: CAPPluginCall) {
           guard let permissions = call.getArray("permissions", String.self) else {
               call.reject("Missing permissions argument")
               return
           }

           let perm = permissions.map { Permission.custom($0) }

           DispatchQueue.main.async {
               self.loginManager.logIn(permissions: perm, viewController: self.bridge?.viewController) { loginResult in
                   switch loginResult {
                   case .failed(let error):
                       print(error)
                       call.reject("LoginManager.logIn failed")

                   case .cancelled:
                       print("User cancelled login")
                       call.resolve()

                   case .success(_, _, _):
                       print("Logged in")
                       return self.getCurrentAccessToken(call)
                   @unknown default:
                       call.reject("LoginManager.logIn failed")
                   }
               }
           }
       }

       @objc func logout(_ call: CAPPluginCall) {
           loginManager.logOut()

           call.resolve()
       }

       @objc func reauthorize(_ call: CAPPluginCall) {
           DispatchQueue.main.async {
               if let token = AccessToken.current, !token.isDataAccessExpired {
                   return self.getCurrentAccessToken(call)
               } else {
                   self.loginManager.reauthorizeDataAccess(from: (self.bridge?.viewController)!) { (loginResult, error) in
                       if (loginResult?.token) != nil {
                           return self.getCurrentAccessToken(call)
                       } else {
                           print(error!)
                           call.reject("LoginManager.reauthorize failed")
                       }
                   }
               }
           }
       }

       private func accessTokenToJson(_ accessToken: AccessToken) -> [String: Any?] {
           return [
               "applicationId": accessToken.appID,
               /*declinedPermissions: accessToken.declinedPermissions,*/
               "expires": dateToJS(accessToken.expirationDate),
               "lastRefresh": dateToJS(accessToken.refreshDate),
               /*permissions: accessToken.grantedPermissions,*/
               "token": accessToken.tokenString,
               "userId": accessToken.userID
           ]
       }

       @objc func getCurrentAccessToken(_ call: CAPPluginCall) {
           guard let accessToken = AccessToken.current else {
               call.resolve()
               return
           }

           let email = self.getProfile(call).getString("email") as? String ?? "no email"
           let profilePictureUrl = self.getProfile(call).getString("url") as? String ?? "no profile picture"

           let result = LoginProviderHelper.createLoginProviderResponsePayload(
                "FACEBOOK",
                accessToken.tokenString,
                null,
                email,
                profilePictureUrl,
                call.getData().getString("inviteCode")
            )
           call.resolve([result])
       }

       @objc func getProfile(_ call: CAPPluginCall) -> [String: Any]? {
           guard let accessToken = AccessToken.current else {
               call.reject("You're not logged in. Call FacebookLogin.login() first to obtain an access token.")
               return
           }

           if accessToken.isExpired {
               call.reject("AccessToken is expired.")
               return
           }

           guard let fields = call.getArray("fields", String.self) else {
               call.reject("Missing fields argument")
               return
           }
           let parameters = ["fields": fields.joined(separator: ",")]
           let graphRequest = GraphRequest.init(graphPath: "me", parameters: parameters)

           graphRequest.start { (_ connection, _ result, _ error) in
               if error != nil {
                   call.reject("An error has been occured.")
                   return
               }

               //call.resolve(result as! [String: Any])
               return result
           }
       }
}
