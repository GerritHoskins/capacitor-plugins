import Foundation

@objc public class Clevertap: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
