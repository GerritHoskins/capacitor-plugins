package net.bitburst.plugins.loginprovider;

public class LoginProviderConfig {

    private boolean skipNativeAuth = false;
    private String[] providers = new String[] {};

    public boolean getSkipNativeAuth() {
        return skipNativeAuth;
    }

    public void setSkipNativeAuth(boolean skipNativeAuth) {
        this.skipNativeAuth = skipNativeAuth;
    }

    public String[] getProviders() {
        return providers;
    }

    public void setProviders(String[] providers) {
        this.providers = providers;
    }
}
