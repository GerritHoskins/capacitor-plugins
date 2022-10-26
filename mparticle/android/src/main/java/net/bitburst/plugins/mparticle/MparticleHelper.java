package net.bitburst.plugins.mparticle;

import com.getcapacitor.JSObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class MparticleHelper {

    static Map<String, String> ConvertStringMap(JSONObject jsonObject) throws JSONException {
        Map<String, String> map = new HashMap<>();
        if (jsonObject != null) {
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                map.put(key, jsonObject.getString(key));
            }
        }
        return map;
    }

    static Map<String, Object> MapObjectList(List<Object> list) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        JSONObject temp;
        for (int i = 0; i < list.size(); i++) {
            temp = (JSONObject) list.get(i);
            map.put(temp.getString("name"), temp.getString("value"));
        }
        return map;
    }

    static long parseString(String longString) {
        try {
            return Long.parseLong(longString);
        } catch (NumberFormatException ex) {
            return 0L;
        }
    }
}
