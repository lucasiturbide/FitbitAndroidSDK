package com.mindbodyonline.fitbitintegration.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mindbodyonline.fitbitintegration.authentication.ui.FitbitLoginActivity;
import com.mindbodyonline.fitbitintegration.service.FitbitService;
import com.mindbodyonline.fitbitintegration.service.models.auth.OAuthAccessToken;
import com.mindbodyonline.fitbitintegration.service.storage.OAuthAccessTokenStorage;
import com.mindbodyonline.fitbitintegration.service.storage.SharedPreferenceTokenStorage;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jboggess on 9/14/16.
 */
public class AuthenticationManager {

    private static final int RESULT_CODE = 1;
    private static boolean configured = false;
    private static AuthenticationConfiguration authenticationConfiguration;
    private static OAuthAccessToken currentAccessToken;
    private static OAuthAccessTokenStorage tokenStorage;
    private static FitbitService apiService;

    public static void configure(Context context, AuthenticationConfiguration authenticationConfiguration) {
        AuthenticationManager.authenticationConfiguration = authenticationConfiguration;
        SharedPreferences preferences = context.getSharedPreferences("Fitbit", Context.MODE_PRIVATE);
        tokenStorage = new SharedPreferenceTokenStorage(preferences);
        configured = true;
        apiService = new FitbitService(preferences);
    }

    public static synchronized OAuthAccessToken getCurrentAccessToken() {
        checkPreconditions();
        if (currentAccessToken == null) {
            currentAccessToken = tokenStorage.getToken();
        }
        return currentAccessToken;
    }

    public static synchronized void setCurrentAccessToken(OAuthAccessToken currentAccessToken) {
        checkPreconditions();
        AuthenticationManager.currentAccessToken = currentAccessToken;

        //Save to shared tokenStorage
        tokenStorage.setToken(currentAccessToken);
    }

    public static void login(Activity activity) {
        Set<Scope> scopes = new HashSet<>();
        scopes.addAll(authenticationConfiguration.getRequiredScopes());
        scopes.addAll(authenticationConfiguration.getOptionalScopes());

        Intent intent = FitbitLoginActivity.createIntent(
                activity,
                authenticationConfiguration.getClientCredentials(),
                scopes);

        activity.startActivityForResult(intent, RESULT_CODE);
    }

    public static boolean onActivityResult(int requestCode, int resultCode, Intent data, @NonNull AuthenticationHandler authenticationHandler) {
        checkPreconditions();
        switch (requestCode) {
            case (RESULT_CODE): {
                if (resultCode == Activity.RESULT_OK) {
                    AuthenticationResult authenticationResult = data.getParcelableExtra(FitbitLoginActivity.AUTHENTICATION_RESULT_KEY);

                    if (authenticationResult.isSuccessful() && authenticationResult.getAccessToken() != null) {
                        Set<Scope> grantedScopes = new HashSet<>(authenticationResult.getAccessToken().getScopes());
                        Set<Scope> requiredScopes = new HashSet<>(authenticationConfiguration.getRequiredScopes());

                        requiredScopes.removeAll(grantedScopes);
                        if (requiredScopes.size() > 0) {
                            authenticationResult = AuthenticationResult.missingRequiredScopes(requiredScopes);
                        } else {
                            setCurrentAccessToken(authenticationResult.getAccessToken());
                        }
                    }

                    authenticationHandler.onAuthFinished(authenticationResult);
                } else {
                    authenticationHandler.onAuthFinished(AuthenticationResult.dismissed());
                }
                return true;
            }
        }

        return false;
    }

    public static boolean isLoggedIn() {
        checkPreconditions();
        OAuthAccessToken currentAccessToken = getCurrentAccessToken();
        return currentAccessToken != null && !currentAccessToken.needsRefresh();
    }

    public static void logout(final Activity contextActivity) {
        logout(contextActivity, null);
    }

    public static void logout(final Activity contextActivity, @Nullable final LogoutTaskCompletionHandler logoutTaskCompletionHandler) {
        checkPreconditions();
        if (!isLoggedIn()) {
            return;
        }

        final ClientCredentials clientCredentials = getAuthenticationConfiguration().getClientCredentials();
        String tokenString = String.format("%s:%s", clientCredentials.getClientId(), clientCredentials.getClientSecret());
        String currentToken = getCurrentAccessToken().getAccess_token() != null ? getCurrentAccessToken().getAccess_token() : "";
        apiService.getTokenService().revokeToken(tokenString, currentToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()){
                    Intent beforeLoginActivity = authenticationConfiguration.getBeforeLoginActivity();
                    if (beforeLoginActivity != null) {
                        contextActivity.startActivity(beforeLoginActivity);
                    }
                    if (logoutTaskCompletionHandler != null) {
                        logoutTaskCompletionHandler.logoutSuccess();
                    }
                } else {
                    logoutError(response.message(), contextActivity, logoutTaskCompletionHandler);
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                logoutError("", contextActivity, logoutTaskCompletionHandler);
            }
        });
        setCurrentAccessToken(null);
    }

    private static void logoutError(String message, Activity contextActivity, @Nullable LogoutTaskCompletionHandler logoutTaskCompletionHandler){
        Intent beforeLoginActivity = authenticationConfiguration.getBeforeLoginActivity();
        if (beforeLoginActivity != null) {
            contextActivity.startActivity(beforeLoginActivity);
        }
        if (logoutTaskCompletionHandler != null) {
            logoutTaskCompletionHandler.logoutError(message);
        }
    }

    public static AuthenticationConfiguration getAuthenticationConfiguration() {
        checkPreconditions();
        return authenticationConfiguration;
    }

    private static void checkPreconditions() {
        if (!configured) {
            throw new IllegalArgumentException("You must call `configure` on AuthenticationManager before using its methods!");
        }
    }

}
