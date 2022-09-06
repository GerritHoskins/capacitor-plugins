package net.bitburst.plugins.loginprovider;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

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

    public static JSArray convertJSONArray(@Nullable JSONArray jsonArray) {
        if (jsonArray == null) return null;

        JSArray returnJSArray = new JSArray();
        JSONArray sourceArray = jsonArray;

        for (int i = 0; i < sourceArray.length(); i++) {
            try {
                returnJSArray.put(i, sourceArray.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return returnJSArray;
    }

    public static JSArray convertStringArray(@Nullable String[] stringArray) {
        if (stringArray == null) return null;

        JSArray returnJSArray = new JSArray();
        String[] sourceArray = stringArray;

        for (int i = 0; i < sourceArray.length; i++) {
            try {
                returnJSArray.put(i, sourceArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return returnJSArray;
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
}
