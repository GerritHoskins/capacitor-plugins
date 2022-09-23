package net.bitburst.plugins.mparticle;

import android.annotation.SuppressLint;
import androidx.annotation.Nullable;
import com.mparticle.MParticle;
import com.mparticle.MParticle.EventType;
import com.mparticle.MParticleOptions;
import com.mparticle.identity.IdentityApiRequest;
import com.mparticle.identity.MParticleUser;
import java.util.Objects;

public class Mparticle {

    public interface OnReadyListener {
        void onReady();
    }

    private OnReadyListener onReadyListener;
    private static MParticle instance = MParticle.getInstance();

    public Mparticle(MparticlePlugin plugin) {
        this.onReadyListener =
            () -> {
                if (onReadyListener != null) {
                    onReadyListener.onReady();
                }
            };
        start(plugin.getConfig());
    }

    public void setOnReadyListener(@Nullable OnReadyListener listener) {
        this.onReadyListener = listener;
    }

    @Nullable
    public OnReadyListener getAuthStateChangeListener() {
        return onReadyListener;
    }

    @SuppressLint("MParticleInitialization")
    public void start(PluginConfig mParticleConfig) {
        mParticleKey = mParticleConfig.getString("key");
        mParticleSecret = mParticleConfig.getString("secret");
        MParticleOptions options = MParticleOptions
            .builder(getBridge().getContext())
            .credentials(mParticleKey, mParticleSecret)
            .environment(MParticle.Environment.AutoDetect)
            .dataplan("bitcode_frontend_plan", 4)
            .build();
        MParticle.start(options);
    }

    public static MParticle sharedInstance() {
        if (instance == null) {
            instance = MParticle.getInstance();
        }
        return instance;
    }

    public EventType getEventType(Integer eType) {
        /*int ord = eType;
        for (EventType e : EventType.values()) {
            if (e.ordinal() == ord) {
                return e;
            }
        }*/
        return EventType.Other;
    }

    public MParticleUser currentUser() {
        return Objects.requireNonNull(MParticle.getInstance()).Identity().getCurrentUser();
    }

    public IdentityApiRequest identityRequest(String email, String customerId) {
        return IdentityApiRequest.withEmptyUser().email(email).customerId(customerId).build();
    }
}
