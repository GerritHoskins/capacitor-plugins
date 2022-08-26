package net.bitburst.plugins.loginprovider;

public class LoginProviderUserModel {

    private String provider;
    private String displayName;
    private String email;
    private Uri photoUrl;
    private String providerId;
    private String tenantId;
    private String uid;
    private String secret;

    public LoginProviderUserModel(
        String provider, String displayName, String email,
        Uri photoUrl, String providerId, String tenantId,
        String uid, String secret
    ) {
        this.provider = provider;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.providerId = providerId;
        this.tenantId = tenantId;
        this.uid = uid;
        this.secret = secret;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

}