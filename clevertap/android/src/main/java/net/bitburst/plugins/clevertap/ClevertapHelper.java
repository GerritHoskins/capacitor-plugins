package net.bitburst.plugins.clevertap;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClevertapHelper {

    public static HashMap<String, Object> convertJSObjectToHashMap(@Nullable JSObject jsObject) {
        if (jsObject == null) return null;

        HashMap<String, Object> returnHashMap = new HashMap<String, Object>();
        JSONArray keys = jsObject.names();

        for (int i = 0; i < Objects.requireNonNull(keys).length(); i++) {
            String key = null;
            Object value = null;
            try {
                key = keys.getString(i);
                value = jsObject.getString(key);
                returnHashMap.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return returnHashMap;
    }

    public static Bundle convertJSONToBundle(@Nullable JSONObject json) {
        Bundle bundle = new Bundle();
        if (json == null || json.length() == 0) return bundle;

        Iterator<String> iterator = json.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            try {
                Object value = json.get(key);
                if (value == null); else if (value instanceof String) bundle.putString(key, (String) value); else if (
                    value instanceof Boolean
                ) bundle.putBoolean(key, (Boolean) value); else if (value instanceof Integer) bundle.putInt(key, (Integer) value); else if (
                    value instanceof Long
                ) bundle.putLong(key, (Long) value); else if (value instanceof Float) bundle.putFloat(key, (Float) value); else if (
                    value instanceof Double
                ) bundle.putDouble(key, (Double) value); else if (value instanceof JSONObject) bundle.putBundle(
                    key,
                    ClevertapHelper.convertJSONToBundle((JSONObject) value)
                ); else if (value instanceof JSONArray) {
                    JSONArray array = (JSONArray) value;
                    Object first = array.length() == 0 ? null : (Object) array.get(0);
                    if (first == null); else if (first instanceof JSONObject) {
                        Bundle[] items = new Bundle[array.length()];
                        for (int i = 0; i < array.length(); i++) items[i] = ClevertapHelper.convertJSONToBundle(array.getJSONObject(i));
                        bundle.putParcelableArray(key, items);
                    } else if (first instanceof String) {
                        String[] items = new String[array.length()];
                        for (int i = 0; i < array.length(); i++) items[i] = array.getString(i);
                        bundle.putStringArray(key, items);
                    } else if (first instanceof Integer || first instanceof Float || first instanceof Double) {
                        float[] items = new float[array.length()];
                        for (int i = 0; i < array.length(); i++) {
                            items[i] = ((Number) array.get(i)).floatValue();
                        }
                        bundle.putFloatArray(key, items);
                    }
                }
            } catch (ClassCastException | JSONException e) {
                e.printStackTrace();
            }
        }

        return bundle;
    }

    public static Bundle convertJSObjectToBundle(@Nullable JSObject jsObject) {
        if (jsObject == null) return null;

        Bundle returnBundle = new Bundle();
        JSONArray keys = jsObject.names();

        for (int i = 0; i < Objects.requireNonNull(keys).length(); i++) {
            String key = null;
            String value = null;
            try {
                key = keys.getString(i);
                value = jsObject.getString(key);
                returnBundle.putString(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return returnBundle;
    }

    public static JSObject convertJSONObjectToJSObject(@Nullable JSONObject jsonObject) {
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

    public static JSArray convertJSONArrayToJSArray(@Nullable JSONArray jsonArray) {
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

    public static JSArray convertStringArrayToJSArray(@Nullable String[] stringArray) {
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
}
