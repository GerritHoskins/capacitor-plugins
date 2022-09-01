package net.bitburst.plugins.loginprovider.providers;

import android.content.Intent;
import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.bitburst.plugins.loginprovider.LoginProvider;

public class GoogleProvider {

    private static final String VERIFY_TOKEN_URL = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=";
    private static final String FIELD_TOKEN_EXPIRES_IN = "expires_in";
    private static final String FIELD_ACCESS_TOKEN = "accessToken";
    private static final String FIELD_TOKEN_EXPIRES = "expires";

    // see https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInStatusCodes#SIGN_IN_CANCELLED
    private static final int SIGN_IN_CANCELLED = 12501;

    public static final int KAssumeStaleTokenSec = 60;

    private GoogleSignInClient googleSignInClient;
    private LoginProvider pluginImplementation;

    public GoogleProvider(LoginProvider pluginImplementation) {
        this.pluginImplementation = pluginImplementation;
        this.googleSignInClient = buildGoogleSignInClient();
    }

    public Intent login(PluginCall call) {
        googleSignInClient = buildGoogleSignInClient(call);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        return signInIntent;
    }

    public void logout() {
        googleSignInClient.signOut();
    }

    public void refresh(final PluginCall call) {
        call.reject("I don't know how to refresh token on Android");
    }

    public void initialize(final PluginCall call) {
        call.resolve();
    }

    public void handleOnActivityResult(final PluginCall call, ActivityResult result) {
        if (call == null) return;

        Task<GoogleSignInAccount> completedTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // The accessToken is retrieved by executing a network request against the Google API, so it needs to run in a thread
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(
                () -> {
                    try {
                        //  JSONObject accessTokenObject = getAuthToken(account.getAccount(), true);

                        JSObject authentication = new JSObject();
                        authentication.put("idToken", account.getIdToken());
                        //   authentication.put(FIELD_ACCESS_TOKEN, accessTokenObject.get(FIELD_ACCESS_TOKEN));
                        //   authentication.put(FIELD_TOKEN_EXPIRES, accessTokenObject.get(FIELD_TOKEN_EXPIRES));
                        //    authentication.put(FIELD_TOKEN_EXPIRES_IN, accessTokenObject.get(FIELD_TOKEN_EXPIRES_IN));

                        JSObject user = new JSObject();
                        user.put("serverAuthCode", account.getServerAuthCode());
                        user.put("idToken", account.getIdToken());
                        user.put("authentication", authentication);

                        user.put("displayName", account.getDisplayName());
                        user.put("email", account.getEmail());
                        user.put("familyName", account.getFamilyName());
                        user.put("givenName", account.getGivenName());
                        user.put("id", account.getId());
                        user.put("imageUrl", account.getPhotoUrl());

                        call.resolve(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                        call.reject("Something went wrong while retrieving access token", e);
                    }
                }
            );
        } catch (ApiException e) {
            if (SIGN_IN_CANCELLED == e.getStatusCode()) {
                call.reject("The user canceled the sign-in flow.", "" + e.getStatusCode());
            } else {
                call.reject("Something went wrong", "" + e.getStatusCode());
            }
        }
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        return buildGoogleSignInClient(null);
    }

    private GoogleSignInClient buildGoogleSignInClient(@Nullable PluginCall call) {
        GoogleSignInOptions.Builder googleSignInBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("clientId")
            .requestEmail();

        /* if (forceCodeForRefreshToken) {
            googleSignInBuilder.requestServerAuthCode(clientId, true);
        }*/

        /* String[] scopeArray = getConfig().getArray("scopes", new String[] {});
        Scope[] scopes = new Scope[scopeArray.length - 1];
        Scope firstScope = new Scope(scopeArray[0]);
        for (int i = 1; i < scopeArray.length; i++) {
            scopes[i - 1] = new Scope(scopeArray[i]);
        }
        googleSignInBuilder.requestScopes(firstScope, scopes);*/

        GoogleSignInOptions googleSignInOptions = googleSignInBuilder.build();
        googleSignInClient = GoogleSignIn.getClient(this.pluginImplementation.getPlugin().getBridge().getContext(), googleSignInOptions);

        return googleSignInClient;
    }
}
