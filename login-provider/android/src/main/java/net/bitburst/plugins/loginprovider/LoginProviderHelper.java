package net.bitburst.plugins.loginprovider;

import android.net.Uri;
import androidx.annotation.Nullable;
import com.getcapacitor.JSObject;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.OAuthCredential;
import java.util.Map;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginProviderHelper {

    public static JSObject convertJSONObject(@Nullable JSONObject jsonObject) {
        if (jsonObject == null) return null;

        JSObject returnJSObject = new JSObject();
        JSONArray keys = jsonObject.names();

        for (int i = 0; i < Objects.requireNonNull(keys).length(); i++) {
            String key = null;
            String value = null;
            try {
                key = keys.getString(i);
                value = jsonObject.getString(key);
                returnJSObject.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return returnJSObject;
    }

    public static JSObject createLoginProviderResponsePayload(
        @Nullable String provider,
        @Nullable String token,
        @Nullable String secret,
        @Nullable String email,
        @Nullable Uri avatarUrl,
        @Nullable String inviteCode
    ) {
        JSObject result = new JSObject();
        result.put("provider", provider);
        result.put("token", token);
        result.put("secret", secret);
        result.put("email", email);
        if (avatarUrl != null) {
            result.put("avatarUrl", avatarUrl.toString());
        }
        result.put("inviteCode", inviteCode);
        return result;
    }

    @Nullable
    public static JSObject createCredentialResult(
        @Nullable AuthCredential credential,
        @Nullable String idToken,
        @Nullable String nonce,
        @Nullable String accessToken
    ) {
        if (credential == null && idToken == null && nonce == null && accessToken == null) {
            return null;
        }
        JSObject result = new JSObject();
        if (credential != null) {
            result.put("providerId", credential.getProvider());
            if (credential instanceof OAuthCredential) {
                String oAuthAccessToken = ((OAuthCredential) credential).getAccessToken();
                if (oAuthAccessToken != null) {
                    result.put("accessToken", oAuthAccessToken);
                }
                String oAuthIdToken = ((OAuthCredential) credential).getIdToken();
                if (oAuthIdToken != null) {
                    result.put("idToken", oAuthIdToken);
                }
                String oAuthSecret = ((OAuthCredential) credential).getSecret();
                if (oAuthSecret != null) {
                    result.put("secret", oAuthSecret);
                }
            }
        }
        if (idToken != null) {
            result.put("idToken", idToken);
        }
        if (nonce != null) {
            result.put("nonce", nonce);
        }
        if (accessToken != null) {
            result.put("accessToken", accessToken);
        }
        return result;
    }

    @Nullable
    public static JSObject createAdditionalUserInfoResult(@Nullable AdditionalUserInfo additionalUserInfo) {
        if (additionalUserInfo == null) {
            return null;
        }
        JSObject result = new JSObject();
        result.put("isNewUser", additionalUserInfo.isNewUser());
        if (additionalUserInfo.getProfile() != null) {
            JSObject profileResult = new JSObject();
            for (Map.Entry<String, Object> entry : additionalUserInfo.getProfile().entrySet()) {
                profileResult.put(entry.getKey(), entry.getValue());
            }
            result.put("profile", profileResult);
        }
        if (additionalUserInfo.getProviderId() != null) {
            result.put("providerId", additionalUserInfo.getProviderId());
        }
        if (additionalUserInfo.getUsername() != null) {
            result.put("username", additionalUserInfo.getUsername());
        }
        return result;
    }
}
