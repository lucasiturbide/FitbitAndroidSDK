package com.mindbodyonline.fitbitintegration.authentication;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.mindbodyonline.fitbitintegration.service.FitbitService;
import com.mindbodyonline.fitbitintegration.service.models.auth.AuthorizeCodeTokenModel;
import com.mindbodyonline.fitbitintegration.service.models.auth.OAuthAccessToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jboggess on 9/14/16.
 */
public class AuthorizationController implements UrlChangeHandler {
    private final static String TAG = "AuthorizationController";
    private final static String BASE_URL = "https://www.fitbit.com/";
    private static final String AUTHORIZE_URL_FORMAT = "oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s&scope=%s";
    private static final Pattern TOKEN_MATCH_PATTERN = Pattern.compile("#access_token=(.+)&");
    private static final Pattern DISMISSED_PATTERN = Pattern.compile("error_description=The\\+user\\+denied\\+the\\+request");
    private WebView webView;
    private ClientCredentials clientCredentials;
    private AuthenticationHandler authenticationHandler;

    public AuthorizationController(WebView webView, ClientCredentials clientCredentials, AuthenticationHandler authenticationHandler) {
        this.webView = webView;
        this.clientCredentials = clientCredentials;
        this.authenticationHandler = authenticationHandler;
    }

    public void authenticate(@NonNull Set<Scope> scopes) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new AuthenticationWebviewClient(this));
        Uri.Builder uriBuilder = Uri.parse(BASE_URL).buildUpon()
                .appendPath("oauth2")
                .appendPath("authorize")
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("client_id", clientCredentials.getClientId())
                .appendQueryParameter("redirect_url", clientCredentials.getRedirectUrl())
                .appendQueryParameter("scope", TextUtils.join(" ", scopes));

        webView.loadUrl(uriBuilder.toString());
    }

    private List<Scope> parseScopes(String scopes) {
        String[] scopesArray = scopes.split(" ");
        List<Scope> scopesList = new ArrayList<>();
        for (String scopeStr : scopesArray) {
            Scope scope = Scope.fromString(scopeStr);
            if (scope != null) {
                scopesList.add(scope);
            }
        }

        return scopesList;
    }


    private void parseSuccess(String urlString) {
        urlString = urlString.replaceFirst("#", "?");
        Uri uri = Uri.parse(urlString);

        Log.d("DEBUG", "Received URL: " + urlString );

        final String authorizationCode = uri.getQueryParameter("code");

        AuthorizeCodeTokenModel codeModel = new AuthorizeCodeTokenModel(clientCredentials.getClientId(), "authorization_code", clientCredentials.getRedirectUrl(), authorizationCode);

        FitbitService fitbitService = new FitbitService(webView.getContext().getSharedPreferences("Fitbit", Context.MODE_PRIVATE));
        fitbitService.getTokenService().authorizeCode(codeModel).enqueue(new Callback<OAuthAccessToken>() {
            @Override
            public void onResponse(@NonNull Call<OAuthAccessToken> call, @NonNull Response<OAuthAccessToken> accessTokenObject) {
                authenticationHandler.onAuthFinished(AuthenticationResult.success(accessTokenObject.body()));
            }

            @Override
            public void onFailure(@NonNull Call<OAuthAccessToken> call, @NonNull Throwable t) {
                authenticationHandler.onAuthFinished(AuthenticationResult.error(t.getMessage()));
            }
        });

//        //Save Auth Token
//        final String accessToken = uri.getQueryParameter("access_token");
//        Long expiresOn = Long.parseLong(uri.getQueryParameter("expires_in")) + System.currentTimeMillis() / 1000;
//        Integer expiresIn = Integer.parseInt(uri.getQueryParameter("expires_in"));
//        List<Scope> scopes = parseScopes(uri.getQueryParameter("scope"));
//        final OAuthAccessToken accessTokenObject = new OAuthAccessToken(accessToken, expiresIn, "", scopes, "", "");

    }


    @Override
    public void onUrlChanged(String newUrl) {
        if (newUrl.startsWith(clientCredentials.getRedirectUrl())) {
            webView.setVisibility(View.GONE);
            Matcher successMatcher = TOKEN_MATCH_PATTERN.matcher(newUrl);
            Matcher dismissedMatcher = DISMISSED_PATTERN.matcher(newUrl);
            if (successMatcher.find()) {
                parseSuccess(newUrl);
            } else if (dismissedMatcher.find()) {
                authenticationHandler.onAuthFinished(AuthenticationResult.dismissed());
            } else {
                Log.e(TAG, "Error getting access code from url");
                authenticationHandler.onAuthFinished(AuthenticationResult.error(
                        String.format("Url missing access code: %s", newUrl)));
            }
        }
    }

    @Override
    public void onLoadError(int errorCode, CharSequence description) {
        authenticationHandler.onAuthFinished(AuthenticationResult.error(description.toString()));
    }
}
