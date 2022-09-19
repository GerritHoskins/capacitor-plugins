import Foundation
import Capacitor

protocol ProviderHandler {
    func initialize(plugin: LoginProviderPlugin, options: JSObject)
    func login(call: CAPPluginCall)
    func logout(call: CAPPluginCall) throws
    func isAuthenticated() -> Bool
    func fillResult(data: PluginCallResultData) -> PluginCallResultData
}
