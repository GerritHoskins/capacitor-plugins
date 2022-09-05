package net.bitburst.plugins.loginprovider;

import android.content.Intent;

import com.getcapacitor.BridgeActivity;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;


public class LoginProvider extends BridgeActivity {
    public LoginProviderPlugin mProvider;

    public LoginProvider() {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTwitterActivityResult(requestCode, resultCode, data);
    }

    public void mTwitterActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            TwitterAuthClient mAuthClient = new TwitterAuthClient();
            if(mAuthClient != null) {
                mAuthClient.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}