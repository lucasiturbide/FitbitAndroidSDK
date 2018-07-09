package com.mindbodyonline.fitbitintegration.authentication.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import com.mindbodyonline.fitbitintegration.R;
import com.mindbodyonline.fitbitintegration.authentication.AuthenticationHandler;
import com.mindbodyonline.fitbitintegration.authentication.AuthenticationResult;
import com.mindbodyonline.fitbitintegration.authentication.AuthorizationController;
import com.mindbodyonline.fitbitintegration.authentication.ClientCredentials;
import com.mindbodyonline.fitbitintegration.authentication.Scope;

import java.util.HashSet;
import java.util.Set;


public class FitbitLoginActivity extends AppCompatActivity implements AuthenticationHandler {

    public static final String CONFIGURATION_VERSION = "CONFIGURATION_VERSION";
    public static final String AUTHENTICATION_RESULT_KEY = "AUTHENTICATION_RESULT_KEY";
    private static final String CLIENT_CREDENTIALS_KEY = "CLIENT_CREDENTIALS_KEY";
    private static final String SCOPES_KEY = "SCOPES_KEY";
    private WebView loginWebview;

    public static Intent createIntent(Context context, @NonNull ClientCredentials clientCredentials, Set<Scope> scopes) {
        return createIntent(context, null, clientCredentials, scopes);
    }

    public static Intent createIntent(Context context, Integer configVersion, @NonNull ClientCredentials clientCredentials, Set<Scope> scopes) {
        Intent intent = new Intent(context, FitbitLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(CLIENT_CREDENTIALS_KEY, clientCredentials);
        intent.putExtra(CONFIGURATION_VERSION, configVersion);
        intent.putExtra(SCOPES_KEY, scopes.toArray(new Scope[scopes.size()]));

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginWebview = findViewById(R.id.login_webview);

        ClientCredentials clientCredentials = getIntent().getParcelableExtra(CLIENT_CREDENTIALS_KEY);
        Parcelable[] scopes = getIntent().getParcelableArrayExtra(SCOPES_KEY);
        Set<Scope> scopesSet = new HashSet<>();
        for (Parcelable scope : scopes) {
            scopesSet.add((Scope) scope);
        }

        AuthorizationController authorizationController = new AuthorizationController(
                loginWebview,
                clientCredentials,
                this);

        authorizationController.authenticate(scopesSet);

    }

    @Override
    public void onAuthFinished(AuthenticationResult result) {
        loginWebview.setVisibility(View.GONE);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(AUTHENTICATION_RESULT_KEY, result);
        resultIntent.putExtra(CONFIGURATION_VERSION, getIntent().getIntExtra(CONFIGURATION_VERSION, 0));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
