package net.bitburst.plugins.loginprovider.providers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.bitburst.plugins.loginprovider.LoginProviderHelper;
import net.bitburst.plugins.loginprovider.LoginProviderPlugin;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleProvider {

    private static final String VERIFY_TOKEN_URL = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=";
    private static final String FIELD_TOKEN_EXPIRES_IN = "expires_in";
    private static final String FIELD_ACCESS_TOKEN = "accessToken";
    private static final String FIELD_TOKEN_EXPIRES = "expires";

    // see https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInStatusCodes#SIGN_IN_CANCELLED
    private static final int SIGN_IN_CANCELLED = 12501;

    public static final int KAssumeStaleTokenSec = 60;

    private GoogleSignInClient googleSignInClient;
    private LoginProviderPlugin loginProviderPlugin;

    @NonNull
    private JSObject config;

    private String clientId;
    private String[] targetScopes;
    private boolean forceCodeForRefreshToken;

    public GoogleProvider(LoginProviderPlugin loginProviderPlugin, JSObject config) {
        this.loginProviderPlugin = loginProviderPlugin;
        this.config = config;
        this.clientId = config.getString("clientId");
        this.targetScopes = Objects.requireNonNull(config.getString("scope")).split(" ");
        this.forceCodeForRefreshToken = Boolean.TRUE.equals(config.getBoolean("forceCodeForRefreshToken", true));
        this.googleSignInClient = buildGoogleSignInClient();
    }

    public void login(PluginCall call) {
        Intent loginIntent = googleSignInClient.getSignInIntent();
        loginProviderPlugin.startActivityForResult(call, loginIntent, "googleLoginResult");
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

    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions.Builder googleSignInBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(this.clientId)
            .requestEmail();

        if (forceCodeForRefreshToken) googleSignInBuilder.requestServerAuthCode(this.clientId, true);

        String[] scopeArray = this.targetScopes;
        Scope[] scopes = new Scope[scopeArray.length - 1];
        Scope firstScope = new Scope(scopeArray[0]);
        for (int i = 1; i < scopeArray.length; i++) {
            scopes[i - 1] = new Scope(scopeArray[i]);
        }

        googleSignInBuilder.requestScopes(firstScope, scopes);

        GoogleSignInOptions googleSignInOptions = googleSignInBuilder.build();
        googleSignInClient = GoogleSignIn.getClient(loginProviderPlugin.getContext(), googleSignInOptions);

        return googleSignInClient;
    }

    public void handleGoogleLoginResult(final PluginCall call, ActivityResult result) {
        if (call == null) return;

        Task<GoogleSignInAccount> completedTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(
                () -> {
                    try {
                        JSONObject accessTokenObject = getAuthToken(account.getAccount(), true);
                        JSObject data = LoginProviderHelper.createLoginProviderResponsePayload(
                            "GOOGLE",
                            account.getIdToken(),
                            null,
                            account.getEmail(),
                            account.getPhotoUrl(),
                            call.getData().getString("inviteCode")
                        );
                        call.resolve(data);
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

    private JSONObject getAuthToken(Account account, boolean retry) throws Exception {
        AccountManager manager = AccountManager.get(loginProviderPlugin.getContext());
        AccountManagerFuture<Bundle> future = manager.getAuthToken(account, "oauth2:profile email", null, false, null, null);
        Bundle bundle = future.getResult();
        String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
        try {
            return verifyToken(authToken);
        } catch (IOException e) {
            if (retry) {
                manager.invalidateAuthToken("com.google", authToken);
                return getAuthToken(account, false);
            } else {
                throw e;
            }
        }
    }

    private JSONObject verifyToken(String authToken) throws IOException, JSONException {
        URL url = new URL(VERIFY_TOKEN_URL + authToken);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setInstanceFollowRedirects(true);
        String stringResponse = fromStream(new BufferedInputStream(urlConnection.getInputStream()));

        Log.d("AuthenticatedBackend", "token: " + authToken + ", verification: " + stringResponse);
        JSONObject jsonResponse = new JSONObject(stringResponse);
        int expires_in = jsonResponse.getInt(FIELD_TOKEN_EXPIRES_IN);
        if (expires_in < KAssumeStaleTokenSec) {
            throw new IOException("Auth token soon expiring.");
        }
        jsonResponse.put(FIELD_ACCESS_TOKEN, authToken);
        jsonResponse.put(FIELD_TOKEN_EXPIRES, expires_in + (System.currentTimeMillis() / 1000));
        return jsonResponse;
    }

    private static String fromStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
}
