import Foundation
import Capacitor

public protocol ProviderResponsePayload {
    var provider: String? { get }
    var token: String? { get }
    var secret: String? { get }
    var email: String? { get }
    var avatarUrl: String? { get }
    var inviteCode: String? { get }
}

public class LoginProviderHelper {
    public static func createLoginProviderResponsePayload(provider: String?, token: String?, secret: String?, email: String?, avatarUrl: String?, inviteCode: String?) -> PluginCallResultData {
        let result: PluginCallResultData = [
            "provider": provider ?? "",
            "token": token ?? "",
            "secret": secret ?? "",
            "email": email ?? "",
            "avatarUrl": avatarUrl ?? "",
            "inviteCode": inviteCode ?? ""
        ]

        return result
    }
}
