package com.mindbodyonline.fitbitintegration.service.api.endpoint

import android.util.Base64
import com.mindbodyonline.fitbitintegration.service.models.auth.OAuthAccessToken

enum class Environment {
    PRODUCTION, DISASTER_RECOVERY, STAGING, INTEGRATION, DEVELOPMENT
}

abstract class AuthEndpoint {
    companion object {
        const val OAUTH_HEADER_KEY: String = "Authorization"
        const val OAUTH_BASIC: String = "Basic"
        const val OAUTH_BEARER: String = "Bearer"

        fun formatBearerToken(accessToken: String) = "$OAUTH_BEARER $accessToken"

        fun bearerToken(token: OAuthAccessToken): Pair<String, String> {
            val accessToken = token.access_token
            if(accessToken?.isNotBlank() == true) {
                return OAUTH_HEADER_KEY to formatBearerToken(accessToken)
            }
            throw IllegalArgumentException("OAuthAccessToken has an invalid refresh token")
        }
    }

    abstract fun oauthClientId(env: Environment): String
    abstract fun oauthSecret(env: Environment) : String

    open fun basicHeader(env: Environment): Pair<String, String> {
        val basicToken = Base64.encode("${oauthClientId(env)}:${oauthSecret(env)}".toByteArray(), Base64.NO_WRAP)
        return OAUTH_HEADER_KEY to "$OAUTH_BASIC $basicToken"
    }

    open fun auth(env: Environment) : String {
        return when(env) {
            Environment.PRODUCTION -> "https://api.fitbit.com/"
            else -> ""
        }
    }
}

abstract class Endpoint {
    abstract val authEndpoint: AuthEndpoint

    fun auth(env: Environment): String = authEndpoint.auth(env)
}


