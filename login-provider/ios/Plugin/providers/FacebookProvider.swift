import Foundation
import Capacitor
import FBSDKLoginKit
import FBSDKCoreKit

class FacebookProvider: NSObject, ProviderHandler {

    private let loginManager = LoginManager()
    private let dateFormatter = ISO8601DateFormatter()

    var plugin: LoginProviderPlugin?
    var options: JSObject = [:]
    var additionalScopes: [String]!

    func initialize(plugin: LoginProviderPlugin, options: JSObject) {
        self.plugin = plugin
        self.options = options

        if #available(iOS 11.2, *) {
            dateFormatter.formatOptions = [.withInternetDateTime, .withFractionalSeconds]
        } else {
            dateFormatter.formatOptions = [.withInternetDateTime]
        }
    }

    func isAuthenticated() -> Bool {
        return false
    }

    func login(call: CAPPluginCall) {

        guard let permissions = self.options["permissions"] as? String else {
            call.reject("missing permissions argument")
            return
        }

        let defaultGrantedScopes = ["public_profile", "email"]
        additionalScopes = (permissions.components(separatedBy: ", ")).filter {
            return !defaultGrantedScopes.contains($0)
        }

        DispatchQueue.main.async {
            self.loginManager.logIn(permissions: defaultGrantedScopes, from: self.plugin!.bridge?.viewController) {
                ( loginResult: LoginManagerLoginResult?, error: Error?) in

                if let error = error {
                    NSLog(error.localizedDescription)
                    return
                }

                guard let token: AccessToken = loginResult?.token else {
                    call.reject("no valid facebook token found")
                    return
                }

                if token.isExpired {
                    call.reject("token expired")
                    return
                }

                self.getProfile()

                let loginPayload = LoginProviderHelper.createLoginProviderResponsePayload(
                    provider: "FACEBOOK",
                    token: loginResult?.token?.tokenString,
                    secret: "",
                    email: Profile.current?.email,
                    avatarUrl: Profile.current?.imageURL(forMode: Profile.PictureMode.small, size: CGSize(width: 200, height: 200))?.absoluteString,
                    inviteCode: call.getString("inviteCode")
                )

                call.resolve(loginPayload)
            }
        }
    }

    func logout(call: CAPPluginCall) {
        loginManager.logOut()
        call.resolve()
    }

    func reauthorize(call: CAPPluginCall) {
        DispatchQueue.main.async {
            if let token = AccessToken.current, !token.isDataAccessExpired {
                return self.getCurrentAccessToken(call: call)
            } else {
                self.loginManager.reauthorizeDataAccess(from: (self.plugin?.bridge?.viewController)!) { (loginResult, error) in
                    if (loginResult?.token) != nil {
                        return self.getCurrentAccessToken(call: call)
                    } else {
                        NSLog(error?.localizedDescription ?? "reauthorization error")
                        call.reject("LoginManager.reauthorize failed")
                    }
                }
            }
        }
    }

    func getCurrentAccessToken(call: CAPPluginCall) {
        guard AccessToken.current != nil else {
            call.resolve()
            return
        }
    }

    func getProfile() {
        guard let accessToken = AccessToken.current else {
            NSLog("accessToken is expired or invalid.")
            return
        }

        if accessToken.isExpired {
            NSLog("accessToken is expired or invalid.")
            return
        }

        let graphRequest = GraphRequest.init(graphPath: "me", parameters: ["fields": "email, picture.type(large)"])
        graphRequest.start { (_ connection, _ result, _ error) in
            if error != nil {
                NSLog("failed to fetch profile info.")
                return
            }

            // let response = result as! [String: Any]
        }
    }

    func fillResult(data: PluginCallResultData) -> PluginCallResultData {
        guard let accessToken = AccessToken.current else {
            return data
        }

        var jsResult: PluginCallResultData = [:]
        data.forEach { (key, value) in
            jsResult[key] = value
        }

        jsResult["idToken"] = accessToken.tokenString

        return jsResult
    }

    private func accessTokenToJson(_ accessToken: AccessToken) -> [String: Any?] {
        return [
            "applicationId": accessToken.appID,
            "expires": dateToJS(accessToken.expirationDate),
            "lastRefresh": dateToJS(accessToken.refreshDate),
            "token": accessToken.tokenString,
            "userId": accessToken.userID
        ]
    }

    private func dateToJS(_ date: Date) -> String {
        return dateFormatter.string(from: date)
    }
}
