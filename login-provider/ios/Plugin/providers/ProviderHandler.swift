import Foundation
import Capacitor

protocol ProviderHandler {
    func initialize(plugin: CapacitorFirebaseAuth) -> Void
    func login(call: CAPPluginCall) -> Void
    func logout() throws -> Void
    func isAuthenticated() -> Bool
    //func fillResult(credential: AuthCredential?, data: PluginResultData) -> PluginResultData
}
