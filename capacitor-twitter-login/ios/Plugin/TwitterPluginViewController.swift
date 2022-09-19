import Foundation
import Capacitor
import UIKit
import TwitterLoginKit

class TwitterPluginViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func loginTapped(_ sender: Any) {
        TwitterLoginKit.shared.login(withViewController: self) { (state) in
            switch state {
            case .failure(let error):
                print(error.localizedDescription)
            case .success(let session):
                print(session)
            }
        }
    }
}
