package net.bitburst.plugins.mparticle;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.mparticle.MParticle;
import com.mparticle.MParticle.EventType;
import com.mparticle.MParticleOptions;
import com.mparticle.commerce.Product;
import com.mparticle.commerce.Product.Builder;
import com.mparticle.identity.IdentityApiRequest;
import com.mparticle.identity.MParticleUser;
import java.util.*;
import java.util.Dictionary;
import org.json.JSONException;

public class Mparticle {

    public String echo(String value) {
        return value;
    }

    public EventType getEventType(int ord) {
        for (EventType e : EventType.values()) {
            if (e.ordinal() == ord) {
                return e;
            }
        }
        return EventType.Other;
    }

    public MparticleUser currentUser() {
        return Mparticle.getInstance().Identity().getCurrentUser();
    }

    public IdentityApiRequest identityRequest(String email, String customerId) {
        IdentityApiRequest identityRequest = IdentityApiRequest.withEmptyUser().email(email).customerId(customerId).build();
        return identityRequest;
    }
}
