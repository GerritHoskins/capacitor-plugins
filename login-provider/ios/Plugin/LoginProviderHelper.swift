import Foundation
import Capacitor

public class LoginProviderHelper {
    public static func createLoginProviderResponsePayload(provider: String?, token: String?, secret: String?, email: String?, avatarUrl: String?, inviteCode: String?) -> JSObject? {
    /* let result = [
           "provider": provider,
           "token": token,
           "secret":secret,
           "email": email,
           "avatarUrl": avatarUrl,
           "inviteCode": inviteCode
       ]*/

        var result = JSObject()
        result["provider"] = provider
        result["token"] = token
        result["secret"] = secret
        result["email"] = email
        result["avatarUrl"] = avatarUrl
        result["inviteCode"] = inviteCode

        return result
    }
}
