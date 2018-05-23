package com.mindbodyonline.fitbitintegration.service.api.okhttp.interceptor

import com.mindbodyonline.fitbitintegration.service.api.endpoint.AuthEndpoint
import com.mindbodyonline.fitbitintegration.service.models.auth.OAuthAccessToken
import com.mindbodyonline.fitbitintegration.service.api.OAuthDataService
import com.mindbodyonline.fitbitintegration.service.api.RefreshTokenService
import okhttp3.*
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

/**
 * OkHttp Interceptor that appends the active token based on expiration prior to opening the socket
 *
 * Not meant to be attached to OkHttpClient that handles auth!
 *
 * MB App logout policy:
 *      If the user received an HTTP 401 with a valid auth header on any data call
 *      If the user received any HTTP error during an auth request
 *      If the user received an invalid auth token
 */
class AuthenticationInterceptor(val oAuthDataService: OAuthDataService) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        //Auth calls should be pre-authed with a `Basic` token
        if(chain.request().url().toString().equals(oAuthDataService.getTokenServiceUrl(), true)) {
            return chain.proceed(chain.request())
        }

        //Check token validity
        val token = oAuthDataService.token ?: return noTokenResponse(chain.request())
        //If valid, attach as Header to request, continue in the chain
        if(!token.needsRefresh()) {
            return proceedInChain(chain, token)
        } else {
            //All active Threads will pause here
            return synchronized(oAuthDataService) {
                //Another active call Thread could have beat us to it
                val token = oAuthDataService.token
                if (token?.needsRefresh() == false) {
                    return proceedInChain(chain, token)
                    //We're the first thread to recognize that we need a token
                } else if (token != null) {
                    //Handles synchronously pausing, resuming, and draining queue
                    return try {
                        proceedInChain(chain, oAuthDataService.refreshTokenWithLatch(token))
                    } catch (error: RefreshTokenService.HttpError) {
                        Response.Builder()
                                .code(error.code)
                                .protocol(Protocol.HTTP_1_1)
                                .body(ResponseBody.create(MediaType.parse(error.contentType), error.errorBody))
                                .message(error.errorBody)
                                .request(chain.request())
                                .build()
                    }
                } else {
                    //Token is required
                    return noTokenResponse(chain.request())
                }
            }
        }
    }

    private fun noTokenResponse(request: Request) = Response.Builder()
            .protocol(Protocol.HTTP_1_1)
            .code(HTTP_UNAUTHORIZED)
            .body(ResponseBody.create(MediaType.parse("text/plain"), "No token supplied"))
            .message("No token supplied")
            .request(request)
            .build()

    private fun proceedInChain(chain: Interceptor.Chain, validToken: OAuthAccessToken): Response {
        val request = chain.request()
        val header: Pair<String, String> = AuthEndpoint.bearerToken(validToken)
        val authedRequest = request.newBuilder().addHeader(header.first, header.second).build()
        return chain.proceed(authedRequest)
    }
}