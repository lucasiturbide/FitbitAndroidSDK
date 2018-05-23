package com.mindbodyonline.fitbitintegration.service.api

import com.mindbodyonline.fitbitintegration.service.models.auth.OAuthAccessToken
import com.mindbodyonline.fitbitintegration.service.models.auth.RequestTokenModel


interface RefreshTokenService {
    class HttpError(val code: Int, val contentType: String, val errorBody: String): Throwable()

    interface TokenListener {
        fun onTokenReceived(token: OAuthAccessToken)
        fun onHttpError(error: HttpError)
        fun onError(error: Throwable)
    }
    fun refreshToken(outdatedToken: OAuthAccessToken, listener: TokenListener)
    fun getToken(requestTokenModel: RequestTokenModel, listener: TokenListener)
    fun getTokenServiceUrl(): String
}