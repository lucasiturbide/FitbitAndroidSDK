package com.mindbodyonline.fitbitintegration.swagger.api;

import retrofit2.Call;
import retrofit2.http.*;

public interface AuthApi {
  /**
   * Retrieve the active state of an OAuth 2.0 token
   * Retrieves the active state of an OAuth 2.0 token. It follows https://tools.ietf.org/html/rfc7662. The response body is not shown because of a swagger bug (https://github.com/domaindrivendev/Swashbuckle/issues/1172). Copy paste the curl request to a terminal to see the response body
   * @param token OAuth 2.0 token to retrieve the state of (required)
   * @return Call&lt;Void&gt;
   */
  @retrofit2.http.FormUrlEncoded
  @POST("oauth2/introspect")
  Call<Void> oauth2IntrospectPost(
    @retrofit2.http.Field("token") String token
  );

  /**
   * Get OAuth 2 access token
   * Retrieves an OAuth 2 access token.
   * @param clientId This is your Fitbit API application id from your settings on dev.fitbit.com. (required)
   * @param grantType Authorization grant type. Valid values are &#39;authorization_code&#39; and &#39;refresh_token&#39;. (required)
   * @param authorization The Authorization header must be set to &#39;Basic&#39; followed by a space, then the Base64 encoded string of your application&#39;s client id and secret concatenated with a colon. For example, &#39;Basic Y2xpZW50X2lkOmNsaWVudCBzZWNyZXQ&#x3D;&#39;. The Base64 encoded string, &#39;Y2xpZW50X2lkOmNsaWVudCBzZWNyZXQ&#x3D;&#39;, is decoded as &#39;client_id:client secret&#39;. (optional)
   * @param code Authorization code received in the redirect as URI parameter. Required if using the Authorization Code flow. (optional)
   * @param expiresIn Specify the desired access token lifetime. Defaults to 28800 for 8 hours. The other valid value is 3600 for 1 hour. (optional)
   * @param redirectUri Uri to which the access token will be sent if the request is successful. Required if specified in the redirect to the authorization page. Must be exact match. (optional)
   * @param refreshToken Refresh token issued by Fitbit. Required if &#39;grant_type&#39; is &#39;refresh_token&#39;. (optional)
   * @param state Required if specified in the redirect uri of the authorization page. Must be an exact match. (optional)
   * @return Call&lt;Void&gt;
   */
  @POST("oauth2/token")
  Call<Void> oauth2TokenPost(
    @retrofit2.http.Query("client_id") String clientId, @retrofit2.http.Query("grant_type") String grantType, @retrofit2.http.Header("Authorization") String authorization, @retrofit2.http.Query("code") String code, @retrofit2.http.Query("expires_in") String expiresIn, @retrofit2.http.Query("redirect_uri") String redirectUri, @retrofit2.http.Query("refresh_token") String refreshToken, @retrofit2.http.Query("state") String state
  );

}
