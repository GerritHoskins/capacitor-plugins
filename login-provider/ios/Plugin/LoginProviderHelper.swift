import Foundation
import Capacitor

public class LoginProviderHelper {
    public static func createLoginProviderResponsePayload(provider: String?, token: String?, secret: String?, email: String?, avatarUrl: String?, inviteCode: String?) -> PluginCallResultData {
        let result: PluginCallResultData = [
            "provider": provider!,
            "token": token!,
            "secret": secret!,
            "email": email!,
            "avatarUrl": avatarUrl!,
            "inviteCode": inviteCode!
        ]

        return result
    }
}
