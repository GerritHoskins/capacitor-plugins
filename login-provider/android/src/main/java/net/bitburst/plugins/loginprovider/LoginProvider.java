package net.bitburst.plugins.loginprovider;

import android.content.Intent;
import com.getcapacitor.BridgeActivity;

public class LoginProvider extends BridgeActivity {

    public LoginProvider() {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
