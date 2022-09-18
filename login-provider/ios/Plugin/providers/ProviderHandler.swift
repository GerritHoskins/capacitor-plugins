import Foundation
import Capacitor

protocol ProviderHandler {
    func initialize(plugin: CapacitorFirebaseAuth)
    func login(call: CAPPluginCall)
    func logout() throws
    func isAuthenticated() -> Bool
    // func fillResult(credential: AuthCredential?, data: PluginResultData) -> PluginResultData
}
