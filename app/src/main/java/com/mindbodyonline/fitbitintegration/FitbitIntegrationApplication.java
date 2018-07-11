package com.mindbodyonline.fitbitintegration;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.mindbodyonline.fitbitsdk.authentication.AuthenticationConfiguration;
import com.mindbodyonline.fitbitsdk.authentication.AuthenticationConfigurationBuilder;
import com.mindbodyonline.fitbitsdk.authentication.AuthenticationManager;
import com.mindbodyonline.fitbitsdk.authentication.ClientCredentials;
import com.mindbodyonline.fitbitsdk.authentication.Scope;

import java.util.concurrent.TimeUnit;

public class FitbitIntegrationApplication extends Application {

    public static AuthenticationConfiguration generateAuthenticationConfiguration(Context context, Class<? extends Activity> mainActivityClass) {

        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;

            // Load clientId and redirectUrl from application manifest
            String clientId = bundle.getString("com.mindbodyonline.fitbitintegration.CLIENT_ID");
            String redirectUrl = bundle.getString("com.mindbodyonline.fitbitintegration.REDIRECT_URL");
            String clientSecret = bundle.getString("com.mindbodyonline.fitbitintegration.CLIENT_SECRET");

            ClientCredentials clientCredentials = new ClientCredentials(clientId, clientSecret, redirectUrl);

            return new AuthenticationConfigurationBuilder()

                    .setClientCredentials(clientCredentials)
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
