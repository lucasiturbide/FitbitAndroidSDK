package com.mindbodyonline.fitbitintegration;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.mindbodyonline.fitbitintegration.authentication.AuthenticationConfiguration;
import com.mindbodyonline.fitbitintegration.authentication.AuthenticationConfigurationBuilder;
import com.mindbodyonline.fitbitintegration.authentication.AuthenticationManager;
import com.mindbodyonline.fitbitintegration.authentication.ClientCredentials;
import com.mindbodyonline.fitbitintegration.authentication.Scope;
import com.mindbodyonline.fitbitintegration.service.api.endpoint.Environment;

import java.util.concurrent.TimeUnit;

public class FitbitIntegrationApplication extends Application {

    public static AuthenticationConfiguration generateAuthenticationConfiguration(Context context, Class<? extends Activity> mainActivityClass) {

        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;

            // Load clientId and redirectUrl from application manifest
            String clientId = bundle.getString("com.mindbodyonline.fitbitintegration.CLIENT_ID");
            String redirectUrl = bundle.getString("com.mindbodyonline.fitbitintegration.REDIRECT_URL");

            FitbitAuthEndpoint authEndpoint = new FitbitAuthEndpoint();

            ClientCredentials CLIENT_CREDENTIALS = new ClientCredentials(clientId, authEndpoint.oauthClientId(Environment.PRODUCTION), redirectUrl);

            return new AuthenticationConfigurationBuilder()

                    .setClientCredentials(CLIENT_CREDENTIALS)
                    .setBeforeLoginActivity(new Intent(context, mainActivityClass))
                    .setTokenExpiresIn(TimeUnit.DAYS.toMillis(1))
                    .addRequiredScopes(Scope.profile)
                    .addOptionalScopes(Scope.activity, Scope.weight)
                    .setLogoutOnAuthFailure(true)

                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AuthenticationManager.configure(this, generateAuthenticationConfiguration(this, FitbitLoginActivity.class));
    }
}
