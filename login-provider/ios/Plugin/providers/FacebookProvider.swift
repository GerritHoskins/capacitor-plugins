import Foundation
import Capacitor
import FBSDKLoginKit
import FBSDKCoreKit

class FacebookProvider: NSObject, ProviderHandler {

    private let loginManager = LoginManager()
    private let dateFormatter = ISO8601DateFormatter()

    var plugin: LoginProviderPlugin?
    var options: JSObject = [:]

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
        return AccessToken.current != nil
    }

    func login(call: CAPPluginCall) {
        guard let permissions = call.getArray("permissions", String.self) else {
            call.reject("missing permissions argument")
            return
        }

        let perm = permissions.map { Permission.custom($0) }

        DispatchQueue.main.async {
            self.loginManager.logIn(permissions: ["public_profile", "email"], from: self.plugin!.bridge?.viewController) {
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

                call.resolve(LoginProviderHelper.createLoginProviderResponsePayload(
                    provider: "FACEBOOK",
                    token: loginResult?.token?.tokenString,
                    secret: "",
                    email: self.getUserEmailAndPicture()["email"] as? String,
                    // avatarUrl: self.getUserEmailAndPicture()["picture.type(small)"] as? String,
                    avatarUrl: Profile.current?.imageURL(forMode: Profile.PictureMode.small, size: CGSize(width: 200, height: 200))?.absoluteString,
                    inviteCode: call.getString("inviteCode")
                ))
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

    func getUserEmailAndPicture() -> [String: Any] {
        var userProfile: [String: Any] = [:]

        guard let accessToken = AccessToken.current else { return userProfile }
        if accessToken.isExpired { return userProfile }

        let request = GraphRequest(graphPath: "me", parameters: ["fields": "email, picture.type(small)"])
        request.start { (_ connection, result, error) in
            if let result = result, error == nil {
                NSLog("fetched user: \(result)")
                if let dic = result as? [String: Any] {
                    // let picture = dic["picture.type(small)"] as? String,
                    // let email = dic["email"] as? String {
                    userProfile = dic
                }
            }
        }
        return userProfile
    }

    func getProfile(call: CAPPluginCall) {
        guard let accessToken = AccessToken.current else {
            call.reject("login first to obtain an access token.")
            return
        }

        if accessToken.isExpired {
            call.reject("accessToken is expired.")
            return
        }

        let graphRequest = GraphRequest.init(graphPath: "me", parameters: ["fields": "email, picture.type(large)"])

        graphRequest.start { (_ connection, _ result, _ error) in
            if error != nil {
                call.reject("An error has occured.")
                return
            }

            call.resolve(result as! [String: Any])
        }
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
